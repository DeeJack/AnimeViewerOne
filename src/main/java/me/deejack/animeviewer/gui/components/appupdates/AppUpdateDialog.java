package me.deejack.animeviewer.gui.components.appupdates;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class AppUpdateDialog extends Dialog {

  public AppUpdateDialog(Release release) {

  }

  private void initialize() {
    getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
    getDialogPane().setContentText(LocalizedApp.getInstance().getString("UpdateAvailable"));
  }
}
