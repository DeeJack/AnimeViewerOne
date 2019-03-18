package me.deejack.animeviewer.gui.controllers;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.components.animedetail.AnimeInfoBox;
import me.deejack.animeviewer.gui.components.animedetail.ImageAnime;
import me.deejack.animeviewer.gui.components.animedetail.ListViewEpisodes;
import me.deejack.animeviewer.gui.components.general.ButtonBack;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeDetailController implements BaseScene {
  private final Anime anime;
  private Pane root;

  public AnimeDetailController(Anime anime) {
    this.anime = anime;
  }

  public void loadAsync() {
    anime.loadAsync(this::load);
  }

  private void load() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(this::load);
      return;
    }
    setupScene();
    loadScene();
  }

  private void setupScene() {
    root = (Pane) SceneUtility.loadParent("/scenes/animeDetailResp.fxml");
    GridPane gridPane = (GridPane) (root);
    ImageAnime imageAnime = new ImageAnime(anime.getAnimeInformation().getImageUrl());
    HBox boxImage = new HBox(imageAnime);
    boxImage.setMinHeight(Double.MIN_VALUE);
    imageAnime.fitHeightProperty().bind(boxImage.heightProperty());
    boxImage.setAlignment(Pos.CENTER);
    gridPane.add(new ButtonBack(), 0, 0);
    gridPane.add(boxImage, 2, 1);
    gridPane.add(new AnimeInfoBox(anime), 2, 2);
    gridPane.add(new ListViewEpisodes(anime, root), 2, 3);
  }

  private void loadScene() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(this::loadScene);
      return;
    }
    setRoot(this);
  }

  @Override
  public Pane getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return LocalizedApp.getInstance().getString("AnimeDetailWindowTitle") + " " + anime.getAnimeInformation().getName();
  }

  @Override
  public String getName() {
    return "Detail";
  }
}
