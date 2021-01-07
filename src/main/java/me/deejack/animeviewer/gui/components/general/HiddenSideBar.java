package me.deejack.animeviewer.gui.components.general;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class HiddenSideBar extends BorderPane {
  private final TranslateTransition hideAnimation = new TranslateTransition(Duration.millis(250), this);
  private final TranslateTransition showAnimation = new TranslateTransition(Duration.millis(250), this);
  private final Button btnClose = new Button(">");
  private final HBox btnOpen;
  private VBox sideBar;

  public HiddenSideBar(HBox btnOpen) {
    this.btnOpen = btnOpen;
    Tooltip.install(btnOpen, new Tooltip(LocalizedApp.getInstance().getString("SidebarControlButton")));
    setup();
  }

  private void setup() {
    StackPane.setAlignment(this, Pos.TOP_RIGHT);
    sideBar = new VBox(btnClose);
    sideBar.setPrefWidth(350);
    prefWidthProperty().bind(SceneUtility.getStage().getScene().widthProperty());
    hideAnimation.toXProperty().bind(prefWidthProperty());
    prefWidthProperty().addListener((obs, oldValue, newValue) -> {
      if (getTranslateX() > 0 && getTranslateX() < newValue.doubleValue())
        setTranslateX(newValue.doubleValue());
    });
    setTranslateX(getPrefWidth());
    sideBar.setMinWidth(VBox.USE_PREF_SIZE);
    sideBar.setMaxWidth(VBox.USE_PREF_SIZE);
    registerEvents();

    showAnimation.setToX(0);
    sideBar.setBackground(new Background(new BackgroundFill(Color.web("f2f2f2"), CornerRadii.EMPTY, Insets.EMPTY)));
    setRight(sideBar);
    setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
  }

  private void registerEvents() {
    btnOpen.setOnMousePressed((event) -> showAnimation.play());
    btnClose.setOnAction((event) -> close());
    setOnMouseClicked((event) -> {
      if (!sideBar.getBoundsInParent().intersects(event.getSceneX(), event.getSceneY(), 0, 0))
        close();
    });
  }

  public VBox getSideBar() {
    return sideBar;
  }

  public void close() {
    hideAnimation.play();
  }
}
