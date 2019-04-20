package me.deejack.animeviewer.gui.components.general;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class ButtonSideBar extends HBox {
  public ButtonSideBar() {
    Button button = GlyphsDude.createIconButton(MaterialDesignIcon.FILTER, LocalizedApp.getInstance().getString("Filters"),
            "2em", null, ContentDisplay.LEFT);
    getChildren().add(button);
    onMousePressedProperty().addListener((observable, oldValue, newValue) -> button.setOnAction((event) -> newValue.handle(null)));
    setWidth(50);
    setHeight(50);
    setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-width: 1px");
  }
}
