package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class AlwaysOnTopImage extends ImageView {
  public AlwaysOnTopImage() {
    setFitHeight(25);
    setFitWidth(31);
    setPickOnBounds(true);
    setImage(new Image(App.class.getResourceAsStream("/assets/pin.png")));
    setOnMouseClicked((event) -> SceneUtility.getStage().setAlwaysOnTop(!SceneUtility.getStage().isAlwaysOnTop()));
    Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("AlwaysOnTopTooltip")));
  }
}
