package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.controllers.download.DownloadUtility;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

import java.io.IOException;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimePlayer {
  private final Episode episode;
  private final Anime anime;
  private final boolean isNewTab;
  private final Tab currentTab;

  public AnimePlayer(Episode episode, Anime anime, boolean isNewTab, Tab currentTab) {
    this.episode = episode;
    this.anime = anime;
    this.isNewTab = isNewTab;
    this.currentTab = currentTab;
  }

  public AnimePlayer(String link) {
    this(null, null, false, null);
    showWaitAndLoad(LocalizedApp.getInstance().getString("Loading"));
    extractVideo(link);
  }

  public void createStreaming(WebBypassUtility.Callback<StreamingLink> onSelection) {
    try {
      DownloadUtility.chooseSource(episode, (selectedLink) -> {
        onSelection.onSuccess(selectedLink);
        if (selectedLink == null)
          return;
        showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingStreaming"));
        extractVideo(selectedLink.getLink());
      });
    } catch (IOException e) {
      handleException(e);
    }
  }

  public void createStreaming() {
    createStreaming((link) -> {
    });
  }

  private void extractVideo(String link) {
    Connection.Response response = ConnectionUtility.connect(link, true);
    if (response == null)
      return;
    if (response.contentType().contains("video")) {
      setUpStreamingScene(link);
    } else
      WebBypassUtility.bypassSite(link, this::setUpStreamingScene);
  }

  private void setUpStreamingScene(String link) {
    StreamingController streaming = setupPlayer(link);
    if (isNewTab) {
      Node prevContent = currentTab.getContent();
      streaming.setOnBack((event) -> {
        streaming.onFinish();
        currentTab.setContent(prevContent);
      });
      currentTab.setOnCloseRequest((event) -> streaming.onFinish());
      currentTab.setContent(streaming.getRoot());
      streaming.getRoot().setMinWidth(StackPane.USE_PREF_SIZE);
      streaming.getRoot().setMinHeight(StackPane.USE_PREF_SIZE);
      StackPane header = (StackPane) currentTab.getTabPane().lookup(".tab-header-area");
      streaming.getRoot().prefHeightProperty().bind(currentTab.getTabPane().heightProperty().subtract(header.getHeight()));
      streaming.getRoot().prefWidthProperty().bind(currentTab.getTabPane().widthProperty());
    } else
      setRoot(streaming);
  }

  private StreamingController setupPlayer(String link) {
    StreamingController streaming = new StreamingController(new MediaPlayer(new Media(link)), episode, anime);
    streaming.setUpPlayer(isNewTab, currentTab);
    return streaming;
  }
}