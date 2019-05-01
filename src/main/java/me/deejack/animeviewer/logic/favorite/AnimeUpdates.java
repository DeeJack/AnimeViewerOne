package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class AnimeUpdates {
  @Expose
  private final int favoriteId;
  @Expose
  private List<Episode> episodes = new ArrayList<>(); // mettere solo il link TODO

  public AnimeUpdates(int favoriteId) {
    this.favoriteId = favoriteId;
  }

  public List<Episode> checkUpdates() {
    if(Favorite.getInstance().get(favoriteId) == null)
      throw new IllegalArgumentException("WTF? The favourite id doesn't match with any favorite?!");
    FavoriteAnime anime = Favorite.getInstance().get(favoriteId);
    List<Episode> oldEpisodes = anime.getEpisodes();
    anime.getAnime().load();
    List<Episode> newEpisodes = new ArrayList<>(anime.getEpisodes());
    newEpisodes.removeAll(oldEpisodes);
    episodes = newEpisodes;
    return newEpisodes;
  }

  public Anime getAnime() {
    return Favorite.getInstance().get(favoriteId).getAnime();
  }

  public List<Episode> getEpisodes() {
    return episodes;
  }

  @Override
  public boolean equals(Object otherObject) {
    return otherObject instanceof AnimeUpdates && ((AnimeUpdates)otherObject).favoriteId == favoriteId;
  }
}
