package me.deejack.animeviewer.gui.components.animescene;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimeBoxImage extends StackPane {
  private static final Image imageFavorite = new Image(App.class.getResourceAsStream("/assets/favorite.png"));
  private final Anime anime;
  private final ImageView favoriteImageView;

  public AnimeBoxImage(Anime anime) {
    this.anime = anime;
    favoriteImageView = createImageFavorite();
    setup();
  }

  public static Image getImageFavorite() {
    return imageFavorite;
  }

  private void setup() {
    ImageView view = createAnimeImage();
    setAlignment(Pos.TOP_RIGHT);
    getChildren().addAll(createAnimeImage(), createImageFavorite());
  }

  private ImageView createAnimeImage() {
    ImageView view = new ImageView();
    view.setFitHeight(170);
    view.setFitWidth(250);
    Task<Image> task = SceneUtility.loadImage(anime.getAnimeInformation().getImageUrl());
    task.setOnSucceeded((value) -> view.setImage(task.getValue()));
    //view.setCache(true);
    return view;
  }

  private ImageView createImageFavorite() {
    ImageView favoriteImageView = new ImageView();
    favoriteImageView.setFitHeight(20);
    favoriteImageView.setPreserveRatio(true);
    if (anime.isFavorite())
      favoriteImageView.setImage(imageFavorite);
    return favoriteImageView;
  }

  public ImageView getFavoriteImageView() {
    return favoriteImageView;
  }
}
