package me.deejack.animeviewer.gui.components.appupdates;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class UpdateAvailableDialog extends Dialog {
  private final Release release;

  public UpdateAvailableDialog(Release release) {
    this.release = release;
    initialize();
  }

  private void initialize() {
    setTitle(LocalizedApp.getInstance().getString("NewUpdateTitle"));
    setContentText(createContentText());
    getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
    registerEvents();
  }

  private String createContentText() {
    String msg = LocalizedApp.getInstance().getString("NewUpdateContent")
            .replace("{Version}", release.getVersion())
            .replace("{Changelog}", release.getChangelog())
            .replace("{Link}", release.getAssets()[0].getDownloadLink());
    return msg;
  }

  private void registerEvents() {
    getDialogPane().lookupButton(ButtonType.YES).setOnMousePressed((event) -> {
      close();
      Dialog dialog = new Dialog();
      dialog.setContentText("Beh peccato, la funzione non è stata ancora implementata. Vai su github e scarica la nuova versione :)");
      dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.YES, ButtonType.APPLY, ButtonType.CLOSE);
      dialog.showAndWait();
      //new AppUpdateDialog(release).showAndWait();
    });
  }
}
