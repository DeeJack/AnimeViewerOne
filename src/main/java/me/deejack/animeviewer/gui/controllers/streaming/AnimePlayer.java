package me.deejack.animeviewer.gui.controllers.streaming;

import java.io.IOException;
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

  public void createStreaming(WebBypassUtility.CallBack<StreamingLink> onSelection) {
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
    Connection.Response response = ConnectionUtility.connect(link, false);
    if (response == null)
      return;
    if (link.contains("dreamsub")) {
      setRoot(setupPlayer(link));
    } else {
      WebBypassUtility.getOpenloadLink(link, "https://openload.co", (resultLink) -> setRoot(setupPlayer(resultLink)));
    }
  }

  private StreamingController setupPlayer(String link) {
    StreamingController streaming = new StreamingController(new MediaPlayer(new Media(link)), episode, anime);
    streaming.setUpPlayer();
    return streaming;
  }
}