package me.deejack.animeviewer.gui.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.TMP_PATH;
import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

public final class SceneUtility {
  public static final List<BaseScene> previousScenes = new LinkedList<>();
  private static final Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.FINISH);
  /**
   * Key: the link to the image
   * Value: The path to the image in the system
   */
  private static final HashMap<String, String> imagesCache = new HashMap<>();
  private static BaseScene previousScene = null;
  private static Stage stage;

  static {
    File path = new File(TMP_PATH);
    if (!path.exists()) {
      path.mkdir();
      path.deleteOnExit();
    }
  }

  private SceneUtility() {
  }

  /**
   * Load the image async
   *
   * @param link The link to the image
   * @return A task which permit the download of the image async
   */
  public static Task<Image> loadImage(String link) {
    System.err.println("DOWNLOADING IMAGE " + link);
    Task<Image> task = getImageAsync(link);
    new Thread(task).start();
    return task;
  }

  /**
   * Download the image
   *
   * @param link The link to the image
   * @return A task which permit the download of the image async
   */
  private static Task<Image> getImageAsync(String link) {
    return new Task<Image>() {
      @Override
      protected Image call() throws Exception {
        if (imagesCache.containsKey(link))
          return new Image(Files.newInputStream(Paths.get(imagesCache.get(link))));
        else {
          Connection.Response response = ConnectionUtility.connect(link, false);
          Path tempPath = Files.createTempFile(Paths.get(TMP_PATH), null, link.substring(link.lastIndexOf("")));
          Files.write(tempPath, response.bodyAsBytes());
          imagesCache.put(link, tempPath.toString());
          return new Image(new ByteArrayInputStream(response.bodyAsBytes()));
        }
      }
    };
  }

  /**
   * Load the parent giving the path
   *
   * @param path The path (of the jar) to the fxml file, es "/scenes/home.fxml"
   * @return The parent
   */
  public static Parent loadParent(String path) {
    Parent parent = null;
    try {
      parent = FXMLLoader.load(App.class.getResource(path), ResourceBundle.getBundle("languages/messages", Locale.getDefault()));
    } catch (IOException ex) {
      handleException(ex);
    }
    return parent;
  }

  /**
   * Handle the exception showing a alert window and logging into a file
   *
   * @param throwable The exception the program got
   */
  public static void handleException(Throwable throwable) {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(() -> handleException(throwable));
      return;
    }
    logError(throwable);
    hideWaitLoad();
    if (alert.isShowing())
      return;
    alert.setContentText("Errore durante l'esecuzione.\n" +
            throwable.getMessage());
    alert.getDialogPane().setPrefHeight(300);
    alert.show();
  }

  public static Stage getStage() {
    return stage;
  }

  public static void setStage(Stage stage) {
    SceneUtility.stage = stage;
  }

  public static void goToPreviousScene() {
    if (previousScenes.isEmpty()) {
      previousScene = null;
      getStage().setTitle("Home");
      getStage().getScene().setRoot(loadParent("/scenes/select.fxml"));
      return;
    }
    BaseScene previousScene = previousScenes.get(previousScenes.size() - 1); // Retrieve the last scene
    System.out.println(previousScene.getName());
    getStage().setTitle(previousScene.getTitle());
    getStage().getScene().setCursor(Cursor.DEFAULT);
    getStage().getScene().setRoot(previousScene.getRoot());
    previousScenes.remove(previousScene);
    SceneUtility.previousScene = previousScene;
    hideWaitLoad();
    System.out.println(getStage().getScene().getCursor());
  }

  public static void setRoot(BaseScene newScene) {
    if (!newScene.getName().equalsIgnoreCase("Home") &&
            (previousScene != null &&
                    !previousScene.getName().equalsIgnoreCase("Streaming")))
      previousScenes.add(previousScene);
    System.out.println(previousScenes);
    previousScene = newScene;
    getStage().getScene().setRoot(newScene.getRoot());
    getStage().setTitle(newScene.getTitle());
    getStage().getScene().setCursor(Cursor.DEFAULT);
    System.out.println(getStage().getScene().getCursor());
  }

  public static void goToSelect() {
    previousScenes.clear();
    goToPreviousScene();
  }
}
