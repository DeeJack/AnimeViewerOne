package me.deejack.animeviewer.gui.components.updates;

import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.components.favorites.SingleFavorite;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class SingleAnimeUpdate extends SingleFavorite {
  public SingleAnimeUpdate(Anime anime, Episode newEpisode) {
    super(anime);
    System.out.println(newEpisode.getTitle() + newEpisode.getNumber());
  }
}
