package me.deejack.animeviewer.logic.models.anime;

import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.models.episode.Episode;
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

  protected AnimeInformation fetchAnimeDetails() {
    return parseAnimeDetails(animePageRequest());
  }

  protected List<Episode> fetchAnimeEpisodes() {
    return parseEpisodes(animePageRequest());
  }

  protected Connection.Response animePageRequest() {
    if (animePageResponse == null || animePageResponse.statusCode() != 200)
      animePageResponse = ConnectionUtility.connect(url, false);
    animePageResponse.bufferUp();
    return animePageResponse;
  }

  protected abstract List<Episode> parseEpisodes(Connection.Response response);

  protected abstract AnimeInformation parseAnimeDetails(Connection.Response response);
}
