package me.deejack.animeviewer.logic.models.source.asd;

import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeImpl;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.Status;

public class DreamsubAnime extends AnimeImpl {

  public DreamsubAnime(String name, String url, boolean isFilm, String imageUrl) {
    super(new AnimeInformation((short) -1, name, -1, -1, url, new ArrayList<>(), isFilm, imageUrl, Status.UNKNOWN));
  }

  @Override
  public void fetchAnimeDetails() {

  }

  @Override
  public List<Episode> fetchAnimeEpisodes() {
    return null;
  }

  @Override
  public void loadEpisodes() {

  }

  @Override
  public void setInfos() {

  }
}
