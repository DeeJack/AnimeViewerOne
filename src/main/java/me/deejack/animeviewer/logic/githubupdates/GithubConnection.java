package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.Gson;
import javafx.application.Platform;
import me.deejack.animeviewer.gui.components.appupdates.UpdateAvailableDialog;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class GithubConnection {
  /*private static final String REPOSITORY_NAME = "AnimeViewerOne";
  private static final String USER_NAME = "DeeJack";*/
  private static final String REPOSITORY_NAME = "HMCL";
  private static final String USER_NAME = "huanghongxun";
  private static final String API_URL = "https://api.github.com/repos/" + USER_NAME + "/" + REPOSITORY_NAME + "/releases/latest";

  public void checkUpdatesAsync() {
    new Thread((() -> {
      try {
        Connection.Response response = Jsoup.connect(API_URL)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute();
        String body = response.body();
        System.out.println(body);
        Gson gson = new Gson();
        Release release = gson.fromJson(body, Release.class);
        if (!release.getVersion().equalsIgnoreCase(GeneralUtility.version)) {
          Platform.runLater(() -> new UpdateAvailableDialog(release).showAndWait());
        }
      } catch (IOException e) {
        handleException(e);
      }
    })).start();
  }

  private void update(File file) {
    try {
      Desktop.getDesktop().open(file);
      System.exit(0);
    } catch (IOException e) {
      handleException(e);
    }
  }
}
