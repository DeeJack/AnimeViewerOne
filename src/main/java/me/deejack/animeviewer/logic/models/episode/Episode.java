package me.deejack.animeviewer.logic.models.episode;

import me.deejack.animeviewer.logic.anime.dto.StreamingLink;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface Episode {
  void download(File output, String downloadLink);

  List<StreamingLink> getStreamingLinks();

  String getTitle();

  int getNumber();

  String getUrl();

  LocalDate getReleaseDate();

  double getSecondsWatched();

  void setSecondsWatched(double secondsWatched);
}
