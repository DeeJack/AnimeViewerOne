package me.deejack.animeviewer.gui.controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import me.deejack.animeviewer.gui.async.LoadSiteAsync;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.source.AnimeSource;

import static me.deejack.animeviewer.gui.App.getSite;
import static me.deejack.animeviewer.gui.utils.SceneUtility.getStage;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class HomeController implements BaseScene {
  @FXML
  private Button btnFavorites;
  @FXML
  private Button btnHistory;
  @FXML
  private TextField txtStreaming;
  @FXML
  private Button btnStreaming;
  private Parent root;

  public HomeController() {
  }

  public void setup() {
    root = SceneUtility.loadParent("/scenes/home.fxml");
    ((Region) root).heightProperty().addListener((event) -> root.getChildrenUnmodifiable().get(0).prefWidth(((Region) root).getWidth()));
    ImageView icon = (ImageView) root.lookup("#imgSite");
    //((Pane) root.lookup("#paneSearch")).getChildren().add((SceneUtility.loadParent("/scenes/search.fxml")));
    Task<Image> task = SceneUtility.loadImage(getSite().getIconUrl());
    setRoot(this);
    getStage().show();
    task.setOnSucceeded((value) -> {
      icon.setImage(task.getValue());
      icon.toFront();
    });
  }

  private void onSelectionChange(AnimeSource oldSite, AnimeSource newSite) {
    if (getSite() != newSite)
      new Thread(new LoadSiteAsync(newSite)).start();
  }

  @FXML
  private void initialize() {
    btnFavorites.setOnAction((event) -> setRoot(new FavoriteController(false)));
    btnHistory.setOnAction((event) -> setRoot(new FavoriteController(true)));
    btnStreaming.setOnAction((event) -> new AnimePlayer(txtStreaming.getText()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return "Home " + getSite().getName();
  }

  @Override
  public String getName() {
    return "Home";
  }
}
