package me.deejack.animeviewer.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.async.SearchByNameAsync;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class SearchController {
  @FXML
  private TextField txtSearchName;
  @FXML
  private VBox root;
  @FXML
  private TitledPane titledPane;

  @FXML
  private void initialize() {
    txtSearchName.setOnKeyPressed((key) -> {
      if (key.getCode() == KeyCode.ENTER)
        onSearchNameClicked();
    });
    /*titledPane.expandedProperty().addListener((event, oldValue, newValue) -> {
      System.out.println(root.getPrefHeight());
      root.setPrefHeight(200 + titledPane.getHeight());
      System.out.println(root.getPrefHeight());
    });*/
  }

  @FXML
  public void onSearchNameClicked() {
    showWaitAndLoad("Cercando per testo...");
    new Thread(new SearchByNameAsync(txtSearchName.getText())).start();
  }
}
