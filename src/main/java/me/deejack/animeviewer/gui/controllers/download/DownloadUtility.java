package me.deejack.animeviewer.gui.controllers.download;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;

public final class DownloadUtility {
  private DownloadUtility() {
  }

  /**
   * Show a dialog to let the user choose a path for saving the file
   *
   * @param text The initial file name when opening the dialog
   * @return The file chosen by the user, may be null
   */
  public static File savePath(String text) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select the path");
    fileChooser.setInitialFileName(text + ".mp4");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return fileChooser.showSaveDialog(SceneUtility.getStage());
  }

  /**
   * Open a dialog to let the user choose a folder where save the videos
   *
   * @return The directory chosen by the user
   */
  public static File saveDirectory() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Select folder");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    return chooser.showDialog(SceneUtility.getStage());
  }

  /**
   * Let the user chose from which source (and with which resolution) to download the video
   *
   * @return null if the user cancel the operation (close the popup), otherwise it'll return the reference to the selected link
   * @throws IOException
   */
  public static StreamingLink chooseSource(Episode episode) throws IOException {
    AtomicReference<StreamingLink> selectedLink = new AtomicReference<>(null);
    if (episode.getUrl().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Nessuno streaming disponibile, probabilmente deve ancora uscire l'episodio",
              ButtonType.OK);
      alert.showAndWait();
      hideWaitLoad();
      return null;
    }

    List<StreamingLink> links = episode.getStreamingLinks();
    if (links.size() == 1)
      return links.get(0);
    else if (links.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Nessuno streaming disponibile, probabilmente deve ancora uscire l'episodio",
              ButtonType.OK);
      alert.showAndWait();
      hideWaitLoad();
      return null;
    }

    Parent parent = SceneUtility.loadParent("/scenes/download/choseSource.fxml");
    ListView<StreamingLink> listView = (ListView<StreamingLink>) parent.lookup("#listView");
    listView.getItems().addAll(links);
    Button btnOk = (Button) parent.lookup("#btnOk");
    Button btnCancel = (Button) parent.lookup("#btnCancel");
    Scene scene = new Scene(parent);
    Stage stage = new Stage(StageStyle.UNDECORATED);
    stage.setScene(scene);

    btnOk.setOnAction((v) -> {
      if (listView.getSelectionModel().getSelectedItem() != null)
        selectedLink.set(listView.getSelectionModel().getSelectedItem());
      hideWaitLoad();
      stage.close();
    });
    btnCancel.setOnAction((v) -> {
      hideWaitLoad();
      stage.close();
    });
    stage.setOnCloseRequest((event) -> hideWaitLoad());
    stage.showAndWait();
    return selectedLink.get();
  }

  public static int choseDownloadSettings() {
    AtomicInteger prefResolution = new AtomicInteger(-1);
    Label labelRes = new Label("Risoluzione preferita: ");
    TextField textFieldRes = new TextField("720");
    HBox boxSettings = new HBox(20, labelRes, textFieldRes);
    boxSettings.setAlignment(Pos.CENTER);
    Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK, ButtonType.CANCEL);
    alert.setGraphic(boxSettings);
    alert.resultProperty().addListener((event, oldValue, newValue) -> {
      if (newValue != ButtonType.OK) {
        alert.close();
        return;
      }
      Optional<Integer> resolution = GeneralUtility.tryParse(textFieldRes.getText());
      if (!resolution.isPresent() || resolution.get() < 100 || resolution.get() > 8000) {
        new Alert(Alert.AlertType.ERROR, "Inserisci un valore valido", ButtonType.OK).showAndWait();
        return;
      }
      prefResolution.set(GeneralUtility.tryParse(textFieldRes.getText()).get());
    });
    alert.showAndWait();
    return prefResolution.get();
  }
}
