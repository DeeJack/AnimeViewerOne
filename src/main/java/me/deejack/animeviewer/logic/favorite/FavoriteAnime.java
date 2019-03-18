package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.util.concurrent.atomic.AtomicInteger;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class FavoriteAnime {
  private static final AtomicInteger counter = new AtomicInteger(0);
  @Expose
  private final int id = counter.addAndGet(1);
  @Expose
  private final Anime anime;

  public FavoriteAnime(Anime anime) {
    this.anime = anime;
  }

  public Anime getAnime() {
    return anime;
  }

  public int getId() {
    return id;
  }
}
