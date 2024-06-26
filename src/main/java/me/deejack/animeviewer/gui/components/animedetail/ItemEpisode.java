package me.deejack.animeviewer.gui.components.animedetail;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryEpisode;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.time.Duration;

public class ItemEpisode extends HBox {
  private final Episode episode;
  private final Anime anime;

  public ItemEpisode(Episode episode, Anime anime, ListView parent, Listener<Episode> onRequestStreaming) {
    this.episode = episode;
    this.anime = anime;
    setWidth(530);
    setMinHeight(Double.MIN_VALUE);
    initialize(parent, onRequestStreaming);
  }

  private void initialize(ListView parent, Listener<Episode> onRequestStreaming) {
    HBox streaming = episode.getUrl().isEmpty() ? createNotReleased() : createStreamingDownload(onRequestStreaming);
    getChildren().addAll(createTile(parent), createReleaseDate(), streaming);

    if (History.getHistory().contains(anime) && History.getHistory().get(anime).get().contains(episode)) {
      HistoryEpisode episode = History.getHistory().getLastEpisodeOf(anime).get();
      long totalSeconds = (long) episode.getEpisode().getSecondsWatched();
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

  private HBox createStreamingDownload(Listener<Episode> onStreamingRequested) {
    return new HBox(createStreaming(onStreamingRequested), new Label(" - "), createDownload());
  }

  private Label createStreaming(Listener<Episode> onRequestStreaming) {
    Label streaming = new Label(LocalizedApp.getInstance().getString("Streaming"));
    streaming.setOnMouseClicked((ex) -> onRequestStreaming.onChange(episode));
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
