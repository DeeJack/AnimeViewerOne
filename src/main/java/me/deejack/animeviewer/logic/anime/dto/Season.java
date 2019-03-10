package me.deejack.animeviewer.logic.anime.dto;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.deejack.animeviewer.logic.models.episode.Episode;

/**
 * A class representing a season, with a list of episode and a name
 */
public class Season {
  /**
   * The list of the episode in this season
   */
  @Expose
  private final Collection<Episode> episodes = new ArrayList<>();
  /**
   * The name of this season
   */
  @Expose
  private final String name;

  public Season(String name, Collection<? extends Episode> episodes) {
    this.name = name;
    addEpisode(episodes);
  }

  public void addEpisode(Episode episode) {
    episodes.add(episode);
  }

  public void addEpisode(Collection<? extends Episode> episodes) {
    this.episodes.addAll(episodes);
  }

  public List<? extends Episode> getEpisodes() {
    return new ArrayList<>(episodes);
  }

  public String getName() {
    return name;
  }
}
