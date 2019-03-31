package me.deejack.animeviewer.gui.scenes;

import java.io.File;
import java.util.Objects;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import me.deejack.animeviewer.gui.utils.LoadingUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public final class EventHandler {
  private EventHandler() {
  }

  public static void onKeyEvent(KeyEvent keyEvent) {
    switch (keyEvent.getCode()) {
      case F11:
        SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
        break;
      case DOWN:
      case RIGHT:
      case LEFT:
      case UP:
        keyEvent.consume();
        break;
      case TAB:
        if (LoadingUtility.isWaiting())
          keyEvent.consume();
        break;
    }
  }

  public static void onMouseEvent(MouseEvent mouseEvent) {
  }

  public static void onCloseRequest() {
    File tmpDir = new File(GeneralUtility.TMP_PATH);
    if (tmpDir.exists()) {
      for (File file : Objects.requireNonNull(tmpDir.listFiles()))
        file.delete();
      tmpDir.delete();
    }
    /*FilesUtility.saveHistory();
    FilesUtility.saveFavorite();*/
    System.exit(0);
  }
}
