package me.deejack.animeviewer.logic.models.source.asd;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;

public class DreamsubEpisode extends Episode {

  public DreamsubEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  @Override
  public List<StreamingLink> getStreamingLinks() throws IOException {
    return null;
  }
}
