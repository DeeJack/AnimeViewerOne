package me.deejack.animeviewer.logic.history;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.time.LocalDateTime;

public class HistoryEpisode {
  @Expose
  private final Episode episode;
  @Expose
  private final LocalDateTime viewedDate;

  public HistoryEpisode(Episode episode, LocalDateTime viewedDate) {
    this.episode = episode;
    this.viewedDate = viewedDate;
  }

  public Episode getEpisode() {
    return episode;
  }

  public LocalDateTime getViewedDate() {
    return viewedDate;
  }

  @Override
  public boolean equals(Object otherEpisode) {
    return otherEpisode instanceof HistoryEpisode &&
            ((HistoryEpisode) otherEpisode).getEpisode().equals(episode);
  }
}
