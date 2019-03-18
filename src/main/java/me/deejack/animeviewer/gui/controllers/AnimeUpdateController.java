package me.deejack.animeviewer.gui.controllers;

import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.updates.DailyUpdatesBox;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.FavoriteUpdates;

public class AnimeUpdateController implements BaseScene {
  private Pane root;

  public AnimeUpdateController() {
    initialize();
  }

  public void initialize() {
    root = (Pane) SceneUtility.loadParent("/scenes/favorite/favorite.fxml");

    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    loadOldUpdates(boxFavorite);
    checkUpdates(boxFavorite);
  }

  private void loadOldUpdates(VBox boxFavorite) {
    FavoriteUpdates favoriteUpdates = new FavoriteUpdates();
    favoriteUpdates.readFromFile();
    favoriteUpdates.getUpdates().forEach((localDate, animeUpdates) -> {
      boxFavorite.getChildren().add(new DailyUpdatesBox(localDate, animeUpdates));
    });
  }

  public void checkUpdates(VBox boxFavorite) {
    FavoriteUpdates favoriteUpdates = new FavoriteUpdates();
    new Thread(() -> {
      DailyUpdatesBox dailyUpdatesBox = new DailyUpdatesBox(LocalDateTime.now(), favoriteUpdates.checkUpdates());
      Platform.runLater(() -> boxFavorite.getChildren().add(0, dailyUpdatesBox));
    }).start();
/*    Favorite.getInstance().getFavorites().stream()
            .map(FavoriteAnime::getAnime)
            .forEach((favorite) -> {
              List<Episode> oldEpisodes = favorite.getEpisodes();
              favorite.loadAsync(() -> {
                List<Episode> newEpisodes = new ArrayList<>(favorite.getEpisodes());
                newEpisodes.removeAll(oldEpisodes);
                newEpisodes.forEach((episode) -> Platform.runLater(() ->
                        dailyUpdatesBox.addUpdate(favorite, episode)));*/
        /*newEpisodes.forEach(episode ->
                Platform.runLater(() ->
                        boxFavorite.getChildren().add(new SingleAnimeUpdate(favorite, episode))
                ));
      });*/
              /*});
            });*/
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
    return "FavoriteUpdates";
  }
}
