package me.deejack.animeviewer.gui.controllers.streaming;

import java.util.Optional;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public final class StreamingUtility {
  public static void onFinish(Anime anime, MediaPlayer mediaPlayer, ControlsLayerTask cursorTask) {
    /*Optional<Episode> nextEpisode = findNextEpisode();
    if (!nextEpisode.isPresent()) {
      new Alert(Alert.AlertType.NONE, "Hai raggiunto la fine, non è presente un prossimo episodio", ButtonType.OK).show();
      return;
    }
    Alert alert = new Alert(Alert.AlertType.NONE, "Vuoi guardare il prossimo episodio?", ButtonType.YES, ButtonType.NO);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      cursorTask.setInterrupted(true);
      mediaPlayer.dispose();
      showWaitAndLoad("Loading next episode...");
      boolean selected = new AnimePlayer(nextEpisode.get(), anime).streaming();
      if (!selected) {
        hideWaitLoad();
        close(anime, cursorTask, mediaPlayer);
      }
    }*/
  }

  public static Optional<Episode> findNextEpisode() {
    return Optional.empty();
  }

  public static void close(Anime anime, ControlsLayerTask cursorTask, MediaPlayer mediaPlayer) {
  }
}
