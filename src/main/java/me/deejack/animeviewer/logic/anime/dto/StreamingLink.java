package me.deejack.animeviewer.logic.anime.dto;

/**
 * A class who represents a link for the createStreaming
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
  private final boolean allowEmbeddedVideo;

  public StreamingLink(String lang, String link, int resolution, String source, boolean allowEmbeddedVideo) {
    this.lang = lang;
    this.link = link;
    this.resolution = resolution;
    this.source = source;
    this.allowEmbeddedVideo = allowEmbeddedVideo;
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

  public boolean allowsEmbeddedVideo() {
    return allowEmbeddedVideo;
  }

  @Override
  public String toString() {
    return String.format("Lang: %s, Host: %s, Resolution: %s", lang, source, resolution);
  }
}
