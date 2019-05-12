package me.deejack.animeviewer.gui.controllers;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.updates.DailyUpdatesBox;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.animeupdates.AnimeUpdates;
import me.deejack.animeviewer.logic.animeupdates.FavoritesUpdates;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import java.time.LocalDate;

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
    FavoritesUpdates favoriteUpdates = FavoritesUpdates.getInstance();
    favoriteUpdates.readFromFile();
    favoriteUpdates.getUpdatesByDay().forEach((localDate, listAnimeUpdates) -> {
      DailyUpdatesBox dailyUpdatesBox = new DailyUpdatesBox(localDate);
      if (localDate.equals(LocalDate.now()))
        todayBox = dailyUpdatesBox;
      listAnimeUpdates.forEach((dailyUpdatesBox::addAnimeUpdates));
      boxFavorite.getChildren().add(dailyUpdatesBox);
    });
  }

  /**
   * Check for new animeupdates, then add to the box the new episodes
   *
   * @param boxFavorite The box in which the new episodes will be added
   */
  public void loadNewUpdates(VBox boxFavorite) {
    FavoritesUpdates favoriteUpdates = FavoritesUpdates.getInstance();
    DailyUpdatesBox dailyUpdatesBox = todayBox != null ? todayBox : new DailyUpdatesBox(LocalDate.now());
    if (todayBox == null)
      boxFavorite.getChildren().add(0, dailyUpdatesBox);
    new Thread(() ->
            favoriteUpdates.checkUpdates(onNewUpdate(boxFavorite, favoriteUpdates, dailyUpdatesBox)))
            .start();
  }

  private Listener<? super AnimeUpdates> onNewUpdate(VBox boxFavorite, FavoritesUpdates favoriteUpdates, DailyUpdatesBox dailyUpdatesBox) {
    return (animeUpdates -> {
      System.out.println("NEW UPDATE!!! " + animeUpdates.getAnime().getAnimeInformation().getName());
      Platform.runLater(() -> dailyUpdatesBox.addAnimeUpdates(animeUpdates));
      FilesUtility.saveFavorite();
    });
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
    return "FavoritesUpdates";
  }
}
