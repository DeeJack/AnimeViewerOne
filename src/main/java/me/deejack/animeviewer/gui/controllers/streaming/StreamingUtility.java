package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.components.streaming.bottombar.ButtonPause;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;
import me.deejack.animeviewer.logic.history.HistoryEpisode;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.time.LocalDateTime;
import java.util.Optional;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public final class StreamingUtility {

  private StreamingUtility() {
  }

  public static void keyNavigation(KeyEvent keyEvent, MediaPlayer mediaPlayer) {
    switch (keyEvent.getCode()) {
      case UP:
        if (mediaPlayer.getVolume() < 0.55)
          mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.05);
        break;
      case DOWN:
        if (mediaPlayer.getVolume() > 0.05)
          mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.05);
        break;
      case RIGHT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(4)));
        break;
      case LEFT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(4)));
        break;
      case F11:
        SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
        break;
      case SPACE:
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
          mediaPlayer.pause();
        else
          mediaPlayer.play();
        break;
      default:
        keyEvent.consume();
        break;
    }
  }

  public static void onChangeStatus(MediaPlayer.Status status, ButtonPause buttonPause) {
    System.err.println(status);
    switch (status) {
      case PLAYING:
        buttonPause.setText("| |");
        break;
      case UNKNOWN:
      case STALLED:
        buttonPause.setText(">");
        showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingVideo"));
        break;
      case READY:
        hideWaitLoad();
        break;
      case PAUSED:
      case STOPPED:
        buttonPause.setText(">");
        break;
      case HALTED:
        Alert alert = new Alert(Alert.AlertType.ERROR,
                LocalizedApp.getInstance().getString("AlertErrorOnStreaming"),
                ButtonType.OK);
        alert.showAndWait();
    }
  }

  public static Optional<Episode> findNextEpisode(Anime anime, Episode currentEpisode) {
    if (anime == null)
      return Optional.empty();
    if (!anime.hasBeenLoaded())
      anime.load();
    int index = anime.getEpisodes().indexOf(currentEpisode);
    if (index == anime.getEpisodes().size() || anime.getEpisodes().get(index + 1) == null)
      return Optional.empty();
    return Optional.of(anime.getEpisodes().get(index + 1));
  }

  public static void addEpisodeToHistory(Anime anime, Episode episode) {
    if (anime == null)
      return;
    Optional<HistoryElement> historyElement = History.getHistory().get(anime);
    if (historyElement.isPresent()) {
      if (historyElement.get().contains(episode))
        History.getHistory().getHistoryEpisode(historyElement.get(), episode)
                .ifPresent((historyEpisode -> episode.setSecondsWatched(historyEpisode.getEpisode().getSecondsWatched())));
      else historyElement.get().addEpisode(new HistoryEpisode(episode, LocalDateTime.now()));
    } else
      History.getHistory().add(new HistoryElement(anime, new HistoryEpisode(episode, LocalDateTime.now())));
    FilesUtility.saveHistory();
  }
}
