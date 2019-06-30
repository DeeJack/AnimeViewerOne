package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaViewStreaming extends MediaView {
  private final MediaPlayer mediaPlayer;

  public MediaViewStreaming(MediaPlayer mediaPlayer, Pane root) {
    super(mediaPlayer);
    this.mediaPlayer = mediaPlayer;
    root.heightProperty().addListener((event, oldValue, newValue) -> onSizeChange(root.getWidth(), newValue.doubleValue()));
    root.widthProperty().addListener((event, oldValue, newValue) -> onSizeChange(newValue.doubleValue(), root.getWidth()));
  }

  public void onSizeChange(double width, double height) {
    if (mediaPlayer.getMedia().getWidth() > width) {
      setFitWidth(width);
    }
    if (mediaPlayer.getMedia().getHeight() > height) {
      setFitHeight(height);
      return;
    }
    setFitWidth(width);
    setFitHeight(height);
  }
}
