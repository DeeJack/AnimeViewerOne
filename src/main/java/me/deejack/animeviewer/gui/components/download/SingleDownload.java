package me.deejack.animeviewer.gui.components.download;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import java.io.File;

import static me.deejack.animeviewer.gui.controllers.download.DownloadUtility.toMB;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

public class SingleDownload {
  private final DownloadAsync downloadAsync;
  private final BorderPane root;
  private final String title;

  public SingleDownload(File output, String downloadLink, String title) {
    downloadAsync = new DownloadAsync(output, downloadLink);
    root = (BorderPane) SceneUtility.loadParent("/scenes/download/singleDownload.fxml");
    this.title = title;
    layout();
  }

  private void layout() {
    Label labelSize = (Label) root.getBottom().lookup("#lblSize");
    Label labelPerc = (Label) root.getBottom().lookup("#lblPerc");
    Label labelTitle = (Label) root.getTop().lookup("#lblTitle");
    labelTitle.setText(title);
    ProgressIndicator progressBar = (ProgressIndicator) root.getCenter().lookup("#progressBar");
    registerListeners(labelSize, labelPerc, progressBar);
  }

  private void registerListeners(Label labelSize, Label labelPerc, ProgressIndicator progressBar) {
    downloadAsync.getSizeProperty().addListener((newValue) -> {
      double percentage = ((double) newValue / downloadAsync.getTotalDownloadSize());
      Platform.runLater(() -> {
        labelSize.setText(String.format("%.1f/%.1f MB", toMB(newValue), toMB(downloadAsync.getTotalDownloadSize())));
        labelPerc.setText((int) (percentage * 100) + "%");
        progressBar.setProgress(percentage);
      });
    });
    downloadAsync.addFailListener((exc) -> {
      Platform.runLater(() -> {
        // TrayNotification notification = new TrayNotification("Download error",
        //         LocalizedApp.getInstance().getString("ErrorDownload"),
        //         Notifications.ERROR);
        // notification.setAnimation(Animations.FADE);
        // notification.showAndDismiss(Duration.seconds(2));
        // TODO: replace with something that works
      });
      logError(exc);
    });
  }

  public void start() {
    new Thread(downloadAsync).start();
  }

  public void cancel() {
    downloadAsync.setCancelled(true);
  }

  public BorderPane getRoot() {
    return root;
  }

  public void setOnSucceed(SuccessListener succeed) {
    downloadAsync.addSuccessListener(succeed);
  }
}
