package me.deejack.animeviewer.logic.models.episode;

import java.time.LocalDate;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;

public abstract class EpisodeImpl extends HttpEpisode {
  private final String title;
  private final int number;
  private final LocalDate releaseDate;
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
