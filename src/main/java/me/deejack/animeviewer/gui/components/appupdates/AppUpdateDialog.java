package me.deejack.animeviewer.gui.components.appupdates;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import me.deejack.animeviewer.gui.components.download.SingleDownload;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.utils.FilesManager;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppUpdateDialog extends Dialog {
  private final Release release;
  private final SingleDownload download;

  public AppUpdateDialog(Release release) {
    this.release = release;
    File fileVersion = new File(FilesManager.VERSIONS_FOLDER.getPath() + File.separator + release.getVersion() + ".jar");
    System.out.println(fileVersion.getPath());
    if (fileVersion.exists())
      fileVersion.delete();
    download = new SingleDownload(
            fileVersion,
            release.getAssets()[0].getDownloadLink(), "Version " + release.getVersion());
    initialize();
  }

  private void initialize() {
    getDialogPane().setContent(download.getRoot());
    getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    download.start();
    registerEvents();
  }

  private void registerEvents() {
    initStyle(StageStyle.UNDECORATED);
    getDialogPane().lookupButton(ButtonType.CANCEL).setOnMousePressed((event) -> {
      if (requestConfirm())
        event.consume();
      close();
    });
  }

  private boolean requestConfirm() {
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
