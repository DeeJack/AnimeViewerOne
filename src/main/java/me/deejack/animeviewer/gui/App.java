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
import me.deejack.animeviewer.logic.extentions.ExtensionLoader;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.models.source.animeleggendari.AnimeLeggendariSource;
import me.deejack.animeviewer.logic.models.source.dreamsub.DreamSubSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class App extends Application {
  public static final List<FilteredSource> SITES = ExtensionLoader.loadExtension();//{new DreamSubSource(), new AnimeLeggendariSource()};
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
    // ResourceBundle bundle = ResourceBundle.getBundle("languages/messages", Locale.getDefault());
    //ResourceBundle bundle = ResourceBundle.getBundle("languages/messages", new Locale("en", "us"));
//    System.out.println(bundle.getString("hello"));
    ConnectionUtility.setSiteConnection(new CustomConnection());
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> handleException(throwable));
    SceneUtility.setStage(primaryStage);
    SceneUtility.getStage().setScene(new Scene(SceneUtility.loadParent("/scenes/select.fxml")));
    SceneUtility.getStage().setTitle(LocalizedApp.getInstance().getString("SelectWindowTitle"));
    SceneUtility.getStage().show();
    SceneUtility.getStage().getScene().setOnKeyPressed(EventHandler::onKeyEvent);
    SceneUtility.getStage().getScene().setOnMouseClicked(EventHandler::onMouseEvent);
    SceneUtility.getStage().setOnCloseRequest((event) -> EventHandler.onCloseRequest());

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

    // DA CAMBIARE, METTERE BASESCENE, magari mettere un metodo che la cambia così posso fare un listener...?
    /*SceneUtility.getStage().getScene().rootProperty().addListener((listener, oldValue, newValue) -> {
      Runtime.getRuntime().gc();
      if (!previousScenes.isEmpty() && (oldValue == previousScenes.get(previousScenes.size() - 1)
              || newValue == previousScenes.get(previousScenes.size() - 1)))
        previousScenes.remove(previousScenes.size() - 1);
      else previousScenes.add(oldValue);
    });*/
  }
}
