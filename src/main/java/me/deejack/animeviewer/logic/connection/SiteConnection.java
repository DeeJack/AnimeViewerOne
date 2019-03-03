package me.deejack.animeviewer.logic.connection;

import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import org.jsoup.Connection;

/**
 * A functional interface (callable with the lambdas) with a method who is called when the program needs to be connected
 * to a source
 */
@FunctionalInterface
public interface SiteConnection {
  /**
   * The method called when the program needs to connect to a source
   *
   * @param url             The url of the source
   * @param followRedirects If the connection need to follow the redirects
   * @return The response of the connection
   * @throws NoConnectionException If it can't connect to the source for some reasons
   */
  Connection.Response connect(String url, boolean followRedirects) throws NoConnectionException;
}
