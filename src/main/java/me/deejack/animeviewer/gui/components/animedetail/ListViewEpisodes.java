package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ListViewEpisodes extends ListView<ItemEpisode> {
  private final Anime anime;

  public ListViewEpisodes(Anime anime) {
    this.anime = anime;
    initialize();
  }

  private void initialize() {
    setMinHeight(125);
    setHeight(125);
    setPrefWidth(530);
    BorderPane.setAlignment(this, Pos.CENTER);
    SceneUtility.getStage().getScene().heightProperty().addListener((observable, oldValue, newValue) -> {
      setPrefHeight(getHeight() + ((newValue.doubleValue() - oldValue.doubleValue())));
      System.out.print(getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
      System.out.println("  -  " + (newValue.doubleValue() - oldValue.doubleValue()));
    });
    //setBackground(new Background(new BackgroundFill(Paint.valueOf("red"), CornerRadii.EMPTY, null)));
    addChildren();
    //getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void addChildren() {
    /*for (int i = 0;  i < 10; i++) {
      Label label = new Label("Prova");
      label.setId("asdasd" + i);
      label.setPrefHeight(120);
      //label.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, null)));
      getItems().add(label);
    }*/
    /*for(Episode episode : anime.getEpisodes()) {
      getChildren().add(new ItemEpisode(episode, anime, this));
    }
    System.out.println(getChildren().size());
    for (Node node : getChildren())
      System.out.println(((ItemEpisode) node).getHeight());*/
    anime.getEpisodes().stream()
            .map((episode) -> new ItemEpisode(episode, anime, this))
            .forEach(getItems()::add);
  }
}
