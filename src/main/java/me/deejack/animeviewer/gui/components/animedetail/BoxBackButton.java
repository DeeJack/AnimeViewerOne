package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.components.general.ButtonBack;

public class BoxBackButton extends HBox {
  public BoxBackButton() {
    ButtonBack btnBack = new ButtonBack();
    setAlignment(Pos.TOP_LEFT);
    getChildren().add(btnBack);
  }
}
