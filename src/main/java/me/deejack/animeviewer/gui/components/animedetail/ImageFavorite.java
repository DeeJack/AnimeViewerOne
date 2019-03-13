package me.deejack.animeviewer.gui.components.animedetail;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ImageFavorite extends ImageView {
  private static final Image imageFavorite = new Image(App.class.getResourceAsStream("/assets/favorite.png"));
  private static final Image imageNonFavorite = new Image(App.class.getResourceAsStream("/assets/non-favorite.png"));

  public ImageFavorite(Anime anime) {
    setFitHeight(53);
    setFitWidth(59);
    setPreserveRatio(true);
    setPickOnBounds(true);
    if (anime.isFavorite())
      setImage(imageFavorite);
    else
      setImage(imageNonFavorite);
    registerEvents(anime);
  }

  // Volendo posso fare solo il cambio di immagine qua, fare la gestione dell'anime nell'altra classe ma boh
  private void registerEvents(Anime anime) {
    setOnMouseClicked((event) -> {
      anime.toggleFavorite();
      if (anime.isFavorite())
        setImage(imageFavorite);
      else
        setImage(imageNonFavorite);
      Favorite.getInstance().saveToFile();
    });
  }
}
