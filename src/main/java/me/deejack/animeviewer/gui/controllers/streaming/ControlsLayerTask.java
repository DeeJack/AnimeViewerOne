package me.deejack.animeviewer.gui.controllers.streaming;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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

  public ControlsLayerTask(Pane paneToHide, MediaView mediaView) {
    this.paneToHide = paneToHide;
    this.mediaView = mediaView;
  }

  @Override
  public void run() {
    Timer timer = new Timer();
    EventHandler onMove = (event) -> {
      paneToHide.setVisible(true);
      SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
      lastMoved = LocalDateTime.now();
    };
    mediaView.setOnMouseMoved(onMove);
    mediaView.setOnKeyPressed(onMove);
    timer.scheduleAtFixedRate(new TimerTask() {
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
    }, MILLISECONDS_TO_WAIT, MILLISECONDS_TO_WAIT);

    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted)
          cancel();
        FilesUtility.saveTempHistory();
      }
    }, TimeUnit.MINUTES.toMillis(30), TimeUnit.MINUTES.toMillis(30));
  }

  public void setInterrupted(boolean interrupted) {
    isInterrupted = interrupted;
  }
}
