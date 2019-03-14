package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ListViewEpisodes extends ListView<ItemEpisode> {
  private final Anime anime;

  public ListViewEpisodes(Anime anime, Region root) {
    this.anime = anime;
    root.heightProperty().addListener((observable, oldValue, newValue) -> {
      setPrefHeight(getHeight() + ((newValue.doubleValue() - oldValue.doubleValue())));
      System.out.print(getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
      System.out.println("  -  " + (newValue.doubleValue() - oldValue.doubleValue()));
    });
    initialize();
  }

  private void initialize() {
    setMinHeight(100);
    setHeight(100);
    setPrefWidth(530);
    BorderPane.setAlignment(this, Pos.CENTER);
    addChildren();
    getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void addChildren() {
    anime.getEpisodes().stream()
            .map((episode) -> new ItemEpisode(episode, anime, this))
            .forEach(getItems()::add);
  }
}
