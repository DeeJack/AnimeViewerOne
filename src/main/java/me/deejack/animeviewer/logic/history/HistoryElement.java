package me.deejack.animeviewer.logic.history;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.deejack.animeviewer.logic.anime.Anime;
import me.deejack.animeviewer.logic.anime.Episode;

public class HistoryElement {
  @Expose
  private final List<Episode> episodesHistory;
  @Expose
  private Anime viewedElement;

  public HistoryElement(Anime viewedElement, List<Episode> episodesHistory) {
    this.viewedElement = viewedElement;
    this.episodesHistory = episodesHistory;
  }

  public HistoryElement(Anime viewedElement, Episode episode) {
    this(viewedElement, new ArrayList<>());
    episodesHistory.add(episode);
  }

  public void addEpisode(Episode episode) {
    episodesHistory.remove(episode);
    episodesHistory.add(episode);
  }

  public Anime getViewedElement() {
    return viewedElement;
  }

  public void setViewedElement(Anime viewedElement) {
    this.viewedElement = viewedElement;
  }

  public List<Episode> getEpisodesHistory() {
    return Collections.unmodifiableList(episodesHistory);
  }

  public void removeEpisodeFromHistory(int index) {
    episodesHistory.remove(index);
  }

  @Override
  public boolean equals(Object otherElement) {
    return otherElement instanceof HistoryElement &&
            ((HistoryElement) otherElement).viewedElement == viewedElement;
  }
}
