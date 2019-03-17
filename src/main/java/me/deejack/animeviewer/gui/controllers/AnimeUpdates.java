package me.deejack.animeviewer.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.updates.SingleAnimeUpdate;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class AnimeUpdates extends VBox {
  public AnimeUpdates() {
    checkUpdates();
  }

  public void checkUpdates() {
    Favorite.getInstance().getFavorites().forEach((favorite) -> {
      List<Episode> oldEpisodes = favorite.getEpisodes();
      favorite.loadAsync(() -> {
        List<Episode> newEpisodes = new ArrayList<>(favorite.getEpisodes());
        newEpisodes.removeAll(oldEpisodes);
        newEpisodes.stream()
                .map(episode -> new SingleAnimeUpdate(favorite, episode))
                .forEach(getChildren()::add);
      });
    });
  }
}
