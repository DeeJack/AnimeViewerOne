package me.deejack.animeviewer.gui.components.loading;

import javafx.scene.control.ProgressIndicator;

public class LoadingIndicator extends ProgressIndicator {
  public LoadingIndicator() {
    init();
  }

  private void init() {
    setHeight(100);
    setWidth(100);
  }
}
