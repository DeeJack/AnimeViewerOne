package me.deejack.animeviewer.logic.models.source;

import java.util.List;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.logic.anime.Anime;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

/**
 * Contains the methods for connecting to a source
 */
public abstract class HttpSource implements FilteredSource {
  private final String baseUrl;
  private final String iconUrl;
  private final FilterList filters = new FilterList();

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

  /**
   * @see me.deejack.animeviewer.logic.models.source.AnimeSource
   */
  @Override
  public Anime fetchAnimeDetails(Anime anime) {
    return parseAnimeDetails(animeDetailsResponse(anime));
  }

  /**
   * @see me.deejack.animeviewer.logic.models.source.AnimeSource
   */
  @Override
  public List<Episode> fetchAnimeEpisodes(Anime anime) {
    return parseEpisodes(episodeListRequest(anime));
  }

  public FilterList getFilters() {
    return filters;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public Connection.Response animeDetailsResponse(Anime anime) {
    return ConnectionUtility.connect(anime.getAnimeInformation().getUrl(), false);
  }

  public abstract Connection.Response popularAnimeRequest(int page);

  public abstract Connection.Response searchAnimeRequest(int page, String search);

  public abstract Connection.Response filterRequest(int page, FilterList filters);

  public abstract List<Anime> parseAnimeList(Connection.Response response);

  public abstract Anime parseAnimeDetails(Connection.Response response);

  public abstract List<Episode> parseEpisodes(Connection.Response response);

  public abstract Connection.Response episodeListRequest(Anime anime);

  public abstract Connection.Response getPagesRequest();
}
