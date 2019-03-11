package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class StretchVideoImage extends ImageView {
  private final Image stretchVideoFull = new Image(App.class.getResourceAsStream("/assets/stretch-full.png"));
  private final Image stretchVideoSmall = new Image(App.class.getResourceAsStream("/assets/stretch-small.png"));

  public StretchVideoImage(MediaView mediaView) {
    setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    setFitHeight(23);
    setFitWidth(31);
    setOnMouseClicked((event) -> stretchVideo(mediaView));
  }

  private void stretchVideo(MediaView mediaView) {
    mediaView.setPreserveRatio(!mediaView.isPreserveRatio());
    setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    mediaView.setFitHeight(SceneUtility.getStage().getHeight());
    mediaView.setFitWidth(SceneUtility.getStage().getWidth());
  }
}
