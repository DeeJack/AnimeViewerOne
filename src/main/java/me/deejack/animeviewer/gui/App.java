package me.deejack.animeviewer.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.deejack.animeviewer.gui.connection.CustomConnection;
import me.deejack.animeviewer.gui.scenes.EventHandler;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.defaultsources.animeleggendari.AnimeLeggendariSource;
import me.deejack.animeviewer.logic.defaultsources.dreamsub.DreamSubSource;
import me.deejack.animeviewer.logic.defaultsources.otakustream.OtakuStreamSource;
import me.deejack.animeviewer.logic.extensions.ExtensionLoader;
import me.deejack.animeviewer.logic.githubupdates.Github;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.logic.utils.FilesManager.createDirsIfNotExists;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

public class App extends Application {
  public static final List<FilteredSource> SITES = new ArrayList<>();
  private static App instance;
  private static FilteredSource site;

  public static void main(String[] args) {
    ConnectionUtility.setSiteConnection(new CustomConnection());
    launch(args);
  }

  public static FilteredSource getSite() {
    return site;
  }

  public static void setSite(FilteredSource site) {
    App.site = site;
  }

  public static App getInstance() {
    return instance;
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      instance = this;
      SceneUtility.setStage(primaryStage);
      primaryStage.setScene(new Scene(new StackPane(), 1000, 720));
      primaryStage.show();

      createDirsIfNotExists();
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingExt"));
      loadExtensions();
      createScene(primaryStage);

      loadHistory();
      FilesUtility.loadFavorite();
      new Github().checkUpdatesAsync();
    } catch (Exception exc) {
      logError(exc);
    }
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handleException(throwable));
  }

  private void createScene(Stage primaryStage) {
    primaryStage.setScene(new Scene(SceneUtility.loadParent("/scenes/select.fxml")));
    primaryStage.setTitle(LocalizedApp.getInstance().getString("SelectWindowTitle"));
    primaryStage.getScene().setOnKeyPressed(EventHandler::onKeyEvent);
    primaryStage.getScene().setOnMouseClicked(EventHandler::onMouseEvent);
    primaryStage.setOnCloseRequest((event) -> EventHandler.onCloseRequest());
  }

  private void loadExtensions() {
    SITES.addAll(ExtensionLoader.loadExtension());
    SITES.add(new DreamSubSource());
    SITES.add(new OtakuStreamSource());
    SITES.add(new AnimeLeggendariSource());
  }

  private void loadHistory() {
    File history = History.HISTORY_FILE;
    File lastHistory = history;
    if (FilesUtility.tempHistory.exists()) {
      if (!history.exists() || (history.exists() && history.lastModified() > FilesUtility.tempHistory.lastModified()))
        lastHistory = FilesUtility.tempHistory;
    }
    if (lastHistory.exists())
      FilesUtility.loadHistory();
  }
}
