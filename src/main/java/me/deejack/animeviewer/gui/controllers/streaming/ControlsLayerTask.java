package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
    startFakeMouseMove();
  }

  private EventHandler<InputEvent> onAction() {
    return (event) -> {
      if (event instanceof KeyEvent)
        StreamingUtility.keyNavigation((KeyEvent) event, mediaView.getMediaPlayer());
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

  /**
   * Create a timer that every 5 minutes do a "fake move" of the mouse, so windows doesn't start "sleeping"
   * Why javafx doesn't have something that do this already?
   */
  public void startFakeMouseMove() {
    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted)
          cancel();
        fakeMove();
      }
    }, Duration.ofMinutes(1).toMillis() - 5, Duration.ofMinutes(1).toMillis() - 5);
  }

  public void fakeMove() {
    try {
      new Robot().mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    } catch (AWTException e) {
      GeneralUtility.logError(e);
    }
  }
}
