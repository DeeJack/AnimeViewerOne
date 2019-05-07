package me.deejack.animeviewer.gui.components.animescene;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ContextMenuAnimeBox extends ContextMenu {
  private final Anime anime;
  private final ImageView favoriteImageView;
  private final Image favoriteImage;
  private final Listener<Anime> onRequestAnimeTab;

  public ContextMenuAnimeBox(Anime anime, ImageView favoriteImageView, Image favoriteImage, Listener<Anime> onRequestAnimeTab) {
    this.anime = anime;
    this.onRequestAnimeTab = onRequestAnimeTab;
    this.favoriteImageView = favoriteImageView;
    this.favoriteImage = favoriteImage;
    setup();
  }

  private void setup() {
    MenuItem item = anime.isFavorite() ?
            new MenuItem(LocalizedApp.getInstance().getString("FavoriteRemoveItem")) :
            new MenuItem(LocalizedApp.getInstance().getString("FavoriteAddItem"));
    registerItemListener(item);
    getItems().add(item);
    MenuItem newTabItem = new MenuItem(LocalizedApp.getInstance().getString("OpenInNewTab"));
    newTabItem.setOnAction((event) -> onRequestAnimeTab.onChange(anime));
    getItems().add(newTabItem);
  }

  private void registerItemListener(MenuItem item) {
    item.setOnAction((event) -> {
      if (anime.hasBeenLoaded()) {
        onToggleFavorite(item);
      } else anime.loadAsync(() -> onToggleFavorite(item));
    });
  }

  private void onToggleFavorite(MenuItem item) {
    anime.toggleFavorite();
    FilesUtility.saveFavorite();
    favoriteImageView.setImage(anime.isFavorite() ?
            favoriteImage :
            null);
    item.setText(anime.isFavorite() ?
            LocalizedApp.getInstance().getString("FavoriteRemoveItem") :
            LocalizedApp.getInstance().getString("FavoriteAddItem"));
  }
}
