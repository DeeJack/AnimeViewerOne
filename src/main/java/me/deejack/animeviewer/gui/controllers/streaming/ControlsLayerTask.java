package me.deejack.animeviewer.gui.controllers.streaming;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class ControlsLayerTask extends Thread {
  private static final int MILLISECONDS_TO_WAIT = 8 * 1000;
  private final Pane paneToHide;
  private final Pane root;
  private LocalDateTime lastMoved = LocalDateTime.now();
  private volatile boolean isInterrupted = false;
  public static final File tempHistory = new File(System.getProperty("user.home") + File.separator + ".animeviewer" + File.separator +
          "tempHistory.json");

  public ControlsLayerTask(Pane paneToHide, Pane root) {
    this.paneToHide = paneToHide;
    this.root = root;
  }

  @Override
  public void run() {
    Timer timer = new Timer();
    root.setOnMouseMoved((event) -> {
      paneToHide.setVisible(true);
      SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
      lastMoved = LocalDateTime.now();
    });
    paneToHide.setOnMouseMoved((event) -> {
      paneToHide.setVisible(true);
      SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
      lastMoved = LocalDateTime.now();
    });
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted)
          cancel();
        if (Duration.between(lastMoved, LocalDateTime.now()).toMillis() >= MILLISECONDS_TO_WAIT) {
          paneToHide.setVisible(false);
          SceneUtility.getStage().getScene().setCursor(Cursor.NONE);
        }
      }
    }, MILLISECONDS_TO_WAIT, MILLISECONDS_TO_WAIT);

    new Timer().scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (isInterrupted)
          cancel();
        if (!tempHistory.exists()) {
          try {
            tempHistory.createNewFile();
          } catch (IOException e) {
            handleException(e);
          }
        }
        History.getHistory().saveToFile(tempHistory);
      }
    }, TimeUnit.MINUTES.toMillis(30), TimeUnit.MINUTES.toMillis(30));
  }

  public void setInterrupted(boolean interrupted) {
    isInterrupted = interrupted;
  }
}
