package me.deejack.animeviewer.gui.controllers;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.favorites.FavoriteItem;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.favorite.FavoriteAnime;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class FavoriteController implements BaseScene {
  private StackPane root;

  public FavoriteController() {
    initialize();
  }

  public void initialize() {
    root = (StackPane) SceneUtility.loadParent("/scenes/favorite/favorite.fxml");

    VBox boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    Favorite.getInstance().getFavorites().stream()
            .map(FavoriteAnime::getAnime)
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
