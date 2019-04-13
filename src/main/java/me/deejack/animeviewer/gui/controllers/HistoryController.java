package me.deejack.animeviewer.gui.controllers;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.favorites.HistoryItem;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class HistoryController implements BaseScene {
  private Parent root;

  public HistoryController() {
    initialize();
  }

  private void initialize() {
    root = SceneUtility.loadParent("/scenes/favorite/favorite.fxml");

    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    History.getHistory().getViewedElements().stream()
            .map(HistoryItem::new)
            .forEach(boxFavorite.getChildren()::add);
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return LocalizedApp.getInstance().getString("HistoryWindowTitle");
  }

  @Override
  public String getName() {
    return "History";
  }
}
