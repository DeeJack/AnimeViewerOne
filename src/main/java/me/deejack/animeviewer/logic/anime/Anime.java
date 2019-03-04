package me.deejack.animeviewer.logic.anime;

import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.favorite.Favorite;

/**
 * The base class for AnimeImpl and Film
 */
public interface Anime {
  void fetchAnimeDetails();

  List<Episode> fetchAnimeEpisodes();
  /**
   * Get the preview, which contains some info about the anime and can connect to the anime (or the film)
   */
  AnimeInformation getAnimeInformation();

  boolean hasLoadedEpisodes();

  /**
   * A method that must be overridden that will load the episodes and the seasons of this anime
   * So it will connect to the page of the anime and get the episodes
   */
  void loadEpisodes();

  List<Season> getSeasons(); // Mah...

  /**
   * A method that will set the infos such as the plot, the genres or anything that hasn't been already set before
   */
  void setInfos();
}
