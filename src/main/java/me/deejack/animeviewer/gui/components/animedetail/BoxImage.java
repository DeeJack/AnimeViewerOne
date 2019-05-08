package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class BoxImage extends HBox {
  public BoxImage(String imageUrl) {
    ImageAnime imageAnime = new ImageAnime(imageUrl);
    setMinHeight(Double.MIN_VALUE);
    imageAnime.fitHeightProperty().bind(heightProperty());
    setAlignment(Pos.CENTER);
    getChildren().add(imageAnime);
  }
}
