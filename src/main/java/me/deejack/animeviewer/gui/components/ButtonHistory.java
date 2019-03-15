package me.deejack.animeviewer.gui.components;

import javafx.scene.control.Button;
import me.deejack.animeviewer.gui.controllers.FavoriteController;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class ButtonHistory extends Button {
  public ButtonHistory() {
    super("Cronologia");
    setOnAction((event) -> {
      showWaitAndLoad("Caricando cronologia, ricorda di cancellare gli hentai...");
      setRoot(new FavoriteController(true));
      hideWaitLoad();
    });
  }
}
