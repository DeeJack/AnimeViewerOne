package me.deejack.animeviewer.gui.components.general;

import javafx.scene.control.Button;
import me.deejack.animeviewer.gui.controllers.FavoriteController;
import me.deejack.animeviewer.gui.utils.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class ButtonFavorite extends Button {
  public ButtonFavorite() {
    super(LocalizedApp.getInstance().getString("FavoriteButtonText"));
    setOnAction((event) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingFavorites"));
      setRoot(new FavoriteController());
      hideWaitLoad();
    });
  }
}