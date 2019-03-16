package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility;

public class ButtonPause extends Button {
  private final MediaPlayer mediaPlayer;

  public ButtonPause(MediaPlayer mediaPlayer) {
    super("| |");
    setEllipsisString("| |");
    this.mediaPlayer = mediaPlayer;
    setOnAction((event) -> pause());
    setOnKeyPressed();
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

  private void setOnKeyPressed() {
    setOnKeyPressed((event) -> StreamingUtility.keyNavigation(event, mediaPlayer));
  }
}
