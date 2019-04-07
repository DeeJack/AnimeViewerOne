package me.deejack.animeviewer.gui.components.favorites;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.controllers.AnimeDetailController;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public class SingleFavorite extends HBox {
  private final Anime anime;
  private final VBox animeInfoBox = new VBox();
  private EventHandler<ActionEvent> removeHandler;
  private Button removeButton;
  private Button resumeButton;

  public SingleFavorite(Anime anime, File imagePath) {
    this(anime, anime.getAnimeInformation().getImageUrl(), imagePath);
  }

  public SingleFavorite(Anime anime, String imageUrl, File imagePath) {
    this.anime = anime;
    initialize(imageUrl, imagePath);
  }

  private void initialize(String imageUrl, File imagePath) {
    ImageView animeImage = createImage(imageUrl, imagePath);
    removeButton = new Button(LocalizedApp.getInstance().getString("RemoveButton"));
    resumeButton = new Button(LocalizedApp.getInstance().getString("ResumeButton"));
    registerEvents(animeImage, removeButton, resumeButton);

    setSpacing(10);
    setMaxHeight(120);
    setStyle("-fx-border-color: black; -fx-border-width: 2 0 0 0");
    getChildren().addAll(animeImage, animeInfoBox, createButtonsBox(removeButton, resumeButton));
  }

  private VBox createButtonsBox(Button buttonRemove, Button buttonResume) {
    VBox buttonsBox = new VBox(buttonRemove, buttonResume);
    buttonsBox.setMinWidth(85);
    buttonsBox.setAlignment(Pos.BOTTOM_RIGHT);
    return buttonsBox;
  }

  protected ImageView createImage(String imageUrl, File imagePath) {
    ImageView animeImage = new ImageView();
    animeImage.setFitHeight(120.0);
    animeImage.setFitWidth(170.0);
    animeImage.setPickOnBounds(true);
    if (imagePath == null) {
      Task<Image> loadImage = SceneUtility.loadImage(imageUrl);
      loadImage.setOnSucceeded((value) -> animeImage.setImage(loadImage.getValue()));
    } else {
      try {
        animeImage.setImage(new Image(Files.newInputStream(Paths.get(imagePath.toURI()))));
      } catch (IOException e) {
        anime.saveImageToFile(imagePath);
        GeneralUtility.logError(e);
        return createImage(imageUrl, null);
      }
    }
    return animeImage;
  }

  private void registerEvents(ImageView animeImage, Button buttonRemove, Button buttonResume) {
    animeImage.setOnMousePressed((event) -> {
      if (event.isPrimaryButtonDown())
        new AnimeDetailController(anime, false).loadAsync();
    });
    buttonRemove.setOnAction((event) -> {
      if (removeHandler != null)
        removeHandler.handle(event);
    });
    buttonResume.setOnAction((event) -> resume());
  }

  public void setOnRemove(EventHandler<ActionEvent> removeHandler) {
    this.removeHandler = removeHandler;
  }

  public VBox getAnimeInfoBox() {
    return animeInfoBox;
  }

  private void resume() {
    if (!anime.hasBeenLoaded())
      anime.load();
    if (!History.getHistory().contains(anime)) {
      new AnimePlayer(anime.getEpisodes().get(0), anime).createStreaming();
      return;
    }
    List<Episode> episodesHistory = History.getHistory().get(anime).getEpisodesHistory();
    new AnimePlayer(episodesHistory.get(episodesHistory.size() - 1), anime).createStreaming();
  }

  public Button getRemoveButton() {
    return removeButton;
  }

  public Button getResumeButton() {
    return resumeButton;
  }
}
