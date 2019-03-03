package me.deejack.animeviewer.logic.customexception;

/**
 * A exception thrown when the connection with the server fails
 */
public class NoConnectionException extends RuntimeException {
  private final String link;

  /**
   * Main constructor of this exception
   *
   * @param link  The link which the program has been unable to connect
   * @param cause the throwable who was got when the program tried the connection
   */
  public NoConnectionException(String link, Throwable cause) {
    super(String.format("Unable to establish a connection to %s, possible causes: \n-No Internet\n-Site Offline\nFull exception message: %s", link, cause.getMessage()),
            cause);
    this.link = link;
  }

  public String getLink() {
    return link;
  }
}
