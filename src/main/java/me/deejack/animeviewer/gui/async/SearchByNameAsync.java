package me.deejack.animeviewer.gui.async;

import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.controllers.AnimeSceneController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.List;

import static me.deejack.animeviewer.gui.App.getSite;

public class SearchByNameAsync extends Task<List<Anime>> {
  private final String searchText;

  public SearchByNameAsync(String searchText) {
    this.searchText = searchText;
  }

  @Override
  protected List<Anime> call() {
    return getSite().searchAnime(searchText, 1);
  }

  @Override
  public void succeeded() {
    new AnimeSceneController(getValue(), 1, true, null, searchText);
  }

  @Override
  public void failed() {
    SceneUtility.handleException(getException());
    super.failed();
  }
}
