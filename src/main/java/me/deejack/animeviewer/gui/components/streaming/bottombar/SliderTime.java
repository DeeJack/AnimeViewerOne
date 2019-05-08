package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility;

public class SliderTime extends Slider {
  private static final double MIN_CHANGE = 0.5;
  private final MediaPlayer mediaPlayer;

  public SliderTime(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    setPrefWidth(864);
    mediaPlayer.totalDurationProperty().addListener((event, oldValue, newValue) -> setMax(newValue.toSeconds() + mediaPlayer.getStartTime().toSeconds()));
    setOnChange();
    setOnKeyPressed();
  }

  private void setOnChange() {
    mediaPlayer.currentTimeProperty().addListener((event, oldValue, newValue) -> {
      if (!isValueChanging())
        setValue(mediaPlayer.getCurrentTime().toSeconds());
    });
    valueChangingProperty().addListener((event, oldValue, newValue) -> {
      if (!newValue)
        mediaPlayer.seek(Duration.seconds(getValue()));
    });
    valueProperty().addListener((event, oldValue, newValue) -> {
      double seconds = mediaPlayer.getCurrentTime().toSeconds();
      if (Math.abs(seconds - newValue.doubleValue()) > MIN_CHANGE)
        mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
    });
  }

  private void setOnKeyPressed() {
    setOnKeyPressed((event) -> StreamingUtility.keyNavigation(event, mediaPlayer));
  }
}
