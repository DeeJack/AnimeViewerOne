package me.deejack.animeviewer.gui.controllers.streaming.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class FullScreenImage extends ImageView {
  private final Image fullScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-full.png"));
  private final Image smallScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-small.png"));

  public FullScreenImage() {
    setImage(SceneUtility.getStage().isFullScreen() ? smallScreenImage : fullScreenImage);
    setFitHeight(24);
    setFitWidth(23);
    setOnMouseClicked((event) -> {
      SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
      setImage(SceneUtility.getStage().isFullScreen() ? smallScreenImage : fullScreenImage);
    });
  }
}
