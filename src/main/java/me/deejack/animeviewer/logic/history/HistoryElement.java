package me.deejack.animeviewer.logic.history;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryElement {
  @Expose
  private final List<HistoryEpisode> episodesHistory;
  @Expose
  private Anime viewedElement;

  public HistoryElement(Anime viewedElement, List<HistoryEpisode> episodesHistory) {
    this.viewedElement = viewedElement;
    this.episodesHistory = episodesHistory;
  }

  public HistoryElement(Anime viewedElement, HistoryEpisode episode) {
    this(viewedElement, new ArrayList<>());
    episodesHistory.add(episode);
  }

  public void addEpisode(HistoryEpisode episode) {
    episodesHistory.remove(episode);
    episodesHistory.add(episode);
  }

  public Anime getViewedElement() {
    return viewedElement;
  }

  public void setViewedElement(Anime viewedElement) {
    this.viewedElement = viewedElement;
  }

  public List<HistoryEpisode> getEpisodesHistory() {
    return Collections.unmodifiableList(episodesHistory);
  }

  public void removeEpisodeFromHistory(int index) {
    episodesHistory.remove(index);
  }

  public boolean contains(Episode episode) {
    return episodesHistory.contains(new HistoryEpisode(episode, null));
  }

  @Override
  public boolean equals(Object otherElement) {
    return otherElement instanceof HistoryElement &&
            ((HistoryElement) otherElement).viewedElement == viewedElement;
  }
}
