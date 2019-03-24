package me.deejack.animeviewer.gui.components.general;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import me.deejack.animeviewer.gui.controllers.HistoryController;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class ButtonHistory extends Button {
  public ButtonHistory() {
    super(LocalizedApp.getInstance().getString("HistoryButtonText"));
    setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenHistory")));
    setOnAction((event) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingHistory"));
      setRoot(new HistoryController());
      hideWaitLoad();
    });
  }
}
