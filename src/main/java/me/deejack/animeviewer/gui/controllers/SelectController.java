package me.deejack.animeviewer.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class SelectController {
  @FXML
  private Button btnHistory;
  @FXML
  private Button btnFavorite;
  @FXML
  private Button btnOpenStreaming;
  @FXML
  private TextField txtLink;

  public SelectController() {
  }

  @FXML
  public void initialize() {
    btnHistory.setOnAction((event) -> {
      showWaitAndLoad("Caricando cronologia, ricorda di cancellare gli hentai...");
      setRoot(new FavoriteController(true));
      hideWaitLoad();
    });
    btnFavorite.setOnAction((event) -> {
      showWaitAndLoad("Caricando i preferiti");
      setRoot(new FavoriteController(false));
      hideWaitLoad();
    });
    btnOpenStreaming.setOnAction((event) -> new AnimePlayer(txtLink.getText()));
  }
}
