package me.deejack.animeviewer.logic.defaultsources.animeplanet;

import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.models.anime.AnimeImpl;
import me.deejack.animeviewer.logic.models.episode.Episode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class AnimePlanetAnime extends AnimeImpl {
  public AnimePlanetAnime(String url, AnimeInformation information) {
    super(url, information);
  }

  @Override
  protected AnimeInformation parseAnimeDetails(Document document) {
    return null;
  }

  @Override
  protected String episodeSelector() {
    return null;
  }

  @Override
  protected Episode parseEpisode(Element element) {
    return null;
  }

  @Override
  public void afterEpisodeLoaded(List<Episode> episodes) {

  }
}
