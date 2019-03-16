package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.components.streaming.ButtonPause;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public final class StreamingUtility {

  private StreamingUtility() {
  }

  public static void keyNavigation(KeyEvent keyEvent, MediaPlayer mediaPlayer) {
    keyEvent.consume();
    switch (keyEvent.getCode()) {
      case UP:
        if (mediaPlayer.getVolume() < 0.55)
          mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.05);
        break;
      case DOWN:
        if (mediaPlayer.getVolume() > 0.05)
          mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.05);
        break;
      case RIGHT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(4)));
        break;
      case LEFT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(4)));
        break;
      case F11:
        SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
        break;
      case SPACE:
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
          mediaPlayer.pause();
        else
          mediaPlayer.play();
        break;
    }
  }

  public static void onChangeStatus(MediaPlayer.Status status, ButtonPause buttonPause) {
    switch (status) {
      case PLAYING:
        buttonPause.setText("| |");
        break;
      case UNKNOWN:
      case STALLED:
        buttonPause.setText(">");
        showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingVideo"));
        break;
      case READY:
        hideWaitLoad();
        break;
      case PAUSED:
      case STOPPED:
        buttonPause.setText(">");
        break;
      case HALTED:
        Alert alert = new Alert(Alert.AlertType.ERROR,
                LocalizedApp.getInstance().getString("AlertErrorOnStreaming"),
                ButtonType.OK);
        alert.showAndWait();
    }
  }
}
