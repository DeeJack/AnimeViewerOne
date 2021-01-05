package me.deejack.animeviewer.logic.models.source;

import me.deejack.animeviewer.logic.filters.Filter;
import me.deejack.animeviewer.logic.models.anime.Anime;
import org.jsoup.Connection;

import java.util.List;
import java.util.Optional;

/**
 * Contains the methods for connecting to a source
 */
public abstract class HttpSource implements FilteredSource {
  private final String baseUrl;
  private final String iconUrl;
  private int pages = 0;

  public HttpSource(String baseUrl, String iconUrl) {
    this.baseUrl = baseUrl;
    this.iconUrl = iconUrl;
  }

  /**
   * @see me.deejack.animeviewer.logic.models.source.AnimeSource
   */
  @Override
  public List<Anime> fetchPopularAnime(int page) {
    return parseAnimeList(popularAnimeRequest(page));
  }

  @Override
  public List<Anime> searchAnime(String search, int page) {
    return parseAnimeList(searchAnimeRequest(page, search).get());
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  @Override
  public int getPages() {
    return pages;
  }

  @Override
  public List<Anime> filter(Filter[] filters, int page) {
    return parseAnimeList(filterRequest(page, filters).get());
  }

  public abstract Connection.Response popularAnimeRequest(int page);

  public abstract Optional<Connection.Response> searchAnimeRequest(int page, String search);

  public abstract Optional<Connection.Response> filterRequest(int page, Filter[] filters);

  public abstract List<Anime> parseAnimeList(Connection.Response response);

  @Override
  public String toString() {
    return "Title: " + getName() + "\nUrl: " + getBaseUrl();
  }
}
