package me.deejack.animeviewer.gui.utils;

import com.sun.javafx.webkit.WebConsoleListener;
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
import netscape.javascript.JSException;
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

  private static int failCount = 0;

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
      alert.getDialogPane().lookupButton(ButtonType.YES).setOnMouseClicked((btnEvent) -> event.consume());
      alert.showAndWait();
    });
    stage.show();
    return new Pair<>(browser, stage);
  }

  public static void getOpenloadLink(String link, CallBack<String> callback) {
    URL url;
    try {
      url = new URL(link); // FAre metodo unico per tutte le volte che lo uso
    } catch (MalformedURLException e) {
      handleException(e);
      return;
    }
    Pair<WebView, Stage> pair = createWebView();
    WebEngine engine = pair.getKey().getEngine();
    engine.getLoadWorker().exceptionProperty().addListener((event, oldValue, newValue) -> handleException(newValue));
    engine.setUserAgent(UserAgents.WIN10_FIREFOX.getValue());
    URL finalUrl = url;
    engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
      System.out.println(newValue + " " + engine.getLocation());
      if (newValue == Worker.State.SUCCEEDED && (engine.getLocation().contains(finalUrl.getProtocol() + "://" + finalUrl.getHost()))) {
        Document document = engine.getDocument();
        engine.getLoadWorker().cancel();
        pair.getValue().close();
        System.out.println("YEAH");
        pair.getKey().getEngine().getLoadWorker().cancel();
        findStreamingLink(document, engine, callback, url, link);
      }
    });
    WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
      System.out.println(message + "[at " + lineNumber + "]");
    });
    engine.load(link);
    engine.locationProperty().addListener((event, oldValue, newValue) -> engine.getLoadWorker().cancel());
  }

  private static void findStreamingLink(Document document, WebEngine engine, CallBack<String> callBack, URL url, String link) {
    String streamingLink;
    if (document.getElementsByTagName("video").getLength() > 0 && document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src") != null)
      streamingLink = document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
    else {
      try {
        Object useless = engine.executeScript("var event = document.createEvent(\"HTMLEvents\");\n" +
                "\n" +
                "  event.initEvent(\"click\", true, true);\n" +
                "document.getElementById('videooverlay').dispatchEvent(event);console.log(document.getElementsByTagName('video')[0].src);");

        System.out.println(document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src"));
        if (document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src") == null)
          throw new JSException("Video attribute SRC is null :(, link: " + link);
        streamingLink = url.getHost() + document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
        //streamingLink = finalUrl.getHost() + "/stream/" + document.getElementById("lqEH1").getTextContent();
      } catch (Exception jsShit) {
        failCount++;
        if (failCount == 11) {
          hideWaitLoad();
          logError(new Exception("Error for url " + link, jsShit));
          new Alert(Alert.AlertType.ERROR, "Error :(, you can try again but maybe the site isn't supported", ButtonType.OK).showAndWait();
        } else
          engine.load(link);
        return;
      }
      failCount = 0;
    }
    if (!streamingLink.startsWith("https:"))
      streamingLink = "https:" + (streamingLink.startsWith("//") ? "" : "//") + streamingLink;
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
    callBack.onSuccess(streamingLink);
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
