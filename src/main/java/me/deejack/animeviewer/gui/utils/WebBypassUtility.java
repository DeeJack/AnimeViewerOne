package me.deejack.animeviewer.gui.utils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import me.deejack.animeviewer.gui.bypasser.SiteBypasser;
import me.deejack.animeviewer.gui.bypasser.defaultbypasser.GeneralBypasser;
import me.deejack.animeviewer.logic.extensions.ExtensionLoader;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.utils.UserAgents;
import org.apache.logging.log4j.LogManager;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public final class WebBypassUtility {
  private static final List<SiteBypasser> bypassers = ExtensionLoader.loadBypassers();

  private WebBypassUtility() {
  }

  public static void bypassSite(String link, Callback<String> callback) {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingStreaming"));
    boolean bypassed = false;
    URL url = getURL(link);
    if (url == null) {
      handleException(new RuntimeException("This link isn't a valid URL: " + link));
      return;
    }
    for (SiteBypasser bypasser : bypassers) {
      if (Arrays.asList(bypasser.getCompatibleLinks()).contains(url.getHost())) {
        bypassed = true;
        bypasser.getDirectLink(link, callback);
      }
    }
    if (!bypassed)
      new GeneralBypasser().getDirectLink(link, callback);
  }

  private static URL getURL(String link) {
    try {
      return new URL(link);
    } catch (MalformedURLException exception) {
      return null;
    }
  }

  public static void bypassCloudflare(String pageLink, Callback<List<HttpCookie>> callback) {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(() -> bypassCloudflare(pageLink, callback));
      return;
    }
    URL pageUrl;
    try {
      pageUrl = new URL(pageLink);
    } catch (IOException exc) {
      handleException(exc);
      return;
    }
    CookieManager cookieManager = registerCookieManager();

    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingCloudflare"));
    Pair<WebView, Stage> pair = createWebView();
    WebEngine engine = pair.getKey().getEngine();
    engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue == Worker.State.SUCCEEDED && engine.getLocation().contains(pageUrl.getHost())) { // TODO però così funziona solo per dreamsub
        org.w3c.dom.Document document = engine.getDocument();
        for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
          if (cookie.getName().equalsIgnoreCase("cf_clearance")) {
            hideWaitLoad();
            callback.onSuccess(cookieManager.getCookieStore().getCookies());
            pair.getValue().close();
          }
        }
      }
    });
    engine.setOnError(event -> handleException(event.getException()));
    engine.setOnAlert(event -> LogManager.getLogger().warn(event.getData()));
    engine.load(pageLink);
  }

  public static Pair<WebView, Stage> createWebView() {
    WebView browser = new WebView();
    WebEngine engine = browser.getEngine();
    engine.setUserAgent(UserAgents.WIN10_FIREFOX.getValue());
    Stage stage = new Stage(StageStyle.UNDECORATED);
    stage.setScene(new Scene(new HBox(browser), 1, 1));
    stage.setOnCloseRequest((event) -> {
      Alert alert = new Alert(Alert.AlertType.WARNING, LocalizedApp.getInstance().getString("WebViewClosingAlert"), ButtonType.YES, ButtonType.NO);
      alert.getDialogPane().lookupButton(ButtonType.YES).setOnMouseClicked((btnEvent) -> {
        hideWaitLoad();
        engine.getLoadWorker().cancel();
      });
      alert.getDialogPane().lookupButton(ButtonType.NO).setOnMouseClicked((btnEvent) -> event.consume());
      alert.showAndWait();
    });
    stage.show();
    return new Pair<>(browser, stage);
  }

  public static CookieManager registerCookieManager() {
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    CookieHandler.setDefault(cookieManager);
    return cookieManager;
  }

  @FunctionalInterface
  public interface Callback<T> {
    void onSuccess(T t);
  }
}
