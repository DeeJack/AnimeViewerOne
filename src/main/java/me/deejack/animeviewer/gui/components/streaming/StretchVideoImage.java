package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class StretchVideoImage extends ImageView {
  private final Image stretchVideoFull = new Image(App.class.getResourceAsStream("/assets/stretch-full.png"));
  private final Image stretchVideoSmall = new Image(App.class.getResourceAsStream("/assets/stretch-small.png"));

  public StretchVideoImage(MediaView mediaView, Pane root) {
    setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    setFitHeight(23);
    setFitWidth(31);
    setPickOnBounds(true);
    setOnMouseClicked((event) -> stretchVideo(mediaView, root));
  }

  private void stretchVideo(MediaView mediaView, Pane root) {
    mediaView.setPreserveRatio(!mediaView.isPreserveRatio());
    if(!mediaView.isPreserveRatio())
      root.layoutBoundsProperty().addListener((event, oldValue, newValue) -> onSizeChange(mediaView));
    setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    onSizeChange(mediaView);
  }

  private void onSizeChange(MediaView mediaView) {
    mediaView.setFitHeight(SceneUtility.getStage().getHeight());
    mediaView.setFitWidth(SceneUtility.getStage().getWidth());
  }
}
