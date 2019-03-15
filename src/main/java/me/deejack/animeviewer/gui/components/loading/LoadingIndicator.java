package me.deejack.animeviewer.gui.components.loading;

import javafx.scene.control.ProgressIndicator;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class LoadingIndicator extends ProgressIndicator {
  public LoadingIndicator() {
    init();
  }

  private void init() {
    setHeight(100);
    setWidth(100);
  }
}
