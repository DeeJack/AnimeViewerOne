package me.deejack.animeviewer.gui.controllers.streaming.components;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ButtonNext extends Button {
  private final Anime anime;
  private final Episode currentEpisode;
  private SuccessListener nextEpisodeListener;

  public ButtonNext(Anime anime, Episode currentEpisode) {
    super(">>");
    this.anime = anime;
    this.currentEpisode = currentEpisode;
    setEllipsisString(">>");
    setOnAction((event) -> askNextEpisode());
  }

  private void askNextEpisode() {
    Optional<Episode> nextEpisode = findNextEpisode();
    if (!nextEpisode.isPresent()) {
      new Alert(Alert.AlertType.NONE, "Hai raggiunto la fine, non è presente un prossimo episodio", ButtonType.OK).show();
      return;
    }
    Alert alert = new Alert(Alert.AlertType.NONE, "Vuoi guardare il prossimo episodio?", ButtonType.YES, ButtonType.NO);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      showWaitAndLoad("Loading next episode...");
      boolean selected = new AnimePlayer(nextEpisode.get(), anime).streaming();
      if (selected) {
        nextEpisodeListener.onSuccess();
      }
      else
        hideWaitLoad();
    }
  }

  private Optional<Episode> findNextEpisode() {
    if (anime == null)
      return Optional.empty();
    if (!anime.hasBeenLoaded())
      anime.load();
    int index = anime.getEpisodes().indexOf(currentEpisode);
    if (index == anime.getEpisodes().size())
      return Optional.empty();
    return Optional.of(anime.getEpisodes().get(index + 1));
  }

  public void setOnNextEpisode(SuccessListener nextEpisodeListener) {
    this.nextEpisodeListener = nextEpisodeListener;
  }
}
