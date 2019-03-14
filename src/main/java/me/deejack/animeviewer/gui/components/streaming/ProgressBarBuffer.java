package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.control.ProgressBar;
import javafx.scene.media.MediaPlayer;

public class ProgressBarBuffer extends ProgressBar {
  private final MediaPlayer mediaPlayer;

  public ProgressBarBuffer(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    setMaxWidth(Double.MAX_VALUE);
    setPrefWidth(864);
    setOnChange();
  }

  public void setOnChange() {
    mediaPlayer.bufferProgressTimeProperty().addListener((event, oldValue, newValue) -> {
      if(mediaPlayer.getBufferProgressTime() == null || mediaPlayer.getTotalDuration() == null)
        return;
      double bufferedProgressPercent = (mediaPlayer.getBufferProgressTime().toSeconds() * 100) / mediaPlayer.getTotalDuration().toSeconds();
      setProgress(bufferedProgressPercent / 100);
    });
  }
}
