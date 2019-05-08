package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

public class LabelTime extends Label {
  private final MediaPlayer mediaPlayer;

  public LabelTime(MediaPlayer mediaPlayer) {
    super("0:00:00/00:00:00");
    this.mediaPlayer = mediaPlayer;
    setMinWidth(102);
    setOnChange();
  }

  private void setOnChange() {
    mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
      int hours = (int) newValue.toHours();
      int minutes = (int) newValue.toMinutes() % 60;
      int seconds = (int) newValue.toSeconds() % 60 % 60;
      int endHours = (int) mediaPlayer.getTotalDuration().toHours() + (int) mediaPlayer.getStartTime().toHours();
      int endMinutes = (int) (mediaPlayer.getTotalDuration().toMinutes() + mediaPlayer.getStartTime().toMinutes()) % 60;
      int endSeconds = (int) (mediaPlayer.getTotalDuration().toSeconds() + mediaPlayer.getStartTime().toSeconds()) % 60 % 60;
      setText(String.format("%01d:%02d:%02d/%02d:%02d:%02d", hours, minutes, seconds, endHours, endMinutes, endSeconds));
    });
  }
}
