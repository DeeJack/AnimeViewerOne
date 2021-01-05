package me.deejack.animeviewer.logic.connection;

import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import me.deejack.animeviewer.logic.utils.UserAgents;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Optional;

/**
 * A simple implementation of the connection, you can directly use this or set
 * your own implementation using {@link GeneralUtility}
 */
public class SimpleSiteConnection implements SiteConnection {
  @Override
  public Optional<Connection.Response> connect(String url, boolean followRedirects) throws NoConnectionException {
    Connection.Response response;
    try {
      response = Jsoup
              .connect(url)
              .method(Connection.Method.GET)
              .userAgent(UserAgents.WIN10_FIREFOX.getValue())
              .ignoreHttpErrors(true)
              .followRedirects(followRedirects)
              .timeout(60 * 1000)
              .ignoreContentType(true)
        .execute(); // 1
    } catch (IOException e) {
      throw new NoConnectionException(url, e);
    }
    return Optional.of(response);
  }
}
