package me.deejack.animeviewer.logic.connection;

import java.util.HashMap;
import java.util.Map;

/**
 * A class who represent a Session, which contains cookies, headers and the link of a source
 */
public class Session {
  /**
   * The cookies present in this session
   */
  private Map<String, String> cookies;
  /**
   * The headers needed for the connection
   */
  private Map<String, String> headers;
  /**
   * The link to the source
   */
  private String link;

  /**
   * The constructor to create a Session
   *
   * @param cookies The cookies present in this session
   * @param headers The headers needed for the connection
   * @param link    The link to the source
   */
  public Session(Map<String, String> cookies, Map<String, String> headers, String link) {
    this.cookies = cookies;
    this.headers = headers;
    this.link = link;
  }

  /**
   * GETTERS AND SETTERS
   */

  public Map<String, String> getCookies() {
    return cookies;
  }

  public void setCookies(HashMap<String, String> cookies) {
    this.cookies = cookies;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }
}
