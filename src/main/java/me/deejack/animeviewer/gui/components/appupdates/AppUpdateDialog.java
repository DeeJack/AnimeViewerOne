package me.deejack.animeviewer.gui.components.appupdates;

import java.io.File;
import javafx.scene.control.Dialog;
import me.deejack.animeviewer.gui.components.download.SingleDownload;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.utils.FilesManager;

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
    download.start();
  }
}
