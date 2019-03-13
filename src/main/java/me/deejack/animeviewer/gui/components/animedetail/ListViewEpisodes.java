package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class ListViewEpisodes extends ListView {
  private final Anime anime;

  public ListViewEpisodes(Anime anime) {
    this.anime = anime;
    initialize();
  }

  private void initialize() {
    setMinHeight(158);
    setHeight(158);
    setPrefWidth(530);
    BorderPane.setAlignment(this, Pos.CENTER);
    SceneUtility.getStage().getScene().heightProperty().addListener((observable, oldValue, newValue) ->
            setPrefHeight(getMinWidth() + (newValue.doubleValue() - 430))
    );
    addChildren();
    getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void addChildren() {
    for(Episode episode : anime.getEpisodes()) {
      getChildren().add(new ItemEpisode(episode, anime, this));
    }
    System.out.println(getChildren().size());
    for (Node node : getChildren())
      System.out.println(((ItemEpisode) node).getHeight());
    /*anime.getEpisodes().stream()
            .map((episode) -> new ItemEpisode(episode, anime, this))
            .forEach(getChildren()::add);
    System.out.println(getChildren().size());*/
  }
}
