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
  private final Button btnClose = new Button(">");
  private final Button btnOpen;

  public HiddenSideBar(Button btnOpen) {
    this.btnOpen = btnOpen;
    getChildren().add(btnClose);
    setWidth(0);
    setTranslateX(getWidth());
    setMinWidth(getWidth());
    setMaxWidth(getWidth());
    registerEvents();
    hideAnimation.setToX(getWidth());
    showAnimation.setToX(-getWidth());
    setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY)));
  }

  private void registerEvents() {
    btnOpen.setOnAction((event) -> {
      setWidth(350);
      setMinWidth(getWidth());
      setMaxWidth(getWidth());
      showAnimation.setToX(0);
      showAnimation.play();
    });
    btnClose.setOnAction((event) -> {
      hideAnimation.play();
      hideAnimation.setToX(getWidth());
      setWidth(0);
      setMinWidth(getWidth());
      setMaxWidth(getWidth());
    });
  }
}
