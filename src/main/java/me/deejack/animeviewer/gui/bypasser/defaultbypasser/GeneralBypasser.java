package me.deejack.animeviewer.gui.bypasser.defaultbypasser;

import com.sun.javafx.webkit.WebConsoleListener;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import me.deejack.animeviewer.gui.bypasser.SiteBypasser;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.utils.UserAgents;
import netscape.javascript.JSException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.gui.utils.WebBypassUtility.createWebView;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

/**
 *
 */
public class GeneralBypasser implements SiteBypasser {
  private static int failCount = 0;

  private static void findStreamingLink(Document document, WebEngine engine, WebBypassUtility.Callback<String> callBack, URL url, String link) {
    String streamingLink;
    if (document.getElementsByTagName("video").getLength() > 0 && document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src") != null)
      streamingLink = document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
    else {
      try {
        engine.executeScript("document.getElementById('videooverlay').click();");
       /* Object useless = engine.executeScript("var event = document.createEvent(\"HTMLEvents\");\n" +
                "\n" +
                "  event.initEvent(\"click\", true, true);\n" +
                "document.getElementById('videooverlay').dispatchEvent(event);console.log(document.getElementsByTagName('video')[0].src);");*/

        System.out.println(document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src"));
        if (document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src") == null)
          throw new JSException("Video attribute SRC is null :(, link: " + link);
        streamingLink = url.getHost() + document.getElementsByTagName("video").item(0).getAttributes().getNamedItem("src").getTextContent();
        //streamingLink = finalUrl.getHost() + "/stream/" + document.getElementById("lqEH1").getTextContent();
      } catch (Exception jsShit) {
        failCount++;
        if (failCount == 3) {
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

  @Override
  public void getDirectLink(String unresolvedLink, WebBypassUtility.Callback<String> callback) {
    URL url;
    try {
      url = new URL(unresolvedLink);
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
      String simpleLinkRequested = finalUrl.getProtocol() + "://" + finalUrl.getHost();
      if (newValue == Worker.State.CANCELLED && (engine.getLocation().contains(simpleLinkRequested))) {
        engine.getLoadWorker().cancel();
        pair.getValue().close();
        throw new RuntimeException("The connection to this site caused a problem, maybe this site isn't supported or it has some problem right now." +
                "INFO: exception in the getDirect method, site: " + engine.getLocation());
      }
      if (newValue == Worker.State.SUCCEEDED && (engine.getLocation().contains(simpleLinkRequested))) {
        Document document = engine.getDocument();
        engine.getLoadWorker().cancel();
        pair.getValue().close();
        pair.getKey().getEngine().getLoadWorker().cancel();
        findStreamingLink(document, engine, callback, url, unresolvedLink);
      }
    });
    WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
      System.out.println(message + "[at " + lineNumber + "]");
    });
    engine.load(unresolvedLink);
    engine.getLoadWorker().exceptionProperty().addListener(((observable, oldValue, newValue) -> newValue.printStackTrace()));
    engine.locationProperty().addListener((event, oldValue, newValue) -> engine.getLoadWorker().cancel());
  }

  @Override
  public String[] getCompatibleLinks() {
    return new String[]{"*"};
  }
}
