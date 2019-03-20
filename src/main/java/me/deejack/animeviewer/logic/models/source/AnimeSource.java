package me.deejack.animeviewer.logic.models.source;

/**
 * methods to get more information of an anime
 */
public interface AnimeSource {
  /**
   * Get the information of an anime
   *
   * @param anime the anime that needs the details
   * @return the updated anime
   */
  /*Anime fetchAnimeDetails(Anime anime);*/

  /**
   * Get the episodes of an anime
   *
   * @param anime
   * @return the list of the episode
   */
  /*List<Episode> fetchAnimeEpisodes(Anime anime);*/

  /**
   * Get the name of this source
   *
   * @return the name of the source
   */
  String getName();

  String getIconUrl();

  String getBaseUrl();
}
