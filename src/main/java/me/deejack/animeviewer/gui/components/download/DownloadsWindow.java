package me.deejack.animeviewer.gui.components.download;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import java.util.ArrayList;
import java.util.List;

public final class DownloadsWindow {
  private static final DownloadsWindow instance = new DownloadsWindow();
  private final Stage downloadStage = new Stage();
  private final FlowPane flowPane;
  private final List<SingleDownload> downloads = new ArrayList<>();

  private DownloadsWindow() {
    ScrollPane root = (ScrollPane) SceneUtility.loadParent("/scenes/download/downloadProgress.fxml");
    flowPane = new FlowPane();
    root.setContent(flowPane);
    downloadStage.setResizable(false);
    downloadStage.setOnCloseRequest(onClose());
    downloadStage.setScene(new Scene(root));
  }

  public static DownloadsWindow getInstance() {
    return instance;
  }

  private EventHandler<WindowEvent> onClose() {
    return (event) -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, LocalizedApp.getInstance().getString("DownloadClose"),
              ButtonType.OK, ButtonType.CANCEL);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.OK) {
        for (SingleDownload download : downloads)
          download.cancel();
      } else event.consume();
    };
  }

  public void addDownload(SingleDownload download) {
    flowPane.getChildren().add(download.getRoot());
    downloads.add(download);
  }

  private void clear() {
    flowPane.getChildren().clear();
  }

  public Stage getStage() {
    return downloadStage;
  }
}
