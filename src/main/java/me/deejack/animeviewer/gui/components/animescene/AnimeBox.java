package me.deejack.animeviewer.gui.components.animescene;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.controllers.AnimeDetailController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class AnimeBox extends VBox {
  private final Anime anime;

  public AnimeBox(Anime anime) {
    this.anime = anime;
    setUp();
  }

  public void setUp() {
    setOnMouseReleased((e) -> {
      if (e.getButton() == MouseButton.PRIMARY)
        loadElement();
    });
    ImageView view = createView();
    Label title = new Label(anime.getAnimeInformation().getName());
    title.setLabelFor(view);
    title.setStyle("-fx-font: 20 arial;");
    title.setMaxWidth(250);
    getChildren().addAll(view, title);
    setPrefHeight(view.getFitHeight() + title.getHeight() + 20);
    setPadding(new Insets(10, 10, 10, 10));
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
    showWaitAndLoad("Caricando anime...");
    new AnimeDetailController(anime).loadAsync();
  }
}
