package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public class FavoriteAnime {
  private static final AtomicInteger counter = new AtomicInteger(0);
  @Expose
  private final int id = counter.addAndGet(1);
  @Expose
  private final Anime anime;
  @Expose
  private final List<Episode> episodes;
  @Expose
  private final String imagePath;
  @Expose
  private boolean imageDownloaded = false;

  public FavoriteAnime(Anime anime) {
    this.anime = anime;
    episodes = anime.getEpisodes();
    imagePath = GeneralUtility.CONFIG_PATH + File.separator + "favoritesImages" + File.separator + id;
  }

  public static void setCounter(int value) {
    counter.set(value);
  }

  public Anime getAnime() {
    return anime;
  }

  public int getId() {
    return id;
  }

  public File getImagePath() {
    return new File(imagePath);
  }

  public boolean isImageDownloaded() {
    return imageDownloaded;
  }

  public void setImageDownloaded(boolean imageDownloaded) {
    this.imageDownloaded = imageDownloaded;
  }
}
