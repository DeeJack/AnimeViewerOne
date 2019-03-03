package me.deejack.animeviewer.gui.async;

import java.util.Collections;
import java.util.List;
import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.controllers.AnimeSceneController;
import me.deejack.animeviewer.logic.anime.Anime;

import static me.deejack.animeviewer.gui.App.getSite;

public class SearchByNameAsync extends Task<List<Anime>> {
  private final String searchText;
  private List<Anime> elements;

  public SearchByNameAsync(String searchText) {
    this.searchText = searchText;
  }

  @Override
  protected List<Anime> call() {
    elements = getSite().searchAnime(searchText, 1);
    return Collections.unmodifiableList(elements);
  }

  @Override
  public void succeeded() {
    new AnimeSceneController(elements, 1);
  }

  @Override
  public void failed() {
        /*SceneUtility.handleException(getException());
        super.failed();*/
  }
}
