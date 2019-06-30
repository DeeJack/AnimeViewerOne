package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class FullScreenImage extends ImageView {
  private final Image fullScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-full.png"));
  private final Image smallScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-small.png"));

  public FullScreenImage() {
    setImage(SceneUtility.getStage().isFullScreen() ? smallScreenImage : fullScreenImage);
    setFitHeight(24);
    setFitWidth(23);
    setPickOnBounds(true);
    setOnMouseClicked((event) -> onClick());
    SceneUtility.getStage().fullScreenProperty().addListener((event) -> setImage(SceneUtility.getStage().isFullScreen() ? smallScreenImage : fullScreenImage));
    Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("FullScreenTooltip")));
  }

  private void onClick() {
    SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
    if (!SceneUtility.getStage().isFullScreen() && SceneUtility.getStage().isAlwaysOnTop()) {
      SceneUtility.getStage().setAlwaysOnTop(false); // I don't know why but it doesn't work otherwise
      SceneUtility.getStage().setAlwaysOnTop(true); // I don't know why but it doesn't work otherwise
    }
  }
}
