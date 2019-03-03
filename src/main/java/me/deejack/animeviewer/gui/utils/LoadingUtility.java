package me.deejack.animeviewer.gui.utils;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.App;

public final class LoadingUtility {

  private LoadingUtility() {
  }

  public static void showWaitAndLoad(String msg) {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(() -> showWaitAndLoad(msg));
      return;
    }
    hideWaitLoad();
    Region root = (Region) SceneUtility.getStage().getScene().getRoot();
    ImageView loadGif = new ImageView(new Image(App.class.getResourceAsStream("/assets/load.gif")));
    loadGif.setFitHeight(100);
    loadGif.setPreserveRatio(true);
    Label info = new Label(msg);
    VBox center = new VBox(loadGif, info);
    center.setLayoutX(root.getWidth() / 2 + loadGif.getFitWidth());
    center.setLayoutY(root.getHeight() / 2); //+ loadGif.getFitHeight());
    AnchorPane layer = new AnchorPane(center);
    layer.setId("loadingLayer");
    layer.setPrefWidth(root.getWidth());
    layer.setPrefHeight(root.getHeight());
    layer.setStyle("-fx-background-color: rgba(169, 169, 169, 0.7)");
    //layer.setBackground(new Background(new BackgroundFill(Paint.valueOf("grey"), null, null)));
    if (root instanceof Pane)
      ((Pane) root).getChildren().add(layer);
    else
      ((Pane) ((ScrollPane) root).getContent()).getChildren().add(layer);
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
    if (root instanceof Pane)
      ((Pane) root).getChildren().remove(root.lookup("#loadingLayer"));
    else
      ((Pane) ((ScrollPane) root).getContent()).getChildren().remove(root.lookup("#loadingLayer"));
  }
}
