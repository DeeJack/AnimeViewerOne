package me.deejack.animeviewer.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.components.animedetail.AnimeInfoBox;
import me.deejack.animeviewer.gui.components.animedetail.ImageAnime;
import me.deejack.animeviewer.gui.components.animedetail.ListViewEpisodes;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
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
    //lstViewEpisodes.setPrefHeight(158 + (SceneUtility.getStage().getScene().getHeight() - 530));
  }

  private void sagaRightClick(Season season, double x, double y) {
    MenuItem downloadSaga = new MenuItem("Scarica intera saga");
    downloadSaga.setOnAction((event) -> downloadSaga(season));
    MenuItem downloadAll = new MenuItem("Scarica tutti gli episodi");
    downloadAll.setOnAction((event) -> downloadAll());
    ContextMenu menu = new ContextMenu(downloadSaga, downloadAll);
    menu.show(SceneUtility.getStage(), x, y);
  }

  private void downloadSaga(Season season) {
    List<Episode> episodes = new ArrayList<>(season.getEpisodes());
    downloadEpisodes(episodes);
  }

  private void downloadEpisodes(List<Episode> episodes) {
    DownloadController controller = DownloadController.getDownloadController();
    controller.addDownloads(episodes, anime.getAnimeInformation().getName());
  }

  private void downloadAll() {
    List<Episode> episodes = new ArrayList<>();
    /*for (Season season : anime.getEpisodes()) {
      episodes.addAll(season.getEpisodes());
    }*/
    downloadEpisodes(episodes);
  }

  private void download(Episode episode) {
    DownloadController controller = DownloadController.getDownloadController();
    controller.singleDownload(episode, anime.getAnimeInformation().getName());
  }

  @Override
  public Pane getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return "Dettaglio " + anime.getAnimeInformation().getName();
  }

  @Override
  public String getName() {
    return "Detail";
  }
}
