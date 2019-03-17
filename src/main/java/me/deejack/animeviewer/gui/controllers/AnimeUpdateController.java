package me.deejack.animeviewer.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.updates.SingleAnimeUpdate;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class AnimeUpdateController implements BaseScene {
  private Pane root;

  @FXML
  public void initialize() {
    root = (Pane) SceneUtility.loadParent("/scenes/favorite/favorite.fxml");

    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    /*Favorite.getInstance().getFavorites().stream()
            .map(FavoriteItem::new)
            .forEach(boxFavorite.getChildren()::add);*/
    checkUpdates(boxFavorite);
  }

  public void checkUpdates(VBox boxFavorite) {
    Favorite.getInstance().getFavorites().forEach((favorite) -> {
      List<Episode> oldEpisodes = favorite.getEpisodes();
      favorite.loadAsync(() -> {
        List<Episode> newEpisodes = new ArrayList<>(favorite.getEpisodes());
        newEpisodes.removeAll(oldEpisodes);
        newEpisodes.stream()
                .map(episode -> new SingleAnimeUpdate(favorite, episode))
                .forEach(boxFavorite.getChildren()::add);
      });
    });
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return LocalizedApp.getInstance().getString("AnimeUpdatesWindowTitle");
  }

  @Override
  public String getName() {
    return "AnimeUpdates";
  }
}
