package me.deejack.animeviewer.gui.controllers.download;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import me.deejack.animeviewer.gui.components.dialogs.ChooseSourceDialog;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public final class DownloadUtility {
  private static boolean showingPopupSources = false;
  private DownloadUtility() {
  }

  /**
   * Show a dialog to let the user choose a path for saving the file
   *
   * @param text The initial file name when opening the dialog
   * @return The file chosen by the user, may be null
   */
  public static File savePath(String text) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select the path");
    fileChooser.setInitialFileName(text + ".mp4");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return fileChooser.showSaveDialog(SceneUtility.getStage());
  }

  /**
   * Open a dialog to let the user choose a folder where save the videos
   *
   * @return The directory chosen by the user
   */
  public static File saveDirectory() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select folder");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return chooser.showDialog(SceneUtility.getStage());
  }

  private static boolean checkEpisodeReleased(Episode episode) {
    if (episode.getUrl() == null || episode.getUrl().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Nessuno createStreaming disponibile, probabilmente deve ancora uscire l'episodio",
              ButtonType.OK);
      alert.showAndWait();
      hideWaitLoad();
      return false;
    }
    return true;
  }

  /**
   * Let the user chose from which source (and with which resolution) to download the video
   *
   * @return null if the user cancel the operation (close the popup), otherwise it'll return the reference to the selected link
   * @throws IOException
   */
  public static void chooseSource(Episode episode, WebBypassUtility.CallBack<StreamingLink> callBack) throws IOException {
    if (showingPopupSources) // TODO dividere il metodo ç.ç
      return;
    showingPopupSources = true;
    if (!checkEpisodeReleased(episode)) {
      showingPopupSources = false;
      callBack.onSuccess(null);
      return;
    }
    Task<List<StreamingLink>> linkFutureTask = new Task<List<StreamingLink>>() {
      @Override
      protected List<StreamingLink> call() throws Exception {
        return episode.getStreamingLinks();
      }

      @Override
      public void failed() {
        handleException(getException());
      }
    };
    new Thread(linkFutureTask).start();
    linkFutureTask.setOnSucceeded((event) -> {
      List<StreamingLink> streamingLinks = linkFutureTask.getValue();
      if (streamingLinks == null) {
        showingPopupSources = false;
        callBack.onSuccess(null);
        return;
      }
      if (streamingLinks.size() == 1) {
        if (streamingLinks.get(0).allowsEmbeddedVideo())
          callBack.onSuccess(streamingLinks.get(0));
        else showNotSupportedVideoError(streamingLinks.get(0));
        showingPopupSources = false;
        return;
      } else if (streamingLinks.isEmpty()) {
        showNoStreaming();
        showingPopupSources = false;
        callBack.onSuccess(null);
        return;
      }

      ChooseSourceDialog sourceDialog = new ChooseSourceDialog(streamingLinks);
      sourceDialog.setOnEvent((selectedLink) -> {
        if (selectedLink == null) {
          hideWaitLoad();
          showingPopupSources = false;
          return;
        }
        hideWaitLoad();
        if (selectedLink.allowsEmbeddedVideo())
          callBack.onSuccess(selectedLink);
        else showNotSupportedVideoError(selectedLink);
        showingPopupSources = false;
      });
      sourceDialog.showAndWait();
    });
  }

  public static int choseDownloadSettings() {
    AtomicInteger prefResolution = new AtomicInteger(-1);
    Label labelRes = new Label(LocalizedApp.getInstance().getString("FavoriteResolutionDownload") + " ");
    TextField textFieldRes = new TextField("720");
    HBox boxSettings = new HBox(20, labelRes, textFieldRes);
    boxSettings.setAlignment(Pos.CENTER);
    Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK, ButtonType.CANCEL);
    alert.setGraphic(boxSettings);
    alert.resultProperty().addListener((event, oldValue, newValue) -> {
      if (newValue != ButtonType.OK) {
        alert.close();
        return;
      }
      Optional<Integer> resolution = GeneralUtility.tryParse(textFieldRes.getText());
      if (!resolution.isPresent() || resolution.get() < 100 || resolution.get() > 8000) {
        new Alert(Alert.AlertType.ERROR,
                LocalizedApp.getInstance().getString("ErrorNotInteger"),
                ButtonType.OK)
                .showAndWait();
        return;
      }
      prefResolution.set(GeneralUtility.tryParse(textFieldRes.getText()).get());
    });
    alert.showAndWait();
    return prefResolution.get();
  }

  private static void showNotSupportedVideoError(StreamingLink selectedLink) {
    new Alert(Alert.AlertType.WARNING,
            LocalizedApp.getInstance().getString("SiteNotSupported").replace("{Site}", selectedLink.getSource()),
            ButtonType.OK)
            .show();
    hideWaitLoad();
    try {
      Desktop.getDesktop().browse(new URI(selectedLink.getLink()));
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private static void showNoStreaming() {
    Alert alert = new Alert(Alert.AlertType.WARNING, LocalizedApp.getInstance().getString("NoStreaming"),
            ButtonType.OK);
    alert.showAndWait();
    hideWaitLoad();
  }
}
