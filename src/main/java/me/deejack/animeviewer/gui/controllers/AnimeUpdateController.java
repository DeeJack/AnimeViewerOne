package me.deejack.animeviewer.gui.controllers;

import java.time.LocalDate;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.updates.DailyUpdatesBox;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.AnimeUpdates;
import me.deejack.animeviewer.logic.favorite.FavoriteUpdates;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class AnimeUpdateController implements BaseScene {
  private final Pane root;
  private DailyUpdatesBox todayBox;

  public AnimeUpdateController() {
    root = (Pane) SceneUtility.loadParent("/scenes/favorite/favorite.fxml");
    initialize();
  }

  public void initialize() {
    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    loadOldUpdates(boxFavorite);
    loadNewUpdates(boxFavorite);
  }

  private void loadOldUpdates(VBox boxFavorite) {
    FavoriteUpdates favoriteUpdates = FavoriteUpdates.getInstance();
    favoriteUpdates.readFromFile();
    favoriteUpdates.getUpdatesByDay().forEach((localDate, animeUpdates) -> {
      DailyUpdatesBox dailyUpdatesBox = new DailyUpdatesBox(localDate, animeUpdates);
      if (localDate.equals(LocalDate.now()))
        todayBox = dailyUpdatesBox;
      boxFavorite.getChildren().add(dailyUpdatesBox);
    });
  }

  /**
   * Check for new updates, then add to the box the new episodes
   *
   * @param boxFavorite The box in which the new episodes will be added
   */
  public void loadNewUpdates(VBox boxFavorite) {
    FavoriteUpdates favoriteUpdates = FavoriteUpdates.getInstance();
    new Thread(() -> {
      List<AnimeUpdates> updates = favoriteUpdates.checkUpdates();
      Platform.runLater(() -> {
        if(todayBox != null)
          boxFavorite.getChildren().remove(todayBox);
        DailyUpdatesBox dailyUpdatesBox = new DailyUpdatesBox(LocalDate.now(), updates); // Usare todayBox, non crearlo nuovo
        boxFavorite.getChildren().add(0, dailyUpdatesBox);
      });
      FilesUtility.saveFavorite();
      favoriteUpdates.writeToFile();
    }).start();
  }

  @Override
  public void onBackFromOtherScene() {
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
