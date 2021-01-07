package me.deejack.animeviewer.gui.async;

import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.List;

public class LoadPageAsync extends Task<List<Anime>> {
  private final HiddenSidebarBuilder filters;
  private final String search;
  private final boolean isSearch;
  private final int page;

  public LoadPageAsync(HiddenSidebarBuilder filters, String search, boolean isSearch, int page) {
    this.filters = filters;
    this.search = search;
    this.isSearch = isSearch;
    this.page = page;
  }

  @Override
  protected List<Anime> call() {
    return isSearch ? App.getSite().searchAnime(search, page) : App.getSite().filter(filters.getFilters(), page);
  }

  @Override
  protected void failed() {
    SceneUtility.handleException(getException());
    super.failed();
  }
}
