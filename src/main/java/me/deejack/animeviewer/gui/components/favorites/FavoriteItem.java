package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class FavoriteItem extends SingleFavorite {
  private final Anime anime;

  public FavoriteItem(Anime anime) {
    super(anime);
    this.anime = anime;
    setOnRemove(getOnRemove());
    setAnimeInfo();
  }

  private EventHandler<ActionEvent> getOnRemove() {
    return (event) -> {
      Favorite.getInstance().removeFavorite(anime);
      Favorite.getInstance().saveToFile();
      ((Pane) getParent()).getChildren().remove(this);
    };
  }

  private void setAnimeInfo() {
    Label labelTitle = new Label("Titolo: " + anime.getAnimeInformation().getName());
    Label labelEpisodes = new Label("Episodi: " + anime.getAnimeInformation().getNumberOfEpisodes());
    TextArea textAreaPlot = new TextArea(anime.getAnimeInformation().getPlot());
    textAreaPlot.setWrapText(true);
    textAreaPlot.setEditable(false);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelEpisodes, textAreaPlot);
  }
}
