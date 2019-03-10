package me.deejack.animeviewer.gui.controllers.streaming.components;

import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

public class LabelTime extends Label {
  private final MediaPlayer mediaPlayer;

  public LabelTime(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    setMinWidth(102);
    setOnChange();
  }

  private void setOnChange() {
    mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
      int hours = (int) newValue.toHours();
      int minutes = (int) newValue.toMinutes() % 60;
      int seconds = (int) newValue.toSeconds() % 60 % 60;
      int endHours = (int) mediaPlayer.getTotalDuration().toHours();
      int endMinutes = (int) mediaPlayer.getTotalDuration().toMinutes() % 60;
      int endSeconds = (int) mediaPlayer.getTotalDuration().toSeconds() % 60 % 60;
      setText(String.format("%01d:%02d:%02d/%02d:%02d:%02d", hours, minutes, seconds, endHours, endMinutes, endSeconds));
    });
  }
}
