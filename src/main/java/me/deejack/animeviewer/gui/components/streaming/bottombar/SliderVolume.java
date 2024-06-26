package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility;

public class SliderVolume extends Slider {
  private final MediaPlayer mediaPlayer;

  public SliderVolume(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    setMax(0.5);
    setValue(0.5);
    setBlockIncrement(0.01);

    setOnChange();
    setOnKeyPressed();
  }

  private void setOnChange() {
    mediaPlayer.volumeProperty().addListener((event) -> setValue(mediaPlayer.getVolume()));
    valueProperty().addListener((listener) -> mediaPlayer.setVolume(getValue()));
  }

  private void setOnKeyPressed() {
    setOnKeyPressed((event) -> StreamingUtility.keyNavigation(event, mediaPlayer));
  }
}
