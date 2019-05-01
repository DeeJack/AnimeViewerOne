package me.deejack.animeviewer.gui.controllers.download;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.image.Image;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.components.download.DownloadsWindow;
import me.deejack.animeviewer.gui.components.download.SingleDownload;
import me.deejack.animeviewer.gui.utils.WebBypassUtility;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

/**
 * Number of time this class has been rewritten: 3
 */
public final class DownloadController {
  private static final DownloadController downloadController = new DownloadController();
  private final Image imageCancel;
  private final Image imageRestart;

  private DownloadController() {
    imageCancel = new Image(App.class.getResourceAsStream("/assets/cancel.png"));
    imageRestart = new Image(App.class.getResourceAsStream("/assets/resume2.png"));
  }

  public static DownloadController getDownloadController() {
    return downloadController;
  }

  public void addDownloads(List<Episode> episodes, String animeName) {
    int prefResolution = DownloadUtility.choseDownloadSettings();
    AtomicReference<DownloadAsync> previousDownload = new AtomicReference<>();
    if (prefResolution == -1)
      return;
    File destination = DownloadUtility.saveDirectory();
    if (destination == null)
      return;
    AtomicInteger count = new AtomicInteger(0);

    chooseLink(episodes, count, prefResolution, animeName, destination);
  }

  private void chooseLink(List<Episode> episodes, AtomicInteger count, int prefResolution, String animeName, File destination) {
    List<StreamingLink> links = episodes.get(count.get()).getStreamingLinks();
    if (links.isEmpty())
      return;
    AtomicReference<StreamingLink> link = new AtomicReference<>(links.get(0));
    links.forEach((streamingLink -> {
      if (Math.abs(streamingLink.getResolution() - prefResolution) < Math.abs(link.get().getResolution() - prefResolution))
        link.set(streamingLink);
    }));
    processLink(link.get(), (resultLink) -> startDownload(resultLink, episodes.get(count.get()), animeName, destination, () -> {
      chooseLink(episodes, new AtomicInteger(count.addAndGet(1)), prefResolution, animeName, destination);
    }));
  }

  public void singleDownload(Episode episode, String animeName) {
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingStartDownload"));
    try {
      DownloadUtility.chooseSource(episode, (downloadLink) -> {
        if (downloadLink == null)
          return;
        File destination = DownloadUtility.savePath(episode.getTitle());
        if (destination == null)
          return;
        processLink(downloadLink, (resultLink) -> startDownload(resultLink, episode, animeName, destination, () -> {
        }));
      });
    } catch (IOException e) {
      handleException(e);
    }
  }

  private void processLink(StreamingLink downloadLink, WebBypassUtility.Callback<String> callBack) {
    Connection.Response response = ConnectionUtility.connect(downloadLink.getLink(), false);
    if (response.contentType().contains("video")) {
      callBack.onSuccess(downloadLink.getLink());
    } else
      WebBypassUtility.bypassSite(downloadLink.getLink(), callBack);
  }

  private void startDownload(String downloadLink, Episode episode, String animeName, File destination, SuccessListener finishListener) {
    if (destination.isDirectory())
      destination = new File(destination.getPath() + File.separator + animeName + " - " + episode.getNumber() + ".mp4");

    DownloadsWindow.getInstance().addDownload(new SingleDownload(destination, downloadLink));
    System.out.println("DOWNLOADING " + downloadLink);
    if (!DownloadsWindow.getInstance().getStage().isShowing()) {
      DownloadsWindow.getInstance().getStage().show();
      hideWaitLoad();
    }
  }

  /*private void toggleDownload(DownloadAsync downloadAsync, ImageView toggleDownload) {
    downloadAsync.setCancelled(!downloadAsync.getCancelled());
    if (downloadAsync.getCancelled()) {
      toggleDownload.setImage(imageRestart);
      return;
    }
    new Thread(downloadAsync).start();
    toggleDownload.setImage(imageCancel);
  }*/
}
