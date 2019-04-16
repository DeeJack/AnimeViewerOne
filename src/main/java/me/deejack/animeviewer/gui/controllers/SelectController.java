package me.deejack.animeviewer.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class SelectController {
  @FXML
  private Button btnOpenStreaming;
  @FXML
  private TextField txtLink;

  @FXML
  public void initialize() {
    btnOpenStreaming.setOnAction((event) -> {
      showWaitAndLoad();
      new AnimePlayer(txtLink.getText());
    });
    btnOpenStreaming.setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenExtVideo")));
  }
}
