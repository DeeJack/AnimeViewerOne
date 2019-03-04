package me.deejack.animeviewer.logic.models.anime;

import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

/**
 * Ora che ho definito cosa può fare un anime, definisco come farlo tramite connessioni, però non conosco
 * come fare il parsing degli episodi o del dettaglio, quindi sono astratti
 */
public abstract class HttpAnime implements Anime {
  private final String url;
  private Connection.Response animePageResponse;

  public HttpAnime(String url) {
    this.url = url;
  }

  @Override
  public AnimeInformation fetchAnimeDetails() {
    return parseAnimeDetails(animePageRequest());
  }

  @Override
  public List<Episode> fetchAnimeEpisodes() {
    return parseEpisodes(animePageRequest());
  }

  public Connection.Response animePageRequest() {
    if (animePageResponse == null || animePageResponse.statusCode() != 200)
      animePageResponse = ConnectionUtility.connect(url, false);
    return animePageResponse;
  }

  public abstract List<Episode> parseEpisodes(Connection.Response response);

  public abstract AnimeInformation parseAnimeDetails(Connection.Response response);
}
