package me.deejack.animeviewer.logic.anime.dto;

/**
 * A class who represents a link for the streaming
 */
public class StreamingLink {
  /**
   * The lang of the video
   */
  private final String lang;
  /**
   * The link to the page of the video
   */
  private final String link;
  /**
   * The resolution of the video
   */
  private final int resolution;
  /**
   * The host of the video, like "Openload" or "Streamango"
   */
  private final String source;

  public StreamingLink(String lang, String link, int resolution, String source) {
    this.lang = lang;
    this.link = link;
    this.resolution = resolution;
    this.source = source;
  }

  /**
   * GETTERS
   */

  public String getLang() {
    return lang;
  }

  public String getLink() {
    return link;
  }

  public int getResolution() {
    return resolution;
  }

  public String getSource() {
    return source;
  }

  @Override
  public String toString() {
    return String.format("Lang: %s, Host: %s, Resolution: %s", lang, source, resolution);
  }
}
