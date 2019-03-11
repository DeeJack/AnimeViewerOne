package me.deejack.animeviewer.gui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.LoadPageAsync;
import me.deejack.animeviewer.gui.components.HiddenSideBar;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeSceneController implements BaseScene {
  private final int currentPage;
  private final int initialSize;
  private final boolean isSearch;
  private final FilterList filters;
  private final String search;
  private StackPane root;
  private Pane content;
  private int elementsMultiplier = 1;
  private FlowPane elementsPane;
  private int loadedPage;
  private double startWidth;
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
    if (elements.isEmpty())
      showNotFound();
    else {
      showFound(elements, elementsMultiplier);
    }
  }

  private void showFound(List<Anime> elements, int newElementsMultiplier) {
    if (newElementsMultiplier > elementsMultiplier)
      addElements(elements, elementsMultiplier, newElementsMultiplier);
    else if (newElementsMultiplier < elementsMultiplier)
      removeElements(newElementsMultiplier);
    elementsMultiplier = newElementsMultiplier;

    List<VBox> boxes = generateAnimeBox(elements);
    elementsPane.getChildren().addAll(boxes);
    loadImages(boxes, elements);
    loadPages();
  }

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
  }

  private void loadScene() {
    root = (StackPane) SceneUtility.loadParent("/scenes/animeFlex.fxml");
    content = (Pane) ((Pane) ((ScrollPane) root.lookup("#scrollPane")).getContent()).getChildren().get(0);
    //((Pane) content.lookup("#boxFilter")).getChildren().add(SceneUtility.loadParent("/scenes/search.fxml"));
    ((ButtonBase) content.lookup("#btnBack")).setOnAction((handler) -> SceneUtility.goToPreviousScene());
    HiddenSideBar sideBar = new FilterList((Button) content.lookup("#controlSideBar")).getSideBar();
    //sideBar.setTranslateX(200);
    /*((ScrollPane) root.lookup("#scrollPane")).prefWidthProperty().addListener((event, oldValue, newValue) -> {
      System.out.println(newValue.doubleValue());
      if(oldValue.doubleValue() > 0)
        ((ScrollPane) root.lookup("#scrollPane")).setPrefWidth(oldValue.doubleValue());
    });*/
    content.widthProperty().addListener((event, oldValue, newValue) -> System.out.println(newValue.doubleValue()));
    //content.prefWidthProperty().addListener((event, oldValue, newValue) -> System.out.println(newValue.doubleValue()));
    /*sideBar.widthProperty().addListener((event, oldValue, newValue) ->
            ((Region) root.lookup("#scrollPane")).setPrefWidth(((Region) root.lookup("#scrollPane")).getPrefWidth() + newValue.doubleValue()));
    sideBar.widthProperty().addListener((event, oldValue, newValue) -> content.setPrefWidth(content.getPrefWidth() + newValue.doubleValue()));*/
    ((Pane) ((ScrollPane) root.lookup("#scrollPane")).getContent()).getChildren().add(sideBar);
    startWidth = ((ScrollPane) root.lookup("#scrollPane")).getPrefWidth();
    sideBar.translateXProperty().addListener((event, oldValue, newValue) -> ((ScrollPane) root.lookup("#scrollPane"))
            .setPrefWidth(startWidth -sideBar.getTranslateX() + 200));
    System.out.println(sideBar.getWidth());

    ComboBox<Integer> cboMultiplier = (ComboBox<Integer>) content.lookup("#cboMultiplier");
    List<Integer> multipliers = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      multipliers.add(i);
    }
    cboMultiplier.setItems(new ObservableListWrapper<>(multipliers));
    cboMultiplier.getSelectionModel().select((Integer) elementsMultiplier);
    cboMultiplier.getSelectionModel().selectedItemProperty().addListener((listener) -> {
      showFound(new ArrayList<>(), cboMultiplier.getSelectionModel().getSelectedItem());
    });
    layoutFlex();
  }

  private List<VBox> generateAnimeBox(List<? extends Anime> elements) {
    List<VBox> boxes = new ArrayList<>();
    for (Anime element : elements) {
      VBox animeBox = new VBox();
      animeBox.setOnMousePressed((e) -> {
        if (e.isPrimaryButtonDown())
          loadElement(element);
      });
      ImageView view = new ImageView();
      //imageViews.add(view);
      view.setId("imageView");
      view.setFitHeight(170);
      view.setFitWidth(250);
      view.setCache(true);
      Label title = new Label(element.getAnimeInformation().getName());
      title.setLabelFor(view);
      title.setStyle("-fx-font: 20 arial;");
      title.setMaxWidth(250);
      animeBox.getChildren().addAll(view, title);
      animeBox.setPrefHeight(view.getFitHeight() + title.getHeight() + 20);
      animeBox.setPadding(new Insets(10, 10, 10, 10));
      boxes.add(animeBox);
    }
    return boxes;
  }

  private void loadImages(List<VBox> boxes, List<? extends Anime> elements) {
    List<ImageView> views = new ArrayList<>();
    for (VBox row : boxes) {
      for (Node node : row.getChildren()) {
        if (node.getId() != null && node.getId().equals("imageView"))
          views.add((ImageView) node);
      }
    }
    if (!elements.isEmpty() && !views.isEmpty())
      loadImage(elements.get(0), views.get(0), views, elements);
  }

  private void loadPages() {
    int totalPages = App.getSite().getPages() / elementsMultiplier + ((App.getSite().getPages() % elementsMultiplier == 0) ? 0 : 1);
    HBox boxPages = (HBox) content.lookup("#hboxPages");
    boxPages.getChildren().clear();

    addPage(1, currentPage != 1, currentPage != 1, boxPages);
    addPage(currentPage - 1, true, currentPage > 2, boxPages);
    addPage(currentPage, false, true, boxPages);
    addPage(currentPage + 1, true, currentPage < totalPages - 1, boxPages);
    addPage(totalPages, true, currentPage != totalPages, boxPages);
  }

  private void addPage(int page, boolean enable, boolean visible, HBox boxPages) {
    if (!visible)
      return;
    Button button = new Button((page) + "");
    button.setDisable(!enable);
    button.setOnMousePressed((a) -> {
      if (a.isPrimaryButtonDown()) {
        showWaitAndLoad("Cambiando pagina...");
        new Thread(new LoadPageAsync(filters, search, isSearch, page + elementsMultiplier - 1, elementsMultiplier)).start();
      }
    });
    boxPages.getChildren().add(button);
  }

  /**
   * Load the image from the first to the last, I don't know if it's better this way o all at one
   * But I prefer this.
   */
  private void loadImage(Anime element, ImageView view, List<? extends ImageView> views, List<? extends Anime> elements) {
    if (element == null || view == null) {
      return;
    }
    Task<Image> task = SceneUtility.loadImage(element.getAnimeInformation().getImageUrl());
    task.setOnSucceeded((value) -> {
      view.setImage(task.getValue());
      if (elements.indexOf(element) + 1 < elements.size() || views.indexOf(view) + 1 < views.size())
        loadImage(elements.get(elements.indexOf(element) + 1), views.get(views.indexOf(view) + 1), views, elements);
    });
  }

  private void layoutFlex() {
    elementsPane = (FlowPane) content.lookup("#itemsPane");
    if (elementsPane == null)
      return;
    elementsPane.setPadding(new Insets(10, 10, 0, 10));
    elementsPane.setPrefWidth(SceneUtility.getStage().getWidth());
    root.widthProperty().addListener((observable -> elementsPane.setPrefWidth(root.getWidth())));
  }

  private void loadElement(Anime element) {
    showWaitAndLoad("Caricando anime...");
    new AnimeDetailController(element).loadAsync();
  }

  /**
   * Show the "not found" page
   */
  private void showNotFound() {
    VBox found = (VBox) content.lookup("#searchFound");
    HBox notFound = (HBox) content.lookup("#searchNotFound");
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
