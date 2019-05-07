package me.deejack.animeviewer.gui.components.animescene;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.controllers.AnimeDetailController;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimeBox extends StackPane {
  private final Anime anime;
  private final Listener<Anime> onRequestAnimeTab;
  private final Tab firstTab;
  private Point2D touchPoint;

  public AnimeBox(Anime anime, Listener<Anime> onRequestAnimeTab, Tab firstTab) {
    this.anime = anime;
    this.onRequestAnimeTab = onRequestAnimeTab;
    this.firstTab = firstTab;
    Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("AnimeBoxTooltip")));
    reload();
  }

  public void reload() {
    getChildren().clear();
    AnimeBoxImage imagePane = new AnimeBoxImage(anime);
    Label title = createTitle(imagePane.getFavoriteImageView());
    ContextMenu contextMenu = new ContextMenuAnimeBox(anime, imagePane.getFavoriteImageView(),
            AnimeBoxImage.getImageFavorite(), onRequestAnimeTab);
    registerListener(contextMenu);
    getChildren().addAll(new VBox(imagePane, title));
    setPrefHeight(imagePane.getFavoriteImageView().getFitHeight() + title.getHeight() + 20);
    setPadding(new Insets(10, 10, 10, 10));
  }

  private void registerListener(ContextMenu contextMenu) {
    setOnMousePressed((e) -> onClick(e, contextMenu));
    setOnTouchPressed((e) ->
            touchPoint = new Point2D(e.getTouchPoint().getScreenX(), e.getTouchPoint().getScreenY()));
    setOnTouchReleased((e) -> {
      if (touchPoint.getX() == e.getTouchPoint().getScreenX() && touchPoint.getY() == e.getTouchPoint().getScreenY())
        loadElement();
    });
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

  private Label createTitle(ImageView view) {
    Label title = new Label(anime.getAnimeInformation().getName());
    title.setLabelFor(view);
    title.setStyle("-fx-font: 20 arial;");
    title.setMaxWidth(250);
    return title;
  }

  private void loadElement() {
    new AnimeDetailController(anime, true, firstTab).loadAsync();
  }
}
