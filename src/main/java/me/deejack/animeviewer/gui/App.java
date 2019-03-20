package me.deejack.animeviewer.gui;

import java.io.File;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.deejack.animeviewer.gui.connection.CustomConnection;
import me.deejack.animeviewer.gui.scenes.EventHandler;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.extensions.ExtensionLoader;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class App extends Application {
  public static final List<FilteredSource> SITES = ExtensionLoader.loadExtension();
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
    ConnectionUtility.setSiteConnection(new CustomConnection());
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handleException(throwable));
    SceneUtility.setStage(primaryStage);
    primaryStage.setScene(new Scene(SceneUtility.loadParent("/scenes/select.fxml")));
    primaryStage.setTitle(LocalizedApp.getInstance().getString("SelectWindowTitle"));
    primaryStage.show();
    primaryStage.getScene().setOnKeyPressed(EventHandler::onKeyEvent);
    primaryStage.getScene().setOnMouseClicked(EventHandler::onMouseEvent);
    primaryStage.setOnCloseRequest((event) -> EventHandler.onCloseRequest());

    // AGGIUNGERE GLI ANIME DESERIALIZZATI DAL FILE PIU AGGIORNATO
    File history = new File(System.getProperty("user.home") + File.separator + ".animeviewer" + File.separator + "history.json");
    File lastHistory;
    if (history.exists()) {
      lastHistory = history;
      /*if (ControlsLayerTask.tempHistory.exists() && history.lastModified() < ControlsLayerTask.tempHistory.lastModified())
        lastHistory = ControlsLayerTask.tempHistory;
    } else if (ControlsLayerTask.tempHistory.exists()) {
      lastHistory = ControlsLayerTask.tempHistory;*/
    } else {
      lastHistory = null;
    }
    if (lastHistory != null) {
      FilesUtility.loadHistory();
    }
    FilesUtility.loadFavorite();
  }
}
