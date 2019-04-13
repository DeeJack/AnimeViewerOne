package me.deejack.animeviewer.gui.utils;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
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
    Parent rootParent = SceneUtility.getStage().getScene().getRoot();
    if (rootParent instanceof GridPane) {
      GridPane root = (GridPane) rootParent;
      root.add(new LoadingPane(msg, root), 2, 4);
    } else if (rootParent instanceof Pane) {
      Pane root = (Pane) rootParent;
      root.getChildren().add(new LoadingPane(msg, root));
    } else {
      TabPane root = (TabPane) rootParent;
      ((Pane) root.getTabs().get(0).getContent()).getChildren().add(new LoadingPane(msg, root)); // Sbagliato
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
