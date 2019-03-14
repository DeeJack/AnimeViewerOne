package me.deejack.animeviewer.gui.components.animedetail;

import java.time.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ItemEpisode extends HBox {
  private final Episode episode;
  private final Anime anime;
  private final ListView parent;

  public ItemEpisode(Episode episode, Anime anime, ListView parent) {
    this.episode = episode;
    this.anime = anime;
    this.parent = parent;
    setWidth(530);
    initialize();
  }

  private void initialize() {
    HBox streaming = episode.getUrl().isEmpty() ? createNotReleased() : createStreamingDownload();
    getChildren().addAll(createTile(), createReleaseDate(), streaming);

    if (History.getHistory().contains(anime) && History.getHistory().get(anime).getEpisodesHistory().contains(episode)) {
      long totalSeconds = (long) History.getHistory().get(anime).getEpisodesHistory().get(History.getHistory().get(anime).getEpisodesHistory().indexOf(episode)).getSecondsWatched();
      int seconds = (int) Duration.ofSeconds(totalSeconds).getSeconds() % 60 % 60;
      int minutes = (int) Duration.ofSeconds(totalSeconds).toMinutes() % 60;
      int hours = (int) Duration.ofSeconds(totalSeconds).toHours();
      Label watched = new Label(String.format("Visto per: %02d:%02d:%02d", hours, minutes, seconds));
      getChildren().add(1, watched);
      getChildren().add(2, new Label(" - "));
    }
  }

  private Label createTile() {
    Label title = new Label(episode.getNumber() + " - " + episode.getTitle());
    title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    parent.widthProperty().addListener((event, oldValue, newValue) -> title.setMaxWidth(newValue.doubleValue() - (250)));
    HBox.setHgrow(title, Priority.ALWAYS);
    if (episode.getTitle().isEmpty())
      title.setText("Episodio " + episode.getNumber());
    return title;
  }

  public Label createReleaseDate() {
    return new Label("[" + episode.getReleaseDate() + "] - ");
  }

  private HBox createStreamingDownload() {
    return new HBox(createStreaming(), new Label(" - "), createDownload());
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
    Label download = new Label("Download");
    download.setOnMouseClicked((ex) -> download(episode));
    return download;
  }

  private HBox createNotReleased() {
    return new HBox(new Label("[Non ancora disponibile]"));
  }

  private void download(Episode episode) {
    DownloadController controller = DownloadController.getDownloadController();
    controller.singleDownload(episode, anime.getAnimeInformation().getName());
  }
}
