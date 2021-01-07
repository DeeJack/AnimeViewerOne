package me.deejack.animeviewer.gui.scenes;

import javafx.scene.Parent;

public interface BaseScene {
  /**
   * Get the root of the scene
   *
   * @return the root of this scene
   */
  Parent getRoot();

  /**
   * Get the title for the window or the tab if is in a new tab
   *
   * @return the title of this scene
   */
  String getTitle();

  /**
   * Get the name of the scene, not affect the window
   *
   * @return the name of this scene
   */
  String getName();

  /**
   * Called when the user go back to this page from another page
   */
  void onBackFromOtherScene();
}
