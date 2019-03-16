package me.deejack.animeviewer.gui.controllers.streaming;

import java.io.IOException;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.controllers.download.DownloadUtility;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimePlayer {
  private final Episode episode;
  private final Anime anime;

  public AnimePlayer(Episode episode, Anime anime) {
    this.episode = episode;
    this.anime = anime;
  }

  public AnimePlayer(String link) {
    this(null, null);
    showWaitAndLoad(LocalizedApp.getInstance().getString("Loading"));
    extractVideo(link);
  }

  public boolean streaming() {
    String link = "";
    try {
      StreamingLink choice = DownloadUtility.chooseSource(episode);
      if (choice == null)
        return false;
      link = choice.getLink();
    } catch (IOException e) {
      handleException(e);
    }
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingStreaming"));
    extractVideo(link);
    //showWaitAndLoad("Loading...");
    return true;
  }

  private void extractVideo(String link) {
    try {
      new URL(link);
    } catch (IOException invalidUrl) {
      handleException(new Exception(LocalizedApp.getInstance().getString("ExceptionInvalidUrl") + " " + link));
      return;
    }
    if (link.contains("openload") || link.contains("streamango")) {
      WebBypassUtility.getOpenloadLink(link, "https://openload.co", (resultLink) -> setRoot(setupPlayer(resultLink)));
    } else {
      setRoot(setupPlayer(link));
    }
  }

  private StreamingController setupPlayer(String link) {
    StreamingController streaming = new StreamingController(new MediaPlayer(new Media(link)), episode, anime);
    streaming.setUpPlayer();
    return streaming;
  }
}