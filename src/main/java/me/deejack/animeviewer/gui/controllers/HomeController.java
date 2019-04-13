package me.deejack.animeviewer.gui.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.components.general.HiddenSideBar;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.App.getSite;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class HomeController implements BaseScene {
  @FXML
  private TextField txtStreaming;
  @FXML
  private Button btnStreaming;
  private Pane root;

  public HomeController() {
  }

  public void setup() {
    root = (Pane) SceneUtility.loadParent("/scenes/home.fxml");
    ImageView icon = (ImageView) root.lookup("#imgSite");
    Button btnSideBar = (Button) root.lookup("#btnSideBar");
    HiddenSideBar sideBar = new FilterList(btnSideBar, null).getSideBar();
    root.getChildren().add(sideBar);
    Task<Image> task = SceneUtility.loadImage(getSite().getIconUrl());
    setRoot(this);
    task.setOnSucceeded((value) -> {
      icon.setImage(task.getValue());
      icon.toFront();
    });
  }

  @FXML
  private void initialize() {
    btnStreaming.setOnAction((event) -> new AnimePlayer(txtStreaming.getText()));
    btnStreaming.setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenExtVideo")));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return LocalizedApp.getInstance().getString("HomeWindowTitle");
  }

  @Override
  public String getName() {
    return "Home";
  }
}
