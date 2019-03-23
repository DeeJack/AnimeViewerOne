package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.Gson;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class Github {
  private static final String REPOSITORY_NAME = "AnimeViewerOne";
  private static final String USER_NAME = "DeeJack";
  /*private static final String REPOSITORY_NAME = "guice";
  private static final String USER_NAME = "google";*/
  private static final String API_URL = "https://api.github.com/repos/" + USER_NAME + "/" + REPOSITORY_NAME + "/releases/latest";

  public static void main(String[] args) {
    Github github = new Github();
    //github.update(new File("D:\\Cartelle\\Desktop\\JavaVersions\\animeviewer.jar"));
    github.checkUpdates();
    //System.out.println(System.getProperty("java.io.tmpdir"));
  }

  public void checkUpdates() {
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
        Asset asset = release.getAssets()[0];
        // File output = new File(GeneralUtility.TMP_PATH + File.separator + "animeviewer by DeeJack v" + release.getVersion() + ".jar"); Se rimane nella temp path è un problema perché viene cancellato
        File output = new File("D:\\Cartelle\\Desktop\\JavaVersions\\animeviewer.jar");
        DownloadAsync downloadAsync = new DownloadAsync(output, asset.getDownloadLink());
        new Thread(downloadAsync).start();
        downloadAsync.addSuccessListener(() -> update(output));
        // downloadAsync.addFailListener((SceneUtility::handleException));
      }
    } catch (IOException e) {
      handleException(e);
    }
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
