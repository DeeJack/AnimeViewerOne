package me.deejack.animeviewer.gui.components.animedetail;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.utils.SceneUtility;

public class ImageAnime extends ImageView {
  public ImageAnime(String url) {
    setFitHeight(213);
    setFitWidth(673);
    setPickOnBounds(true);
    Task<Image> task = SceneUtility.loadImage(url);
    Platform.runLater(() -> task.setOnSucceeded((value) -> setImage(task.getValue())));
  }
}
