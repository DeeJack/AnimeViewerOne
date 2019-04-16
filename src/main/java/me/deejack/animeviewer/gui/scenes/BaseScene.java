package me.deejack.animeviewer.gui.scenes;

import javafx.scene.Parent;

public interface BaseScene {
  Parent getRoot();

  String getTitle();

  String getName();

  void onBackFromOtherScene();
}
