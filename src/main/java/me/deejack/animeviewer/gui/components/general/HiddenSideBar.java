package me.deejack.animeviewer.gui.components.general;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
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
    setUp();
  }

  private void setUp() {
    StackPane.setAlignment(this, Pos.TOP_RIGHT);
    getChildren().add(btnClose);
    setWidth(350);
    setTranslateX(getWidth());
    setMinWidth(getWidth());
    setMaxWidth(getWidth());
    registerEvents();
    hideAnimation.setToX(getWidth());
    showAnimation.setToX(0);
    setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY)));
  }

  private void registerEvents() {
    btnOpen.setOnAction((event) -> showAnimation.play());
    btnClose.setOnAction((event) -> hideAnimation.play());
  }
}
