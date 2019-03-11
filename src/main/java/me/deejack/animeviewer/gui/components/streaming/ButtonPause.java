package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

public class ButtonPause extends Button {
  private final MediaPlayer mediaPlayer;

  public ButtonPause(MediaPlayer mediaPlayer) {
    super("| |");
    setEllipsisString("| |");
    this.mediaPlayer = mediaPlayer;
    setOnAction((event) -> pause());
  }

  public void pause() {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
      mediaPlayer.pause();
      setText(">");
    } else {
      mediaPlayer.play();
      setText("| |");
    }
  }
}
