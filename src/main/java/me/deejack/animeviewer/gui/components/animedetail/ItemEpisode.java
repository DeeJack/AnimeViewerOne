package me.deejack.animeviewer.gui.components.animedetail;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ItemEpisode extends HBox {
  private final Episode episode;
  private final Anime anime;

  public ItemEpisode(Episode episode, Anime anime) {
    this.episode = episode;
    this.anime = anime;
  }

  private void initialize() {
    HBox streaming = episode.getUrl().isEmpty() ? createNotReleased() : createStreamingDownload();
    getChildren().addAll(createTile(), createReleaseDate(), streaming);
  }

  private Label createTile() {

  }

  public Label createReleaseDate() {
    return new Label("[" + episode.getReleaseDate() + "]");
  }

  private HBox createStreamingDownload() {
    return new HBox(createStreaming(), new Label(" - "), createDownload())
  }

  private Label createStreaming() {
    Label streaming = new Label("Streaming");
    streaming.setOnMouseClicked((ex) -> {
      showWaitAndLoad("Caricando link");
      new AnimePlayer(episode, anime).streaming();
    });
    streaming.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    return streaming;
  }

  private Label createDownload() {

  }

  private HBox createNotReleased() {

  }
}
