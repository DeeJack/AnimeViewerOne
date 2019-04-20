package me.deejack.animeviewer.gui.controllers.streaming;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class ControlsLayerTask extends Thread {
  private static final int MILLISECONDS_TO_WAIT = 8 * 1000;
  private final Pane paneToHide;
  private final MediaView mediaView;
  private LocalDateTime lastMoved = LocalDateTime.now();
  private volatile boolean isInterrupted = false;
  private double secondsWatched = 0;

  public ControlsLayerTask(Pane paneToHide, MediaView mediaView) {
    this.paneToHide = paneToHide;
    this.mediaView = mediaView;
  }

  @Override
  public void run() {
    EventHandler<InputEvent> onMove = onAction();
    SceneUtility.getStage().getScene().getRoot().setOnKeyPressed(onMove);
    SceneUtility.getStage().getScene().getRoot().setOnTouchPressed(onMove);
    SceneUtility.getStage().getScene().getRoot().setOnMouseMoved(onMove);
    Timer hideCursorTimer = new Timer();
    Timer saveHistoryTimer = new Timer();
    hideCursorTimer.scheduleAtFixedRate(hideCursorTimer(), MILLISECONDS_TO_WAIT, MILLISECONDS_TO_WAIT);
    saveHistoryTimer.scheduleAtFixedRate(saveHistoryTimer(), TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS.toMillis(30));
  }

  private EventHandler<InputEvent> onAction() {
    return (event) -> {
      paneToHide.setVisible(true);
      SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
      lastMoved = LocalDateTime.now();
    };
  }

  private TimerTask hideCursorTimer() {
    return new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted) {
          cancel();
          return;
        }
        if (Duration.between(lastMoved, LocalDateTime.now()).toMillis() >= MILLISECONDS_TO_WAIT) {
          Platform.runLater(() -> {
            paneToHide.setVisible(false);
            mediaView.requestFocus();
          });
          SceneUtility.getStage().getScene().setCursor(Cursor.NONE);
        }
      }
    };
  }

  private TimerTask saveHistoryTimer() {
    return new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted)
          cancel();
        if (secondsWatched == mediaView.getMediaPlayer().getCurrentTime().toSeconds()) // The video is paused
          return;
        secondsWatched = mediaView.getMediaPlayer().getCurrentTime().toSeconds();
        FilesUtility.saveTempHistory();
      }
    };
  }

  public void setInterrupted(boolean interrupted) {
    isInterrupted = interrupted;
    if (interrupted) {
      SceneUtility.getStage().getScene().getRoot().setOnKeyPressed(null);
      SceneUtility.getStage().getScene().getRoot().setOnTouchPressed(null);
      SceneUtility.getStage().getScene().getRoot().setOnMouseMoved(null);
    }
  }
}
