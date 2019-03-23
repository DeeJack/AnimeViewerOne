package me.deejack.animeviewer.gui.components.animedetail;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ImageFavorite extends ImageView {
  private static final Image imageFavorite = new Image(App.class.getResourceAsStream("/assets/favorite.png"));
  private static final Image imageNonFavorite = new Image(App.class.getResourceAsStream("/assets/non-favorite.png"));

  public ImageFavorite(Anime anime) {
    setFitHeight(53);
    setFitWidth(59);
    setPreserveRatio(true);
    setPickOnBounds(true);
    if (anime.isFavorite()) {
      Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("FavoriteNotImageTooltip")));
      setImage(imageFavorite);
    } else {
      Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("FavoriteImageTooltip")));
      setImage(imageNonFavorite);
    }
    registerEvents(anime);
  }

  // Volendo posso fare solo il cambio di immagine qua, fare la gestione dell'anime nell'altra classe ma boh
  private void registerEvents(Anime anime) {
    setOnMouseClicked((event) -> {
      anime.toggleFavorite();
      if (anime.isFavorite()) {
        Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("FavoriteNotImageTooltip")));
        setImage(imageFavorite);
      } else {
        Tooltip.install(this, new Tooltip(LocalizedApp.getInstance().getString("FavoriteImageTooltip")));
        setImage(imageNonFavorite);
      }
      FilesUtility.saveFavorite();
    });
  }
}
