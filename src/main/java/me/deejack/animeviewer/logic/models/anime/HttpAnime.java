package me.deejack.animeviewer.logic.models.anime;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

import java.util.List;

/**
 * Ora che ho definito cosa può fare un anime, definisco come farlo tramite connessioni, però non conosco
 * come fare il parsing degli episodi o del dettaglio, quindi sono astratti
 */
public abstract class HttpAnime implements Anime {
  @Expose
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
      ConnectionUtility.connect(url, true).ifPresent(response -> animePageResponse = response);
    animePageResponse.bufferUp();
    return animePageResponse;
  }

  protected abstract List<Episode> parseEpisodes(Connection.Response response);

  protected abstract AnimeInformation parseAnimeDetails(Connection.Response response);

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object otherAnime) {
    return otherAnime instanceof Anime && ((Anime) otherAnime).getUrl().equalsIgnoreCase(getUrl());
  }
}
