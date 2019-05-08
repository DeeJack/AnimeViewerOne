package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.*;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.util.Optional;

import static me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility.findNextEpisode;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ButtonNext extends Button {
  private final Anime anime;
  private final Episode currentEpisode;
  private final boolean isNewTab;
  private final Tab currentTab;
  private SuccessListener nextEpisodeListener;

  public ButtonNext(Anime anime, Episode currentEpisode, boolean isNewTab, Tab currentTab) {
    super(">>");
    this.anime = anime;
    this.currentEpisode = currentEpisode;
    this.isNewTab = isNewTab;
    this.currentTab = currentTab;
    setEllipsisString(">>");
    setOnAction((event) -> askNextEpisode());
    setTooltip(new Tooltip(LocalizedApp.getInstance().getString("NextEpisodeTooltip")));
  }

  private void askNextEpisode() {
    Optional<Episode> nextEpisode = findNextEpisode(anime, currentEpisode);
    if (!nextEpisode.isPresent()) {
      new Alert(Alert.AlertType.NONE, LocalizedApp.getInstance().getString("AlertNoNextEpisode"), ButtonType.OK).show();
      return;
    }
    Alert alert = new Alert(Alert.AlertType.NONE, LocalizedApp.getInstance().getString("AlertNextEpisode"), ButtonType.YES, ButtonType.NO);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingNextEpisode"));
      new AnimePlayer(nextEpisode.get(), anime, isNewTab, currentTab).createStreaming((selectedLink) -> {
        if (selectedLink != null)
          nextEpisodeListener.onSuccess();
        else
          hideWaitLoad();
      });
    }
  }

  public void setOnNextEpisode(SuccessListener nextEpisodeListener) {
    this.nextEpisodeListener = nextEpisodeListener;
  }
}
