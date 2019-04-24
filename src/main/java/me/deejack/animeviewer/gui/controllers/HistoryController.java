package me.deejack.animeviewer.gui.controllers;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.favorites.HistoryItem;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class HistoryController implements BaseScene {
  private final Parent root;
  private final VBox boxFavorite;

  public HistoryController() {
    root = SceneUtility.loadParent("/scenes/favorite/favorite.fxml");
    boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    reload();
  }

  private void reload() {
    boxFavorite.getChildren().clear();
    History.getHistory().sort();
    List<HistoryItem> items = History.getHistory().getViewedElements().stream()
            .map(HistoryItem::new)
            .collect(Collectors.toList());
    items.forEach((item) -> item.setOnRemove((event) -> {
      item.getOnRemove().handle(event);
      reload();
    }));
    items.forEach(boxFavorite.getChildren()::add);
  }

  @Override
  public void onBackFromOtherScene() {
    reload();
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
