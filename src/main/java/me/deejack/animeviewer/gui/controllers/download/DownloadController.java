package me.deejack.animeviewer.gui.controllers.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

/**
 * Number of time this class has been rewritten: 3
 */
public final class DownloadController {
  private static final DownloadController downloadController = new DownloadController();
  private final Stage stage = new Stage();
  private final List<DownloadAsync> downloadsInProgress = new ArrayList<>();
  private Image imageCancel;
  private Image imageRestart;

  private DownloadController() {
    layout();
  }

  public static DownloadController getDownloadController() {
    return downloadController;
  }

  private void layout() {
    stage.setResizable(false);
    Parent downloadRoot = SceneUtility.loadParent("/scenes/download/downloadProgress.fxml");
    ((ScrollPane) downloadRoot).setContent(new FlowPane());
    stage.setScene(new Scene(downloadRoot));
    stage.setOnCloseRequest((event) -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Chiudere la finestra? Fare questo fermerà i download",
              ButtonType.OK, ButtonType.CANCEL);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.OK) {
        for (DownloadAsync download : downloadsInProgress)
          download.setCancelled(true);
      } else event.consume();
    });
    imageCancel = new Image(App.class.getResourceAsStream("/assets/cancel.png"));
    imageRestart = new Image(App.class.getResourceAsStream("/assets/resume2.png"));
  }

  public void addDownloads(List<Episode> episodes, String animeName) {
    int prefResolution = DownloadUtility.choseDownloadSettings();
    AtomicReference<DownloadAsync> previousDownload = new AtomicReference<>();
    if (prefResolution == -1)
      return;
    File destination = DownloadUtility.saveDirectory();
    if (destination == null)
      return;
    AtomicInteger count = new AtomicInteger(0);

    /*SuccessListener finishListener = () -> chooseLink(episodes.get(count.addAndGet(1)), prefResolution, animeName, destination, () -> {
    });*/
    chooseLink(episodes, count, prefResolution, animeName, destination);
  }

  private void chooseLink(List<Episode> episodes, AtomicInteger count, int prefResolution, String animeName, File destination) {
    List<StreamingLink> links = episodes.get(count.get()).getStreamingLinks();
    if (links.isEmpty())
      return;
    AtomicReference<StreamingLink> link = new AtomicReference<>(links.get(0));
    links.forEach((streamingLink -> {
      if (Math.abs(streamingLink.getResolution() - prefResolution) < Math.abs(link.get().getResolution() - prefResolution))
        link.set(streamingLink);
    }));
    processLink(link.get(), (resultLink) -> startDownload(resultLink, episodes.get(count.get()), animeName, destination, () -> {
      chooseLink(episodes, new AtomicInteger(count.addAndGet(1)), prefResolution, animeName, destination);
    }));
  }

  public void singleDownload(Episode episode, String animeName) {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingStartDownload"));
    try {
      DownloadUtility.chooseSource(episode, (downloadLink) -> {
        if (downloadLink == null)
          return;
        File destination = DownloadUtility.savePath(episode.getTitle());
        if (destination == null)
          return;
        processLink(downloadLink, (resultLink) -> startDownload(resultLink, episode, animeName, destination, () -> {
        }));
      });
    } catch (IOException e) {
      handleException(e);
    }
  }

  private void processLink(StreamingLink downloadLink, WebBypassUtility.Callback<String> callBack) {
    Connection.Response response = ConnectionUtility.connect(downloadLink.getLink(), false);
    if (response.contentType().contains("video")) {
      callBack.onSuccess(downloadLink.getLink());
    } else
      WebBypassUtility.bypassSite(downloadLink.getLink(), callBack);
  }

  private void startDownload(String downloadLink, Episode episode, String animeName, File destination, SuccessListener finishListener) {
    if (destination.isDirectory()) {
      destination = new File(destination.getPath() + File.separator + animeName + " - " + episode.getNumber() + ".mp4");
    }
    System.out.println("DOWNLOADING " + downloadLink);
    DownloadAsync downloadAsync = new DownloadAsync(destination, downloadLink);
    downloadsInProgress.add(downloadAsync);

    layoutDownload(episode, downloadAsync, animeName);
    downloadAsync.addFailListener(SceneUtility::handleException);
    downloadAsync.addFailListener((exc) -> finishListener.onSuccess());
    downloadAsync.addSuccessListener(finishListener);
    downloadAsync.addCancelListener((cancelValue) -> finishListener.onSuccess());
    new Thread(downloadAsync).start();
    if (!stage.isShowing()) {
      stage.show();
      hideWaitLoad();
    }
  }

  private void layoutDownload(Episode episode, DownloadAsync downloadAsync, String animeName) {
    File dest = downloadAsync.getOutput().isDirectory() ?
            new File(downloadAsync.getOutput().getPath() + File.separator +
                    animeName + " - " + episode.getNumber() + ".mp4") :
            downloadAsync.getOutput();

    Parent downloadComp = SceneUtility.loadParent("/scenes/download/singleDownload.fxml");
    ((Pane) ((ScrollPane) stage.getScene().getRoot()).getContent()).getChildren().add(downloadComp);
    registerListeners(downloadAsync, downloadComp, stage, episode, animeName);
  }

  private void registerListeners(DownloadAsync downloadAsync, Parent downloadComp, Stage currentStage, Episode episode, String animeName) {
    downloadAsync.getSizeProperty().addListener((newValue) -> {
      double percentage = ((double) newValue / downloadAsync.getTotalDownloadSize());
      Platform.runLater(() -> {
        ((Labeled) downloadComp.lookup("#lblSize")).setText(String.format("%.1f/%.1f MB", toMB(newValue), toMB(downloadAsync.getTotalDownloadSize())));
        ((Labeled) downloadComp.lookup("#lblPerc")).setText((int) (percentage * 100) + "%");
        ((ProgressIndicator) downloadComp.lookup("#progressBar")).setProgress(percentage);
      });
    });
    downloadAsync.addFailListener(SceneUtility::handleException); // TODO: personalizzata, lui può fallire ma gli altri nella coda devono provare a scaricarsi
    ImageView toggleDownload = (ImageView) downloadComp.lookup("#imageCancel");
    (downloadComp.lookup("#layerToggle")).setOnMouseClicked((event -> toggleDownload(downloadAsync, toggleDownload)));
    ((Labeled) downloadComp.lookup("#lblTitle")).setText(String.format("%s: %d (%s)",
            animeName,
            episode.getNumber(),
            episode.getTitle()));
    downloadAsync.addFailListener((exc) -> toggleDownload.setImage(imageRestart));
    downloadAsync.addSuccessListener(() -> Platform.runLater(() -> ((Pane) downloadComp.getParent()).getChildren().remove(downloadComp)));
  }

  private void toggleDownload(DownloadAsync downloadAsync, ImageView toggleDownload) {
    downloadAsync.setCancelled(!downloadAsync.getCancelled());
    if (downloadAsync.getCancelled()) {
      toggleDownload.setImage(imageRestart);
      return;
    }
    new Thread(downloadAsync).start();
    toggleDownload.setImage(imageCancel);
  }

  /**
   * Converts byte to MegaByte
   */
  private double toMB(long value) {
    return (double) value / 1024 / 1024;
  }
}
