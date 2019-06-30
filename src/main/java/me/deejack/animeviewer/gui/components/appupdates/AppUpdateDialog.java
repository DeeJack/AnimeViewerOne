package me.deejack.animeviewer.gui.components.appupdates;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.components.download.SingleDownload;
import me.deejack.animeviewer.logic.githubupdates.LocalAppUpdate;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.utils.FilesManager;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class AppUpdateDialog extends Dialog {
  private final File fileVersion;

  public AppUpdateDialog(Release release) {
    fileVersion = new File(FilesManager.VERSIONS_FOLDER.getPath() + File.separator + release.getVersion() + ".jar");
    System.out.println(fileVersion.getPath());
    if (fileVersion.exists())
      fileVersion.delete();
    SingleDownload download = new SingleDownload(
            fileVersion,
            release.getAssets()[0].getDownloadLink(), "Version " + release.getVersion());
    initialize(download);
  }

  private void initialize(SingleDownload download) {
    getDialogPane().setContent(download.getRoot());
    getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    download.start();
    registerEvents(download);
  }

  private void registerEvents(SingleDownload download) {
    initStyle(StageStyle.UNDECORATED);
    getDialogPane().lookupButton(ButtonType.CANCEL).setOnMousePressed((event) -> {
      if (requestConfirm(download))
        event.consume();
      close();
    });
    download.setOnSucceed(this::executeNewVersion);
  }

  private void executeNewVersion() {
    Platform.runLater(() -> {
      close();
      try {
        LocalAppUpdate.executeNewVersion(fileVersion.getPath(), new File(App.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath());
      } catch (URISyntaxException e) {
        handleException(new RuntimeException("Exception while opening the new version of the app", e));
      }
    });
  }

  private boolean requestConfirm(SingleDownload download) {
    AtomicBoolean confirmed = new AtomicBoolean(false);
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, LocalizedApp.getInstance().getString("CloseAppUpdate"), ButtonType.YES, ButtonType.NO);
    alert.getDialogPane().lookupButton(ButtonType.YES).setOnMousePressed((event) -> {
      confirmed.set(true);
      download.cancel();
    });
    alert.showAndWait();
    System.out.println(confirmed.get());
    return confirmed.get();
  }
}
