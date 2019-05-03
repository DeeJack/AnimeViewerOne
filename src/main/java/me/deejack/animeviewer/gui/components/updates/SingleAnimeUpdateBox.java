package me.deejack.animeviewer.gui.components.updates;

import javafx.scene.control.Label;
import me.deejack.animeviewer.gui.components.favorites.SingleFavorite;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

/**
 * GUI component that show an update from an anime
 */
public class SingleAnimeUpdateBox extends SingleFavorite {
  private final Anime anime;
  private final Episode newEpisode;

  public SingleAnimeUpdateBox(Anime anime, Episode newEpisode) {
    super(anime, anime.getAnimeInformation().getImageUrl(), null);
    this.anime = anime;
    this.newEpisode = newEpisode;
    initialize();
    setInfo();
  }

  private void initialize() {
    getRemoveButton().setDisable(true);
    getRemoveButton().setVisible(false);
    getResumeButton().setText(LocalizedApp.getInstance().getString("WatchNewEpisode"));
    getResumeButton().setOnAction((event) -> new AnimePlayer(newEpisode, anime, false, null));
  }

  private void setInfo() {
    String title = LocalizedApp.getInstance().getString("Title") + ": " + anime.getAnimeInformation().getName();
    Label labelTitle = new Label(title);
    String newEpisodeMsg = LocalizedApp.getInstance().getString("Episode") + ": " + newEpisode.getNumber();
    Label labelNewEpisode = new Label(newEpisodeMsg);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelNewEpisode);
  }
}
