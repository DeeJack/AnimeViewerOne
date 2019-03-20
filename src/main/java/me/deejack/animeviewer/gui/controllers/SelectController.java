package me.deejack.animeviewer.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.utils.LocalizedApp;

public class SelectController {
  @FXML
  private Button btnHistory;
  @FXML
  private Button btnFavorite;
  @FXML
  private Button btnOpenStreaming;
  @FXML
  private TextField txtLink;

  @FXML
  public void initialize() {
    btnOpenStreaming.setOnAction((event) -> new AnimePlayer(txtLink.getText()));
    btnOpenStreaming.setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenExtVideo")));
  }
}
