package me.deejack.animeviewer.gui.controllers;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.favorites.FavoriteItem;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class FavoriteController implements BaseScene {
  private final Parent root;
  private final VBox boxFavorite;

  public FavoriteController() {
    root = SceneUtility.loadParent("/scenes/favorite/favorite.fxml");
    boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
  }

  private void reload() {
    boxFavorite.getChildren().clear();
    List<FavoriteItem> items = Favorite.getInstance().getFavorites().stream()
            .map(FavoriteItem::new)
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
    return LocalizedApp.getInstance().getString("FavoriteWindowTitle");
  }

  @Override
  public String getName() {
    return "Favorite";
  }
}
