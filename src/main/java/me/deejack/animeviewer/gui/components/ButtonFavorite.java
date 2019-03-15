package me.deejack.animeviewer.gui.components;

import javafx.scene.control.Button;
import me.deejack.animeviewer.gui.controllers.FavoriteController;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class ButtonFavorite extends Button {
  public ButtonFavorite() {
    super("Preferiti");
    setOnAction((event) -> {
      showWaitAndLoad("Caricando i preferiti");
      setRoot(new FavoriteController(false));
      hideWaitLoad();
    });
  }
}
