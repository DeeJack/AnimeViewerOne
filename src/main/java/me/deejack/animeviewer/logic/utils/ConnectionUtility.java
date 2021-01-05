package me.deejack.animeviewer.logic.utils;

import me.deejack.animeviewer.logic.connection.SimpleSiteConnection;
import me.deejack.animeviewer.logic.connection.SiteConnection;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;

/**
 * A utility class for the connection
 */
public final class ConnectionUtility {
  /**
   * The implementation of {@link SiteConnection} to be used for the connections
   * At first is {@link SimpleSiteConnection}
   */
  private static SiteConnection siteConnection = new SimpleSiteConnection();

  private ConnectionUtility() {
  }

  public static void setSiteConnection(SiteConnection siteConnection) {
    ConnectionUtility.siteConnection = siteConnection;
  }

  /**
   * Get the response of the connection to the source
   *
   * @param link            The link to the source
   * @param followRedirects if the connection should follow the eventual redirects
   * @return The response of the connection
   * @throws NoConnectionException if the connection isn't possible for some reasons
   */
  public static Optional<Connection.Response> connect(String link, boolean followRedirects) throws NoConnectionException {
    return siteConnection.connect(link, followRedirects);
  }

  /**
   * Get directly the Document of the connectin
   *
   * @param link            The link to the source
   * @param followRedirects if the connection should follow the eventual redirects
   * @return The document of the source
   * @throws NoConnectionException if the connection isn't possible for some reasons
   */
  public static Document getPage(String link, boolean followRedirects) throws NoConnectionException {
    try {
      var response = connect(link, followRedirects);
      if (response.isEmpty())
        throw new NoConnectionException(link, new RuntimeException("Response empty"));
      return response.get().parse();
    } catch (IOException exc) {
      throw new NoConnectionException(link, exc);
    }
  }

  /**
   * Get the document giving the Response of a connection
   *
   * @param response The response of a previous connection
   * @return The document of the response
   * @throws NoConnectionException on error
   */
  public static Document getPage(Connection.Response response) throws NoConnectionException {
    try {
      return response.parse();
    } catch (IOException exc) {
      throw new NoConnectionException(response.url().getHost(), exc);
    }
  }

}
