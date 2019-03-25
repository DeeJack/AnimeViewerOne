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
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeDetailController implements BaseScene {
  private final Anime anime;
  private final boolean isNewTab;
  private Pane root;

  public AnimeDetailController(Anime anime, boolean isNewTab) {
    this.anime = Favorite.getInstance().contains(anime) ? Favorite.getInstance().get(anime.getUrl()).getAnime() : anime;
    this.isNewTab = isNewTab;
  }

  public void loadAsync() {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingAnimeList"));
    if (Favorite.getInstance().contains(anime))
      load();
    else
      anime.loadAsync(this::load);
  }

  public void loadSync() {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingAnimeList"));
    anime.load();
    load();
    hideWaitLoad();
  }

  private void load() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(this::load);
      return;
    }
    setupScene();
    if (!isNewTab)
      loadScene();
  }

  private void setupScene() {
    root = (Pane) SceneUtility.loadParent("/scenes/animeDetailResp.fxml");
    GridPane gridPane = (GridPane) root.getChildren().get(0);
    ImageAnime imageAnime = new ImageAnime(anime.getAnimeInformation().getImageUrl());
    HBox boxImage = new HBox(imageAnime);
    boxImage.setMinHeight(Double.MIN_VALUE);
    imageAnime.fitHeightProperty().bind(boxImage.heightProperty());
    boxImage.setAlignment(Pos.CENTER);
    if (!isNewTab) {
      ButtonBack btnBack = new ButtonBack();
      HBox hBox = new HBox(btnBack);
      hBox.setAlignment(Pos.TOP_LEFT);
      //btnBack.setOpaqueInsets(new Insets(0, 0, 0, 0));
      gridPane.add(hBox, 1, 1);
    }
    ListViewEpisodes listViewEpisodes = new ListViewEpisodes(anime);
    AnimeInfoBox infoBox = new AnimeInfoBox(anime);
    infoBox.setOnReload((action) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("Reloading"));
      anime.loadAsync(() -> Platform.runLater(() -> {
        listViewEpisodes.reload();
        infoBox.reload();
        hideWaitLoad();
      }));
    });
    gridPane.add(boxImage, 2, 1);
    gridPane.add(infoBox, 2, 2);
    gridPane.add(listViewEpisodes, 2, 3);
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
