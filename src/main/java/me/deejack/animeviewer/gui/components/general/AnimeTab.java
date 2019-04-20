package me.deejack.animeviewer.gui.components.general;

import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.scenes.BaseScene;

public class AnimeTab extends HBox implements BaseScene {
  private final Tab root;

  public AnimeTab(Tab root) {
    this.root = root;
  }

  @Override
  public Parent getRoot() {
    return this;
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public String getName() {
    return "Tab";
  }

  @Override
  public void onBackFromOtherScene() {
  }
}
