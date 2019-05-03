package me.deejack.animeviewer.logic.defaultsources.animeplanet;

import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.episode.EpisodeImpl;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.List;

public class AnimePlanetEpisode extends EpisodeImpl {

  public AnimePlanetEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  @Override
  protected List<StreamingLink> getStreamingLinks(Document document) {
    return null;
  }
}
