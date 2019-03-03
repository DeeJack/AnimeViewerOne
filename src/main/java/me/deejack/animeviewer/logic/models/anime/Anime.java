package me.deejack.animeviewer.logic.models.anime;

import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;

public interface Anime {
  void fetchAnimeDetails();

  List<Episode> fetchAnimeEpisodes();

  AnimeInformation getAnimeInformation();

  String episodeSelector();
}
