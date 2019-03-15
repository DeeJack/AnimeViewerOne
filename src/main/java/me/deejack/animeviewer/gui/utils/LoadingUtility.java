package me.deejack.animeviewer.gui.utils;

import javafx.application.Platform;
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
    System.out.println("Showing load");
    hideWaitLoad();
    Pane root = (Pane) SceneUtility.getStage().getScene().getRoot();
    root.getChildren().add(new LoadingPane(msg, root));
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
    Region root = (Region) SceneUtility.getStage().getScene().getRoot();
    if (root.lookup("#loadingLayer") == null)
      return;
    System.out.println("Hiding load");
    ((Pane) root).getChildren().remove(root.lookup("#loadingLayer"));
  }
}
