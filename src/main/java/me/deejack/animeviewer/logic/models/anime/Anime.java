package me.deejack.animeviewer.logic.models.anime;

import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.episode.Episode;

/**
 * Di un anime puoi prendere i dettagli, gli episodi oppure gli anime correlati ma per ora lasciamo perdere
 */
public interface  Anime {
  AnimeInformation getAnimeInformation();

  List<Episode> getEpisodes();

  void loadAsync(SuccessListener callback);

  void load();

  boolean hasBeenLoaded();

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
