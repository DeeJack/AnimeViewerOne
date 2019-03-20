package me.deejack.animeviewer.gui.components.loading;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LoadingPane extends AnchorPane {
  public LoadingPane(String text, Region root) {
    setStyle("-fx-background-color: rgba(169, 169, 169, 0.7)");
    setId("loadingLayer");
    VBox box = new VBox(new LoadingIndicator(), new Label(text));
    getChildren().add(box);
    registerEvents(box, root);
    centerOnScreen(box, root);
  }

  private void registerEvents(VBox box, Region root) {
    root.widthProperty().addListener((event, oldValue, newValue) -> setWidth(newValue.doubleValue()));
    root.heightProperty().addListener((event, oldValue, newValue) -> setHeight(newValue.doubleValue()));
    root.widthProperty().addListener((event, oldValue, newValue) -> centerOnScreen(box, root));
    root.heightProperty().addListener((event, oldValue, newValue) -> centerOnScreen(box, root));
  }

  private void centerOnScreen(VBox box, Region root) {
    double x = root.getWidth() / 2 - getWidth();
    double y = root.getHeight() / 2 - getHeight();
    box.setLayoutX(x);
    box.setLayoutY(y);
  }
}
