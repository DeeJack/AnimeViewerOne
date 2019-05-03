package me.deejack.animeviewer.gui.controllers;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.animescene.AnimePane;
import me.deejack.animeviewer.gui.components.animescene.PagesBox;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.gui.components.general.HiddenSideBar;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.dto.KeyValuePair;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.defaultsources.dreamsub.DreamsubAnime;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.List;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeSceneController implements BaseScene {
  private final boolean isSearch;
  private final HiddenSidebarBuilder filters;
  private final String search;
  private final TabPane root;
  private AnimePane animePane;
  private int currentPage;

  public AnimeSceneController(List<Anime> elements, int page, boolean isSearch, HiddenSidebarBuilder filters, String search) {
    elements.add(new DreamsubAnime("Test", "https://www.dreamsub.stream/anime/test", "https://www.dreamsub.stream/anime/test"));
    currentPage = page;
    this.isSearch = isSearch;
    this.filters = filters;
    this.search = search;
    root = (TabPane) SceneUtility.loadParent("/scenes/animeFlex.fxml");

    initialize(elements);
    setRoot(this);
  }

  private void initialize(List<Anime> elements) {
    setUpScene();
    if (elements.isEmpty()) {
      showNotFound();
      return;
    }
    showFound(elements);
    hideWaitLoad();
  }

  private void setUpScene() {
    Pane browsePane = ((Pane) root.getTabs().get(0).getContent());
    BorderPane content = (BorderPane) browsePane.getChildren().get(0);
    HiddenSideBar sideBar = new HiddenSidebarBuilder()
            .setControlButton((HBox) content.getTop().lookup("#controlSideBar"))
            .setPreviousFilter(filters).build();
    browsePane.getChildren().add(sideBar);
    ((TextInputControl) root.lookup("#txtSearch")).setText(search);
  }

  private void showFound(List<Anime> elements) {
    Pane browsePane = ((Pane) root.getTabs().get(0).getContent());
    BorderPane content = (BorderPane) browsePane.getChildren().get(0);
    VBox found = (VBox) ((ScrollPane) content.lookup("#scrollPane")).getContent();
    PagesBox pagesBox = new PagesBox(currentPage, search, isSearch, filters, onPageChanged(found));
    found.getChildren().add(pagesBox);
    animePane = new AnimePane(elements, onRequestAnimeTab());
    found.getChildren().add(0, animePane);
  }

  private Listener<Anime> onRequestAnimeTab() {
    return (anime) -> {
      Tab tab = new Tab();
      AnimeDetailController detailController = new AnimeDetailController(anime, true, tab);
      detailController.loadSync();
      tab.setText(detailController.getTitle());
      tab.setContent(detailController.getRoot());
      root.getTabs().add(tab);
    };
  }

  private Listener<KeyValuePair<Integer, List<Anime>>> onPageChanged(VBox foundBox) {
    return (keyValuePair) -> {
      currentPage = keyValuePair.getKey();
      foundBox.getChildren().clear();
      initialize(keyValuePair.getValue());
    };
  }

  /**
   * Show the "not found" page if there aren't anime to show
   */
  private void showNotFound() {
    Pane browsePane = ((Pane) root.getTabs().get(0).getContent());
    BorderPane content = (BorderPane) browsePane.getChildren().get(0);
    ScrollPane found = (ScrollPane) content.getCenter().lookup("#scrollPane");
    HBox notFound = (HBox) content.getCenter().lookup("#searchNotFound");
    found.setVisible(false);
    notFound.setVisible(true);
  }

  @Override
  public void onBackFromOtherScene() {
    animePane.reload();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return LocalizedApp.getInstance().getString("BrowseWindowTitle");
  }

  @Override
  public String getName() {
    return "AnimeScene";
  }
}
