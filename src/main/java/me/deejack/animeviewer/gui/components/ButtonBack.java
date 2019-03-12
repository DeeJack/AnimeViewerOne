package me.deejack.animeviewer.gui.components;

import javafx.scene.control.Button;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class ButtonBack extends Button {
  public ButtonBack() {
    super("<-");
    setEllipsisString("<-");
    setOnAction((event) -> SceneUtility.goToPreviousScene());
  }
}
