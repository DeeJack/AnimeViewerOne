package me.deejack.animeviewer.gui.async;

import java.util.List;
import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.gui.controllers.AnimeSceneController;
import me.deejack.animeviewer.gui.controllers.HomeController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.source.FilteredSource;

import static me.deejack.animeviewer.gui.App.setSite;

public class FilterAsync extends Task<List<Anime>> {
  private final FilterList filters;
  private final int page;

  public FilterAsync(FilterList filters, int page) {
    this.filters = filters;
    this.page = page;
  }

  @Override
  protected List<Anime> call() {
    return App.getSite().filter(filters, page);
  }

  @Override
  protected void succeeded() {
    new AnimeSceneController(getValue(), page, false, filters, "");
  }

  @Override
  protected void failed() {
    SceneUtility.handleException(getException());
    super.failed();
  }
}
