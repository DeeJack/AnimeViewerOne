package me.deejack.animeviewer.logic.anime;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;

/**
 * A class representing an episode
 */
public abstract class Episode {
  /**
   * The title of the episode, put an empty string if there isn't one
   */
  private final String title;
  /**
   * The number of the episode
   */
  @Expose
  private final int number;
  /**
   * The url of the page containing the episode
   */
  @Expose
  private final String url;
  /**
   * The release date of this episode
   */
  private final LocalDate releaseDate;
  @Expose
  private double secondsWatched;

  /**
   * Main constructor for creating an episode
   *
   * @param title       The title of the episode, put an empty string if there isn't one
   * @param number      The number of the episode
   * @param url         The url of the page containing the episode
   * @param releaseDate The release date of this episode
   */
  public Episode(String title, int number, String url, LocalDate releaseDate) {
    this.title = title;
    this.number = number;
    this.url = url;
    this.releaseDate = releaseDate;
  }

  /**
   * Well... It returns the title of the episode?
   *
   * @return the title of the episode
   */
  public String getTitle() {
    return title;
  }

  /**
   * ... Seriously? It returns the number of the episode
   *
   * @return the number of the episode
   */
  public int getNumber() {
    return number;
  }

  /**
   * ...
   *
   * @return the url of the page containing the episode
   */
  public String getUrl() {
    return url;
  }

  /**
   * @return the release date of the episode
   */
  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  /**
   * A method that must be overridden that returns a list containing the streaming links of this episode (es the links to openload, the server source ecc)
   *
   * @return A list containing the streaming links
   * @throws IOException
   */
  public abstract List<StreamingLink> getStreamingLinks() throws IOException;

  /**
   * Download a file to a location passed through the argument given the DIRECT link (es https://www.example.org/direct_file.mp4)
   *
   * @param output       The path where you want to put the downloaded file (es: "C:\Users\Shish\Desktop\file.mp4")
   * @param downloadLink The DIRECT link to the file to download (es https://www.example.org/direct_file.mp4)
   * @throws IOException If the link is wrong or it can't connect for some reasons
   */
  // TODO NoConnectionException
  public void download(File output, String downloadLink) throws IOException {
    URL url = new URL(downloadLink);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.connect();
    try (FileOutputStream stream = new FileOutputStream(output)) {
      try (InputStream inputStream = connection.getInputStream()) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
          stream.write(buffer, 0, length);
        }
      }
    }
  }

  /**
   * The same as the download, but asynchronous
   *
   * @param output       The path where you want to put the downloaded file (es: "C:\Users\Shish\Desktop\file.mp4")
   * @param downloadLink downloadLink The DIRECT link to the file to download (es https://www.example.org/direct_file.mp4)
   * @return an instance of the {@link me.deejack.animeviewer.logic.async.DownloadAsync} class, a subclass of {@link java.lang.Runnable} that let you download in another thread
   */
  public DownloadAsync downloadAsync(File output, String downloadLink) {
    return new DownloadAsync(output, downloadLink);
  }

  public double getSecondsWatched() {
    return secondsWatched;
  }

  public void setSecondsWatched(double secondsWatched) {
    this.secondsWatched = secondsWatched;
  }

  @Override
  public boolean equals(Object otherEpisode) {
    return otherEpisode instanceof Episode && ((Episode) otherEpisode).getUrl().equals(getUrl());
  }

  @Override
  public String toString() {
    return String.format("Title: %s\nNumber: %d\nUrl: %s\nRelease date: %s",
            title, number, url, releaseDate);
  }
}
