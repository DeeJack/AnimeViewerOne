package me.deejack.animeviewer.logic.models.episode;

import com.google.gson.annotations.Expose;
import java.time.LocalDate;

public abstract class EpisodeImpl extends HttpEpisode {
  @Expose
  private final String title;
  @Expose
  private final int number;
  @Expose
  private final LocalDate releaseDate;
  @Expose
  private double secondsWatched;

  public EpisodeImpl(String title, int number, String url, LocalDate releaseDate) {
    super(url);
    this.title = title;
    this.number = number;
    this.releaseDate = releaseDate;
  }

  public String getTitle() {
    return title;
  }

  public int getNumber() {
    return number;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  @Override
  public double getSecondsWatched() {
    return secondsWatched;
  }

  @Override
  public void setSecondsWatched(double secondsWatched) {
    this.secondsWatched = secondsWatched;
  }
}
