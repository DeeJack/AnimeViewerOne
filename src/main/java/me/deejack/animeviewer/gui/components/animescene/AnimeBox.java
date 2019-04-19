package me.deejack.animeviewer.gui.components.animescene;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.controllers.AnimeDetailController;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimeBox extends VBox {
  private static final Image imageFavorite = new Image(App.class.getResourceAsStream("/assets/favorite.png"));
  private final Anime anime;
  private final Listener<Anime> onRequestAnimeTab;

  public AnimeBox(Anime anime, Listener<Anime> onRequestAnimeTab) {
    this.anime = anime;
    this.onRequestAnimeTab = onRequestAnimeTab;
    Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("AnimeBoxTooltip")));
    setUp();
  }

  public void setUp() {
    ContextMenu contextMenu = createRightClickMenu();
    setOnMouseReleased((e) -> {
      if (e.getButton() == MouseButton.PRIMARY)
        loadElement();
      else if (e.getButton() == MouseButton.MIDDLE)
        onRequestAnimeTab.onChange(anime);
      else if (e.getButton() == MouseButton.SECONDARY)
        contextMenu.show(this, e.getScreenX(), e.getScreenY());
    });
    ImageView view = createView();
    Label title = createTitle(view);
    StackPane stackPane = new StackPane(view);
    if (anime.isFavorite()) {
      stackPane.setAlignment(Pos.TOP_RIGHT);
      stackPane.getChildren().add(createImageFavorite());
    }
    getChildren().addAll(stackPane, title);
    setPrefHeight(view.getFitHeight() + title.getHeight() + 20);
    setPadding(new Insets(10, 10, 10, 10));
  }

  private ContextMenu createRightClickMenu() {
    ContextMenu contextMenu = new ContextMenu();
    if (anime.isFavorite()) {
      MenuItem item = new MenuItem(LocalizedApp.getInstance().getString("FavoriteRemoveItem"));
      registerEvents(item);
      contextMenu.getItems().add(item);
    } else {
      MenuItem item = new MenuItem(LocalizedApp.getInstance().getString("FavoriteAddItem"));
      registerEvents(item);
      contextMenu.getItems().add(item);
    }
    MenuItem item = new MenuItem(LocalizedApp.getInstance().getString("OpenInNewTab"));
    item.setOnAction((event) -> onRequestAnimeTab.onChange(anime));
    contextMenu.getItems().add(item);
    return contextMenu;
  }

  private ImageView createImageFavorite() {
    ImageView favorite = new ImageView(imageFavorite);
    favorite.setFitHeight(20);
    favorite.setPreserveRatio(true);
    return favorite;
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
        anime.toggleFavorite();
        FilesUtility.saveFavorite();
      } else anime.loadAsync(() -> {
        anime.toggleFavorite();
        FilesUtility.saveFavorite();
      });
    });
  }
}
