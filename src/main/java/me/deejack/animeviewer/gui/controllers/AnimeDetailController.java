package me.deejack.animeviewer.gui.controllers;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.components.animedetail.AnimeInfoBox;
import me.deejack.animeviewer.gui.components.animedetail.BoxBackButton;
import me.deejack.animeviewer.gui.components.animedetail.BoxImage;
import me.deejack.animeviewer.gui.components.animedetail.ListViewEpisodes;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.favorite.FavoriteAnime;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeDetailController implements BaseScene {
  private final Anime anime;
  private final boolean isNewTab;
  private final Tab currentTab;
  private final Pane root;
  private ListViewEpisodes listViewEpisodes;

  public AnimeDetailController(Anime anime, boolean isNewTab, Tab currentTab) {
    this.anime = Favorite.getInstance().get(anime.getUrl())
            .map(FavoriteAnime::getAnime).orElse(anime);
    this.isNewTab = isNewTab;
    this.currentTab = currentTab;
    root = (Pane) SceneUtility.loadParent("/scenes/animeDetailResp.fxml");
  }

  public void loadAsync() {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingAnimeList"), currentTab);
    if (Favorite.getInstance().contains(anime))
      load();
    else
      anime.loadAsync(this::load);
  }

  public void loadSync() {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingAnimeList"), currentTab);
    anime.load();
    load();
  }

  private void load() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(this::load);
      return;
    }
    setupScene();
    hideWaitLoad();
    if (!isNewTab)
      setRoot(this);
  }

  private void setupScene() {
    GridPane gridPane = (GridPane) root.getChildren().get(0);
    gridPane.prefHeightProperty().bind(root.heightProperty());
    BoxImage boxImage = new BoxImage(anime.getAnimeInformation().getImageUrl());
    if (!isNewTab)
      gridPane.add(new BoxBackButton(), 1, 2);
    listViewEpisodes = new ListViewEpisodes(anime, onStreamingRequested());
    AnimeInfoBox infoBox = new AnimeInfoBox(anime);
    infoBox.setOnReload((action) -> reloadInfoBox(listViewEpisodes, infoBox));
    gridPane.add(boxImage, 2, 2);
    gridPane.add(infoBox, 2, 3);
    gridPane.add(listViewEpisodes, 2, 4);
  }

  private Listener<Episode> onStreamingRequested() {
    return (episode) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingEpisodeLinks"), currentTab);
      new Thread(() -> {
        AnimePlayer player = new AnimePlayer(episode, anime, isNewTab, currentTab);
        Platform.runLater(player::createStreaming);
      }).start();
    };
  }

  private void reloadInfoBox(ListViewEpisodes listViewEpisodes, AnimeInfoBox infoBox) {
    showWaitAndLoad(LocalizedApp.getInstance().getString("Reloading"), currentTab);
    anime.loadAsync(() -> Platform.runLater(() -> {
      listViewEpisodes.reload();
      infoBox.reload();
      hideWaitLoad();
    }));
  }

  @Override
  public void onBackFromOtherScene() {
    listViewEpisodes.reload();
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
