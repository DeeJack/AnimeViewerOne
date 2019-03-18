package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class AnimeUpdates {
  @Expose
  private final int favoriteId; // rimpiazzare con un id del favorite, così da non ripeterlo
  @Expose
  private List<Episode> episodes = new ArrayList<>(); // mettere solo il link

  public AnimeUpdates(int favoriteId) {
    this.favoriteId = favoriteId;
  }

  public List<Episode> checkUpdates() {
    Anime anime = Favorite.getInstance().get(favoriteId).getAnime();
    List<Episode> oldEpisodes = anime.getEpisodes();
    anime.load();
    List<Episode> newEpisodes = new ArrayList<>(anime.getEpisodes());
    newEpisodes.removeAll(oldEpisodes);
    System.out.println(newEpisodes);
    episodes = newEpisodes;
    return newEpisodes;
  }

  public Anime getAnime() {
    return Favorite.getInstance().get(favoriteId).getAnime();
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }
}