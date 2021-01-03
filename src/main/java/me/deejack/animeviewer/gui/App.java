package me.deejack.animeviewer.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.connection.CustomConnection;
import me.deejack.animeviewer.gui.scenes.EventHandler;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.defaultsources.dreamsub.DreamSubSource;
import me.deejack.animeviewer.logic.extensions.ExtensionLoader;
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
      /*
       * WebView view = new WebView();
       * view.getEngine().load("https://otakustream.tv/anime/black-clover-2017/");
       * view.getEngine().getLoadWorker().stateProperty().addListener(((observable,
       * oldValue, newValue) -> { view.getEngine().
       * executeScript("fetch('https://otakustream.tv/api/tools.php', {" +
       * "method: 'POST', " +
       * "    headers: {'Content-Type':'application/x-www-form-urlencoded'}, body: 'action=load_episodes&offset=0&parentId=17062'}).then("
       * + "function(response) { response.text().then(function(data) {" +
       * "        document.getElementsByTagName('html')[0].innerHTML = data;" +
       * "      });});");
       * System.out.println(view.getEngine().getDocument().getElementsByTagName("html"
       * ).item(0).getTextContent()); }));
       */
      instance = this;
      SceneUtility.setStage(primaryStage);
      StackPane pane = new StackPane(/* view */);
      primaryStage.setScene(new Scene(pane, 1000, 720));
      primaryStage.show();

      createDirsIfNotExists();
      showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingExt"));
      loadExtensions();
      createScene(primaryStage);

      loadHistory();
      FilesUtility.loadFavorite();

      /*
       * TrayNotification notification = new TrayNotification("Download error",
       * LocalizedApp.getInstance().getString("ErrorDownload"), Notifications.ERROR);
       * notification.setAnimation(Animations.FADE);
       * notification.showAndDismiss(Duration.seconds(2));
       */
      // new GithubConnection().checkUpdatesAsync();
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
