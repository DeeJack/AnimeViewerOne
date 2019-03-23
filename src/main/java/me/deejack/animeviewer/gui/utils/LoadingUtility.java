package me.deejack.animeviewer.gui.utils;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import me.deejack.animeviewer.gui.components.loading.LoadingPane;

public final class LoadingUtility {

  private LoadingUtility() {
  }

  public static void showWaitAndLoad(String msg) {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(() -> showWaitAndLoad(msg));
      return;
    }
    SceneUtility.getStage().getScene().setCursor(Cursor.WAIT);
    System.out.println("SHowing1!!!1");
    hideWaitLoad();
    if (SceneUtility.getStage().getScene().getRoot() instanceof Pane) {
      Pane root = (Pane) SceneUtility.getStage().getScene().getRoot();
      System.err.println(root.getChildren().size());
      root.getChildren().add(new LoadingPane(msg, root));
      System.err.println(root.getChildren().size());
    } else {
      TabPane root = (TabPane) SceneUtility.getStage().getScene().getRoot();
      ((Pane) root.getTabs().get(0).getContent()).getChildren().add(new LoadingPane(msg, root));
    }
  }

  public static void showWaitAndLoad() {
    showWaitAndLoad("");
  }

  public static boolean isWaiting() {
    Region root = (Region) SceneUtility.getStage().getScene().getRoot();
    return root.lookup("#loadingLayer") != null;
  }

  public static void hideWaitLoad() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(LoadingUtility::hideWaitLoad);
      return;
    }
    if (SceneUtility.getStage().getScene() == null)
      return;
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
    Region root = (Region) SceneUtility.getStage().getScene().getRoot();
    if (root instanceof TabPane)
      root = (Region) ((TabPane) root).getTabs().get(0).getContent();
    if (root.lookup("#loadingLayer") == null)
      return;
    ((Pane) root).getChildren().remove(root.lookup("#loadingLayer"));
  }
}
