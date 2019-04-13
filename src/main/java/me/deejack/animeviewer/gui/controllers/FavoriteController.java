package me.deejack.animeviewer.gui.controllers;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.favorites.FavoriteItem;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class FavoriteController implements BaseScene {
  private Parent root;

  public FavoriteController() {
    initialize();
  }

  private void initialize() {
    root = SceneUtility.loadParent("/scenes/favorite/favorite.fxml");

    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    Favorite.getInstance().getFavorites().stream()
            .map(FavoriteItem::new)
            .forEach(boxFavorite.getChildren()::add);
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
