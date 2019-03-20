package me.deejack.animeviewer.gui.components.general;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import me.deejack.animeviewer.gui.controllers.AnimeUpdateController;
import me.deejack.animeviewer.gui.controllers.HistoryController;
import me.deejack.animeviewer.gui.utils.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class ButtonUpdates extends Button {
  public ButtonUpdates() {
    super(LocalizedApp.getInstance().getString("AnimeUpdatesButton"));
    setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenAnimeUpdates")));
    setOnAction((event) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingAnimeUpdates"));
      setRoot(new AnimeUpdateController());
      hideWaitLoad();
    });
  }
}
