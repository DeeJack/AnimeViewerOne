package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.favorite.FavoriteAnime;

public class FavoriteItem extends SingleFavorite {
  private final FavoriteAnime favorite;

  public FavoriteItem(FavoriteAnime favorite) {
    super(favorite.getAnime(), favorite.getImagePath());
    this.favorite = favorite;

    setOnRemove(getOnRemove());
    setAnimeInfo();
  }

  public EventHandler<ActionEvent> getOnRemove() {
    return (event) -> {
      Favorite.getInstance().removeFavorite(favorite.getAnime());
      FilesUtility.saveFavorite();
      ((Pane) getParent()).getChildren().remove(this);
    };
  }

  private void setAnimeInfo() {
    Label labelTitle = new Label("Titolo: " + favorite.getAnime().getAnimeInformation().getName());
    Label labelEpisodes = new Label("Episodi: " + favorite.getAnime().getAnimeInformation().getNumberOfEpisodes());
    TextArea textAreaPlot = new TextArea(favorite.getAnime().getAnimeInformation().getPlot());
    textAreaPlot.setWrapText(true);
    textAreaPlot.setEditable(false);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelEpisodes, textAreaPlot);
  }
}
