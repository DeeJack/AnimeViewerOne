package me.deejack.animeviewer.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.deejack.animeviewer.gui.components.appupdates.UpdateAvailableDialog;
import me.deejack.animeviewer.gui.connection.CustomConnection;
import me.deejack.animeviewer.gui.scenes.EventHandler;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.defaultsources.animeleggendari.AnimeLeggendariSource;
import me.deejack.animeviewer.logic.defaultsources.dreamsub.DreamSubSource;
import me.deejack.animeviewer.logic.extensions.ExtensionLoader;
import me.deejack.animeviewer.logic.githubupdates.Asset;
import me.deejack.animeviewer.logic.githubupdates.Release;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class App extends Application {
  public static final List<FilteredSource> SITES = new ArrayList<>();
  private static FilteredSource site;

  public static void main(String[] args) {
    launch(args);
  }

  public static FilteredSource getSite() {
    return site;
  }

  public static void setSite(FilteredSource site) {
    App.site = site;
  }

  @Override
  public void start(Stage primaryStage) {
    /*try {
      Connection.Response response = Jsoup.connect("https://api.github.com/repos/DeeJack/AnimeViewerOne/releases/latest").ignoreContentType(true).method(Connection.Method.GET).execute();
      String responseJson = response.body();
      new GsonBuilder().generateNonExecutableJson().create().fromJson();
    } catch (IOException e) {
      e.printStackTrace();
    }*/
    ConnectionUtility.setSiteConnection(new CustomConnection());
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handleException(throwable));
    SceneUtility.setStage(primaryStage);
    primaryStage.setScene(new Scene(new StackPane(), 1000, 720));
    primaryStage.show();
    primaryStage.getScene().setOnKeyPressed(EventHandler::onKeyEvent);
    primaryStage.getScene().setOnMouseClicked(EventHandler::onMouseEvent);
    primaryStage.setOnCloseRequest((event) -> EventHandler.onCloseRequest());
    showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingExt"));
    SITES.addAll(ExtensionLoader.loadExtension());
    SITES.add(new DreamSubSource());
    SITES.add(new AnimeLeggendariSource());
    hideWaitLoad();
    primaryStage.setScene(new Scene(SceneUtility.loadParent("/scenes/select.fxml")));
    primaryStage.setTitle(LocalizedApp.getInstance().getString("SelectWindowTitle"));
    new UpdateAvailableDialog(new Release("asd.com", "v1.4.5alpha", new Asset[]{new Asset("dsa.com", "asd.com/qwe.jar")}, "Changelog bello")).show();

    // AGGIUNGERE GLI ANIME DESERIALIZZATI DAL FILE PIU AGGIORNATO
    File history = new File(System.getProperty("user.home") + File.separator + ".animeviewer" + File.separator + "history.json");
    File lastHistory;
    if (history.exists()) {
      lastHistory = history;
      if (FilesUtility.tempHistory.exists() && history.lastModified() < FilesUtility.tempHistory.lastModified())
        lastHistory = FilesUtility.tempHistory;
    } else if (FilesUtility.tempHistory.exists()) {
      lastHistory = FilesUtility.tempHistory;
    } else {
      lastHistory = null;
    }
    if (lastHistory != null) {
      FilesUtility.loadHistory();
    }
    FilesUtility.loadFavorite();
  }
}
