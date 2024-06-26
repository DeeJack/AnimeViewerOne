package me.deejack.animeviewer.logic.animeupdates;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.favorite.FavoriteAnime;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks the new episodes from a favorite anime
 */
public class AnimeUpdates {
  @Expose
  private final int favoriteId;
  @Expose
  private List<Episode> episodes = new ArrayList<>();

  public AnimeUpdates(int favoriteId) {
    this.favoriteId = favoriteId;
  }

  public List<Episode> checkUpdates() {
    FavoriteAnime favoriteAnime = Favorite.getInstance().get(favoriteId)
            .orElseThrow(() -> new IllegalArgumentException("WTF? The favourite id doesn't match with any favorite?!"));
    List<Episode> oldEpisodes = favoriteAnime.getEpisodes();
    favoriteAnime.getAnime().load();
    List<Episode> newEpisodes = new ArrayList<>(favoriteAnime.getAnime().getEpisodes());
    System.err.println(newEpisodes.size());
    newEpisodes.removeAll(oldEpisodes);
    episodes = newEpisodes;
    return newEpisodes;
  }

  public Anime getAnime() {
    return Favorite.getInstance().get(favoriteId)
            .orElseThrow(() -> new IllegalArgumentException("WTF? The favourite id doesn't match with any favorite?!"))
            .getAnime();
  }

  public List<Episode> getEpisodes() {
    return Collections.unmodifiableList(episodes);
  }

  @Override
  public boolean equals(Object otherObject) {
    return otherObject instanceof AnimeUpdates &&
            ((AnimeUpdates) otherObject).favoriteId == favoriteId;
  }
}
