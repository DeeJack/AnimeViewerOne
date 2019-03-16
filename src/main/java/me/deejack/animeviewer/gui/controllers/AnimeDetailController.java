package me.deejack.animeviewer.gui.controllers;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.components.animedetail.AnimeInfoBox;
import me.deejack.animeviewer.gui.components.animedetail.ImageAnime;
import me.deejack.animeviewer.gui.components.animedetail.ListViewEpisodes;
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
    BorderPane content = (BorderPane) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    content.setBottom(new ListViewEpisodes(anime, root));
    ((Pane) content.lookup("#boxImage")).getChildren().add(new ImageAnime(anime.getAnimeInformation().getImageUrl()));
    content.setCenter(new AnimeInfoBox(anime));
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
