package me.deejack.animeviewer.gui.async;

import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.gui.controllers.AnimeSceneController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.List;

public class FilterAsync extends Task<List<Anime>> {
  private final HiddenSidebarBuilder filters;
  private final int page;

  public FilterAsync(HiddenSidebarBuilder filters, int page) {
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
