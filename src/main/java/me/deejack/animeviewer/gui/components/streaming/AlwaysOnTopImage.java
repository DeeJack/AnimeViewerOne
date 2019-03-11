package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class AlwaysOnTopImage extends ImageView {
  public AlwaysOnTopImage() {
    setFitHeight(25);
    setFitWidth(31);
    setImage(new Image(App.class.getResourceAsStream("/assets/pin.png")));
    setOnMouseClicked((event) -> SceneUtility.getStage().setAlwaysOnTop(!SceneUtility.getStage().isAlwaysOnTop()));
  }
}
