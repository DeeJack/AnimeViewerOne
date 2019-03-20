package me.deejack.animeviewer.gui.components.general;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class ButtonBack extends Button {
  public ButtonBack() {
    super("<-");
    setTooltip(new Tooltip(LocalizedApp.getInstance().getString("BackTooltip")));
    setEllipsisString("<-");
    setOnAction((event) -> SceneUtility.goToPreviousScene());
  }
}
