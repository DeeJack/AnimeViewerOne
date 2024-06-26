package me.deejack.animeviewer.gui.connection;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.connection.SiteConnection;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.utils.UserAgents;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class CustomConnection implements SiteConnection {
  private static final String SERVICE_UNAVAILABLE_MSG = "Service Temporarily Unavailable";
  private CountDownLatch lock = new CountDownLatch(1);

  @Override
  public Optional<Connection.Response> connect(String pageLink, boolean followRedirects) throws NoConnectionException {
    System.out.println("Connecting " + pageLink);
    URL pageUrl = verifyURL(pageLink);
    if (pageUrl == null)
      return Optional.empty();

    AtomicReference<List<HttpCookie>> httpCookies = new AtomicReference<>();
    Map<String, String> cookies = new HashMap<>();
    lock = new CountDownLatch(1);
    // CookieManager cookieManager = registerCookieManager();

    /*if (App.getSite() != null && App.getSite().getSession() != null)
      cookies.putAll(App.getSite().getSession().getCookies());*/
    if (!pageLink.contains(".jpg") && !pageLink.contains(".png"))
      System.out.println("Connecting to " + pageLink);
    try {
      Connection.Response response = execute(pageLink, cookies, followRedirects);
      System.out.println("Connected to " + response.url().toExternalForm() + " with code " + response.statusCode() + "(" + response.statusMessage() + ")");
      if (response.statusMessage().equalsIgnoreCase(SERVICE_UNAVAILABLE_MSG)) {
        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, LocalizedApp.getInstance().getString("AlertHttp503"),
                ButtonType.OK).show());
      }
      if (response.statusCode() == 503) {
        WebBypassUtility.bypassCloudflare(pageLink, (result) -> {
          /*cookies.putAll(cookiesToMap(result));
          saveToSession(pageLink, cookies);*/
          lock.countDown();
        });
      } else {
        /*cookies.putAll(cookiesToMap(cookieManager.getCookieStore().getCookies()));
        saveToSession(pageUrl.getHost(), cookies);*/
        return Optional.of(response);
      }
      boolean success = lock.await(25, TimeUnit.SECONDS);
      if (!success) {
        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, LocalizedApp.getInstance().getString("AlertTimeout"), ButtonType.OK).show());
        hideWaitLoad();
        return Optional.empty();
      }
      return Optional.of(getResponse(pageLink, followRedirects, cookies));
    } catch (IOException | InterruptedException ex) {
      if (!pageLink.contains(".jpg") && !pageLink.contains(".png"))
        handleException(new NoConnectionException(pageLink, ex));
      return Optional.empty();
    }
  }

  private Connection.Response getResponse(String pageLink, boolean followRedirects, Map<String, String> cookies) throws IOException, InterruptedException {
    Connection.Response connection;
    int countConnections = 0;
    do {
      connection = execute(pageLink, cookies, followRedirects);
      Thread.sleep(100);
      countConnections++;
    } while (connection.statusCode() != 200 && countConnections < 3);
    return connection;
  }

  private void saveToSession(String pageLink, Map<String, String> cookies) {
    /*if (App.getSite() != null)
      App.getSite().setSession(session);*/
  }

  private Connection.Response execute(String url, Map<String, String> cookies, boolean followRedirects) throws IOException {
    return execute(url, cookies, followRedirects, "");
  }

  private Connection.Response execute(String url, Map<String, String> cookies, boolean followRedirects, String referer) throws IOException {
    var baseUrl = "https://" + new URL(url).getHost().replaceAll("cdn2.", "") + "/";

    return Jsoup.connect(url)
            .method(Connection.Method.GET)
            .followRedirects(followRedirects)
            .userAgent(UserAgents.WIN10_FIREFOX.getValue())
            .ignoreHttpErrors(true)
            .ignoreContentType(true)
            .cookies(cookies)
            .timeout(30 * 1000)
            .header("Referer", baseUrl)
            .execute();
  }

  private URL verifyURL(String pageLink) {
    try {
      return new URL(pageLink);
    } catch (IOException exception) {
      handleException(new Exception(LocalizedApp.getInstance().getString("ExceptionInvalidUrl") + ": " + pageLink));
      return null;
    }
  }

  private Map<String, String> cookiesToMap(List<HttpCookie> cookies) {
    Map<String, String> cookiesMap = new HashMap<>();
    for (HttpCookie httpCookie : cookies)
      cookiesMap.put(httpCookie.getName(), httpCookie.getValue());
    return cookiesMap;
  }
}
