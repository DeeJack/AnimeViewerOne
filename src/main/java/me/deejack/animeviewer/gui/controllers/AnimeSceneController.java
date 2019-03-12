package me.deejack.animeviewer.gui.controllers;

import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.components.HiddenSideBar;
import me.deejack.animeviewer.gui.components.animescene.AnimePane;
import me.deejack.animeviewer.gui.components.animescene.PagesBox;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeSceneController implements BaseScene {
  private final int currentPage;
  private final int initialSize;
  private final boolean isSearch;
  private final FilterList filters;
  private final String search;
  private StackPane root;
  //private Pane content;
  private int elementsMultiplier = 1;
  private FlowPane elementsPane;
  private int loadedPage;
  //private final List<ImageView> imageViews = new ArrayList<>();

  public AnimeSceneController(List<Anime> elements, int elementsMultiplier, boolean isSearch, FilterList filters, String search) {
    this(elements, 1, elementsMultiplier, isSearch, filters, search);
  }

  public AnimeSceneController(List<Anime> elements, int page, int elementsMultiplier, boolean isSearch, FilterList filters, String search) {
    currentPage = page;
    loadedPage = page;
    initialSize = elements.size();
    this.isSearch = isSearch;
    this.filters = filters;
    this.search = search;

    hideWaitLoad();
    initialize(elements, elementsMultiplier);
    this.elementsMultiplier = elementsMultiplier;
    setRoot(this);
  }

  private void initialize(List<Anime> elements, int elementsMultiplier) {
    loadScene();
    if (elements.isEmpty()) {
      showNotFound();
      return;
    }
    showFound(elements, elementsMultiplier);
  }

  private void showFound(List<Anime> elements, int newElementsMultiplier) {
/*
    if (newElementsMultiplier > elementsMultiplier)
      addElements(elements, elementsMultiplier, newElementsMultiplier);
    else if (newElementsMultiplier < elementsMultiplier)
      removeElements(newElementsMultiplier);
    elementsMultiplier = newElementsMultiplier;*/

    BorderPane content = (BorderPane) root.getChildrenUnmodifiable().get(0);
    VBox found = (VBox) ((ScrollPane) content.lookup("#scrollPane")).getContent();
    AnimePane animePane = new AnimePane(elements);
    found.getChildren().add(0, animePane);
    animePane.load();
    //List<VBox> boxes = generateAnimeBox(elements);
    //elementsPane.getChildren().addAll(boxes);
    //loadImages(boxes, elements);
    //loadPages();
  }
/*
  private void removeElements(int newElementsMultiplier) {
    if (elementsPane.getChildren().size() < initialSize * newElementsMultiplier)
      return;
    elementsPane.getChildren().remove(initialSize * newElementsMultiplier, elementsPane.getChildren().size());
    loadedPage -= newElementsMultiplier;
  }

  private void addElements(List<? super Anime> elements, int oldElementsMultiplier, int newElementsMultiplier) {
    for (int i = oldElementsMultiplier; i < newElementsMultiplier; i++) {
      List<Anime> animeList = isSearch ? App.getSite().filter(filters, ++loadedPage) : App.getSite().searchAnime(search, ++loadedPage);
      elements.addAll(animeList);
    }
  }*/

  private void loadScene() {
    root = (StackPane) SceneUtility.loadParent("/scenes/animeFlex.fxml");
    BorderPane content = (BorderPane) root.getChildrenUnmodifiable().get(0);
    HiddenSideBar sideBar = new FilterList((Button) content.getTop().lookup("#controlSideBar")).getSideBar();
    root.getChildren().add(sideBar);

    VBox found = (VBox) ((ScrollPane) content.getCenter().lookup("#scrollPane")).getContent();
    found.getChildren().add(new PagesBox(currentPage, elementsMultiplier, search, isSearch, filters));
    /*cboMultiplier.getSelectionModel().selectedItemProperty().addListener((listener) -> {
      showFound(new ArrayList<>(), cboMultiplier.getSelectionModel().getSelectedItem());
    });*/
  }

  /*private void loadImages(List<VBox> boxes, List<? extends Anime> elements) {
    List<ImageView> views = new ArrayList<>();
    for (VBox row : boxes) {
      for (Node node : row.getChildren()) {
        if (node.getId() != null && node.getId().equals("imageView"))
          views.add((ImageView) node);
      }
    }
    if (!elements.isEmpty() && !views.isEmpty())
      loadImage(elements.get(0), views.get(0), views, elements);
  }*/

  /**
   * Load the image from the first to the last, I don't know if it's better this way o all at one
   * But I prefer this.
   */
  /*private void loadImage(Anime element, ImageView view, List<? extends ImageView> views, List<? extends Anime> elements) {
    if (element == null || view == null) {
      return;
    }
    Task<Image> task = SceneUtility.loadImage(element.getAnimeInformation().getImageUrl());
    task.setOnSucceeded((value) -> {
      view.setImage(task.getValue());
      if (elements.indexOf(element) + 1 < elements.size() || views.indexOf(view) + 1 < views.size())
        loadImage(elements.get(elements.indexOf(element) + 1), views.get(views.indexOf(view) + 1), views, elements);
    });
  }*//*
  private void layoutFlex() {
    elementsPane = (FlowPane) content.lookup("#itemsPane");
    if (elementsPane == null)
      return;
    elementsPane.setPadding(new Insets(10, 10, 0, 10));
    elementsPane.setPrefWidth(SceneUtility.getStage().getWidth());
    root.widthProperty().addListener((observable -> elementsPane.setPrefWidth(root.getWidth())));
  }*/

  /**
   * Show the "not found" page
   */
  private void showNotFound() {
    BorderPane content = (BorderPane) root.getChildrenUnmodifiable().get(0);
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
    return "Naviga";
  }

  @Override
  public String getName() {
    return "AnimeScene";
  }
}
