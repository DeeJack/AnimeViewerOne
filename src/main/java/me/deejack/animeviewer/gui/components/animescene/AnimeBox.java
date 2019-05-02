package me.deejack.animeviewer.gui.components.animescene;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.controllers.AnimeDetailController;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimeBox extends StackPane {
  private static final Image imageFavorite = new Image(App.class.getResourceAsStream("/assets/favorite.png"));
  private final Anime anime;
  private final Listener<Anime> onRequestAnimeTab;
  private ImageView favoriteImageView;

  public AnimeBox(Anime anime, Listener<Anime> onRequestAnimeTab) {
    this.anime = anime;
    this.onRequestAnimeTab = onRequestAnimeTab;
    Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("AnimeBoxTooltip")));
    reload();
  }

  private Point2D touchPoint;
  public void reload() {
    getChildren().clear();
    ContextMenu contextMenu = createRightClickMenu();
    setOnMousePressed((e) -> onClick(e, contextMenu));
    setOnTouchPressed((e) ->
            touchPoint = new Point2D(e.getTouchPoint().getScreenX(), e.getTouchPoint().getScreenY()));
    setOnTouchReleased((e) -> {
      System.out.println(touchPoint.getX() + " " + touchPoint.getY());
      System.err.println(e.getTouchPoint().getScreenX() + " " + e.getTouchPoint().getScreenY());
      if (touchPoint.getX() == e.getTouchPoint().getScreenX() && touchPoint.getY() == e.getTouchPoint().getScreenY())
        loadElement();
    });
    ImageView view = createView();
    Label title = createTitle(view);
    StackPane imagePane = new StackPane(view);
    imagePane.setAlignment(Pos.TOP_RIGHT);
    imagePane.getChildren().add(createImageFavorite());
    getChildren().addAll(new VBox(imagePane, title));
    setPrefHeight(view.getFitHeight() + title.getHeight() + 20);
    setPadding(new Insets(10, 10, 10, 10));
  }

  public void onClick(MouseEvent e, ContextMenu contextMenu) {
    if (e.isSynthesized())
      return;
    if (e.getButton() == MouseButton.PRIMARY)
      loadElement();
    else if (e.getButton() == MouseButton.MIDDLE)
      onRequestAnimeTab.onChange(anime);
    else if (e.getButton() == MouseButton.SECONDARY)
      contextMenu.show(this, e.getScreenX(), e.getScreenY());
  }

  private ContextMenu createRightClickMenu() {
    ContextMenu contextMenu = new ContextMenu();
    MenuItem item = anime.isFavorite() ?
            new MenuItem(LocalizedApp.getInstance().getString("FavoriteRemoveItem")) :
            new MenuItem(LocalizedApp.getInstance().getString("FavoriteAddItem"));
    registerEvents(item);
    contextMenu.getItems().add(item);
    MenuItem newTabItem = new MenuItem(LocalizedApp.getInstance().getString("OpenInNewTab"));
    newTabItem.setOnAction((event) -> onRequestAnimeTab.onChange(anime));
    contextMenu.getItems().add(newTabItem);
    return contextMenu;
  }

  private ImageView createImageFavorite() {
    favoriteImageView = new ImageView();
    favoriteImageView.setFitHeight(20);
    favoriteImageView.setPreserveRatio(true);
    if (anime.isFavorite())
      favoriteImageView.setImage(imageFavorite);
    return favoriteImageView;
  }

  private Label createTitle(ImageView view) {
    Label title = new Label(anime.getAnimeInformation().getName());
    title.setLabelFor(view);
    title.setStyle("-fx-font: 20 arial;");
    title.setMaxWidth(250);
    return title;
  }

  private ImageView createView() {
    ImageView view = new ImageView();
    view.setFitHeight(170);
    view.setFitWidth(250);
    Task<Image> task = SceneUtility.loadImage(anime.getAnimeInformation().getImageUrl());
    task.setOnSucceeded((value) -> view.setImage(task.getValue()));
    //view.setCache(true);
    return view;
  }

  private void loadElement() {
    new AnimeDetailController(anime, false, null).loadAsync();
  }

  private void registerEvents(MenuItem item) {
    item.setOnAction((event) -> {
      if (anime.hasBeenLoaded()) {
        onToggleFavorite(item);
      } else anime.loadAsync(() -> onToggleFavorite(item));
    });
  }

  private void onToggleFavorite(MenuItem item) {
    anime.toggleFavorite();
    FilesUtility.saveFavorite();
    favoriteImageView.setImage(anime.isFavorite() ?
            imageFavorite :
            null);
    item.setText(anime.isFavorite() ?
            LocalizedApp.getInstance().getString("FavoriteRemoveItem") :
            LocalizedApp.getInstance().getString("FavoriteAddItem"));
  }
}
