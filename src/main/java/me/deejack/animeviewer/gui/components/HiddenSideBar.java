package me.deejack.animeviewer.gui.components;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class HiddenSideBar extends VBox {
  private final TranslateTransition hideAnimation = new TranslateTransition(Duration.millis(250), this);
  private final TranslateTransition showAnimation = new TranslateTransition(Duration.millis(250), this);
  private final Button controlButton;

  public HiddenSideBar(Button controlButton) {
    this.controlButton = controlButton;
    getChildren().add(controlButton);
    registerEvents();
    System.out.println(getWidth() + " --- " + getPrefWidth() + " ---  ");
    hideAnimation.setToX(getWidth());
    showAnimation.setToX(-getPrefWidth());
    setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY)));
  }

  private void registerEvents() {
    controlButton.setOnAction((event) -> {
      System.out.println(getWidth() + " --- " + getPrefWidth() + " ---  ");
      hideAnimation.setToX(getWidth());
      showAnimation.setToX(-getPrefWidth());
      if (controlButton.getText().equalsIgnoreCase("<")) {
        controlButton.setText(">");
        hideAnimation.play();
      } else {
        controlButton.setText("<");
        showAnimation.play();
      }
    });
  }
}
