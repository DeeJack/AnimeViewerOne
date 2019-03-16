package me.deejack.animeviewer.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.async.SearchByNameAsync;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.utils.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class SearchController {
  @FXML
  private TextField txtSearchName;
  @FXML
  private VBox root;

  @FXML
  private void initialize() {
    txtSearchName.setOnKeyPressed((key) -> {
      if (key.getCode() == KeyCode.ENTER)
        onSearchNameClicked();
    });
  }

  @FXML
  public void onSearchNameClicked() {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingSearchText"));
    new Thread(new SearchByNameAsync(txtSearchName.getText())).start();
  }
}
