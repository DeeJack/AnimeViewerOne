package me.deejack.animeviewer.gui.components.animedetail;

import java.time.Duration;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ItemEpisode extends HBox {
  private final Episode episode;
  private final Anime anime;

  public ItemEpisode(Episode episode, Anime anime, ListView parent) {
    this.episode = episode;
    this.anime = anime;
    setWidth(530);
    setMinHeight(Double.MIN_VALUE);
    initialize(parent);
  }

  private void initialize(ListView parent) {
    HBox streaming = episode.getUrl().isEmpty() ? createNotReleased() : createStreamingDownload();
    getChildren().addAll(createTile(parent), createReleaseDate(), streaming);

    if (History.getHistory().contains(anime) && History.getHistory().get(anime).getEpisodesHistory().contains(episode)) {
      long totalSeconds = (long) History.getHistory().get(anime).getEpisodesHistory().get(History.getHistory().get(anime).getEpisodesHistory().indexOf(episode)).getSecondsWatched();
      int seconds = (int) Duration.ofSeconds(totalSeconds).getSeconds() % 60 % 60;
      int minutes = (int) Duration.ofSeconds(totalSeconds).toMinutes() % 60;
      int hours = (int) Duration.ofSeconds(totalSeconds).toHours();
      String watchedForMsg = LocalizedApp.getInstance().getString("WatchedFor");
      Label watched = new Label(String.format(watchedForMsg + ": %02d:%02d:%02d", hours, minutes, seconds));
      getChildren().add(1, watched);
      getChildren().add(2, new Label(" - "));
    }
  }

  private Label createTile(ListView parent) {
    Label title = new Label(episode.getNumber() + " - " + episode.getTitle());
    title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    parent.widthProperty().addListener((event, oldValue, newValue) -> title.setMaxWidth(newValue.doubleValue() - (250))); // 250 because.... It works
    HBox.setHgrow(title, Priority.ALWAYS);
    if (episode.getTitle().isEmpty())
      title.setText(LocalizedApp.getInstance().getString("Episode") + " " + episode.getNumber());
    return title;
  }

  public Label createReleaseDate() {
    if (episode.getReleaseDate() == null)
      return new Label();
    return new Label("[" + episode.getReleaseDate() + "] - ");
  }

  private HBox createStreamingDownload() {
    return new HBox(createStreaming(), new Label(" - "), createDownload());
  }

  private Label createStreaming() {
    Label streaming = new Label(LocalizedApp.getInstance().getString("Streaming"));
    streaming.setOnMouseClicked((ex) -> {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingEpisodeLinks"));
      new Thread(() -> {
        /*try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }*/
        AnimePlayer player = new AnimePlayer(episode, anime);
        Platform.runLater(player::createStreaming);
      }).start();
    });
    streaming.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    return streaming;
  }

  private Label createDownload() {
    Label download = new Label(LocalizedApp.getInstance().getString("Download"));
    download.setOnMouseClicked((ex) -> download());
    return download;
  }

  private HBox createNotReleased() {
    return new HBox(new Label(LocalizedApp.getInstance().getString("NotReleased")));
  }

  public void download() {
    DownloadController controller = DownloadController.getDownloadController();
    controller.singleDownload(episode, anime.getAnimeInformation().getName());
  }

  public Episode getEpisode() {
    return episode;
  }

  public Anime getAnime() {
    return anime;
  }
}
