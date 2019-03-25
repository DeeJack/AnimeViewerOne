package me.deejack.animeviewer.gui.utils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
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
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.utils.UserAgents;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

public final class WebBypassUtility {

  private WebBypassUtility() {
  }

  public static Pair<WebView, Stage> createWebView() {
    WebView browser = new WebView();
    WebEngine engine = browser.getEngine();
    engine.setUserAgent(UserAgents.WIN10_FIREFOX.getValue());
    Stage stage = new Stage(StageStyle.UNDECORATED);
    stage.setScene(new Scene(new HBox(browser), 1, 1));
    stage.show();
    return new Pair<>(browser, stage);
  }

  public static void getOpenloadLink(String link, String hostName, CallBack<String> callback) {
    URL url = null;
    try {
      url = new URL(link);
    } catch (MalformedURLException e) {
      handleException(e);
      return;
    }
    Pair<WebView, Stage> pair = createWebView();
    WebEngine engine = pair.getKey().getEngine();
    URL finalUrl = url;
    System.out.println(finalUrl.getProtocol() + finalUrl.getHost());
    engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
      System.out.println(newValue + " " + engine.getLocation());
      if (newValue == Worker.State.SUCCEEDED && (engine.getLocation().contains(finalUrl.getProtocol() + "://" + finalUrl.getHost()))) {
        Document document = engine.getDocument();
        System.out.println("YEAH");
        String streamingLink;
        if (document.getElementsByTagName("video").getLength() > 0 && document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src") != null)
          streamingLink = document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
        else {
          try {
            engine.executeScript("document.getElementById('videooverlay').click();");
            System.out.println(document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src"));
            System.out.println();
            //streamingLink = hostName + document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
            streamingLink = hostName + "/stream/" + document.getElementById("lqEH1").getTextContent();
          } catch (Exception jsShit) {
            hideWaitLoad();
            pair.getKey().getEngine().getLoadWorker().cancel();
            pair.getValue().close();
            logError(new Exception("Error for url " + link, jsShit));
            new Alert(Alert.AlertType.ERROR, "Error :(, try again but maybe the site isn't supported", ButtonType.OK).showAndWait();
            return;
          }
        }
        if (!streamingLink.startsWith("https:"))
          streamingLink = "https:" + streamingLink;
        System.out.println(streamingLink + " AAAAAAAAAAA");
        Connection connection = Jsoup.connect(streamingLink);
        connection.followRedirects(true);
        connection.ignoreContentType(true);
        try {
          Connection.Response response = connection.execute();
          streamingLink = response.url().toString();
        } catch (Exception ignored) {
          return;
        }
        pair.getKey().getEngine().getLoadWorker().cancel();
        pair.getValue().close();
        callback.onSuccess(streamingLink);
      }
    });
    engine.load(link);
  }

  public static void bypassCloudflare(String pageLink, WebBypassUtility.CallBack<List<HttpCookie>> callback) {
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

    LoadingUtility.showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingCloudflare"));
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

  public static CookieManager registerCookieManager() {
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    CookieHandler.setDefault(cookieManager);
    return cookieManager;
  }

  @FunctionalInterface
  public interface CallBack<T> {
    void onSuccess(T t);
  }
}
