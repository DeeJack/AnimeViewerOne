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

  /**
   * Toggle the favorite, if it was one, it removes it, otherwise it adds the element to the favorite list
   *
   * @return true if the element has been added to the favorite list, false if it has been removed
   */
  default boolean toggleFavorite() {
    if (isFavorite()) {
      Favorite.getInstance().removeFavorite(this);
      return true;
    }
    Favorite.getInstance().addFavorite(this);
    return false;
  }

  /**
   * Get if it's in the favorite list
   *
   * @return true if is in the favorite list, false otherwise
   */
  default boolean isFavorite() {
    return Favorite.getInstance().getFavorites().contains(this);
  }
}
