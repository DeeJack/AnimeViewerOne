package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaViewStreaming extends MediaView {
  private final MediaPlayer mediaPlayer;
  private final Pane root;

  public MediaViewStreaming(MediaPlayer mediaPlayer, Pane root) {
    super(mediaPlayer);
    this.mediaPlayer = mediaPlayer;
    this.root = root;
    root.heightProperty().addListener((event, oldValue, newValue) -> onSizeChange());
    root.widthProperty().addListener((event, oldValue, newValue) -> onSizeChange());
  }

  public void onSizeChange() {
    if (mediaPlayer.getMedia().getWidth() > root.getWidth()) {
      setFitWidth(root.getWidth());
    }
    if (mediaPlayer.getMedia().getHeight() > root.getHeight()) {
      setFitHeight(root.getHeight());
      return;
    }
    setFitWidth(root.getWidth());
    setFitHeight(root.getHeight());
  }
}
