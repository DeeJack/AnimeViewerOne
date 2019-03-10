package me.deejack.animeviewer.logic.models.episode;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;

public interface Episode {
  void download(File output, String downloadLink);

  List<StreamingLink> getStreamingLinks();

  String getTitle();

  int getNumber();

  String getUrl();

  LocalDate getReleaseDate();

  void setSecondsWatched(double secondsWatched);

  double getSecondsWatched();
}
