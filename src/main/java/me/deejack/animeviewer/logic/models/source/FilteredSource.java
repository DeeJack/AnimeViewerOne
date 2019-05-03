package me.deejack.animeviewer.logic.models.source;

import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.List;

/**
 * Contains the methods to filter a source
 */
public interface FilteredSource extends AnimeSource {
  /**
   * Get the popular anime
   *
   * @param page the page that needs to be loaded
   * @return the list of the anime
   */
  List<Anime> fetchPopularAnime(int page);

  /**
   * Search for a given string in the source loading a given page
   *
   * @param search The string that needs to be searched
   * @param page   the page that needs to be loaded
   * @return the list of the results anime
   */
  List<Anime> searchAnime(String search, int page);

  /**
   * Get a list of anime giving some filters
   *
   * @param filters The filters that needs to be applied
   * @param page    The page that needs to be loaded
   * @return the list of the resulting anime
   */
  List<Anime> filter(HiddenSidebarBuilder filters, int page);

  /**
   * Get the number of pages
   *
   * @return the number of pages
   */
  int getPages();

  Filter[] getFilters();
}
