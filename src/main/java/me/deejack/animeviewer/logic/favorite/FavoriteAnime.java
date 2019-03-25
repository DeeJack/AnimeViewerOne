package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class FavoriteAnime {
  private static final AtomicInteger counter = new AtomicInteger(0);
  @Expose
  private final int id = counter.addAndGet(1);
  @Expose
  private final Anime anime;
  @Expose
  private final List<Episode> episodes;

  public FavoriteAnime(Anime anime) {
    this.anime = anime;
    episodes = anime.getEpisodes();
  }

  public Anime getAnime() {
    return anime;
  }

  public int getId() {
    return id;
  }

  public static void setCounter(int value) {
    counter.set(value);
  }
}
