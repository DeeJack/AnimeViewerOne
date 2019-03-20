package me.deejack.animeviewer.gui.controllers;

import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.animescene.AnimePane;
import me.deejack.animeviewer.gui.components.animescene.PagesBox;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.components.general.HiddenSideBar;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeSceneController implements BaseScene {
  private final int initialSize;
  private final boolean isSearch;
  private final FilterList filters;
  private final String search;
  private int currentPage;
  private TabPane root;
  private FlowPane elementsPane;
  private AnimePane animePane;

  public AnimeSceneController(List<Anime> elements, boolean isSearch, FilterList filters, String search) {
    this(elements, 1, isSearch, filters, search);
  }

  public AnimeSceneController(List<Anime> elements, int page, boolean isSearch, FilterList filters, String search) {
    currentPage = page;
    initialSize = elements.size();
    this.isSearch = isSearch;
    this.filters = filters;
    this.search = search;

    hideWaitLoad();
    initialize(elements);
    setRoot(this);
  }

  private void initialize(List<Anime> elements) {
    loadScene();
    if (elements.isEmpty()) {
      showNotFound();
      return;
    }
    showFound(elements);
  }

  private void loadScene() {
    root = (TabPane) SceneUtility.loadParent("/scenes/animeFlex.fxml");
    Pane browsePane = ((Pane) root.getTabs().get(0).getContent());
    BorderPane content = (BorderPane) browsePane.getChildren().get(0);
    HiddenSideBar sideBar = new FilterList((Button) content.getTop().lookup("#controlSideBar"), filters).getSideBar();
    browsePane.getChildren().add(sideBar);
    ((TextInputControl) root.lookup("#txtSearch")).setText(search);
  }

  private void showFound(List<Anime> elements) {
    Pane browsePane = ((Pane) root.getTabs().get(0).getContent());
    BorderPane content = (BorderPane) browsePane.getChildren().get(0);
    VBox found = (VBox) ((ScrollPane) content.lookup("#scrollPane")).getContent();
    PagesBox pagesBox = new PagesBox(currentPage, search, isSearch, filters);
    found.getChildren().add(pagesBox);
    animePane = new AnimePane(elements, (anime) -> {
      AnimeDetailController detailController = new AnimeDetailController(anime, true);
      detailController.loadSync();
      root.getTabs().add(new Tab(detailController.getTitle(), detailController.getRoot()));
    });
    found.getChildren().add(0, animePane);
  }

  /**
   * Show the "not found" page
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

  public FlowPane getElementsPane() {
    return elementsPane;
  }

  public int getInitialSize() {
    return initialSize;
  }

  public int addAndGetCurrentPage() {
    return ++currentPage;
  }

  public boolean isSearch() {
    return isSearch;
  }

  public FilterList getFilters() {
    return filters;
  }

  public String getSearch() {
    return search;
  }

  public void addElements(List<Anime> animeList) {
    animePane.addElements(animeList);
  }
}
