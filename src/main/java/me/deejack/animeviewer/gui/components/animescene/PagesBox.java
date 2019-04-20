package me.deejack.animeviewer.gui.components.animescene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.LoadPageAsync;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class PagesBox extends HBox {
  private final String search;
  private final FilterList filters;
  private final boolean isSearch;
  private final int currentPage;
  private final Listener<List<Anime>> onPageChanged;

  public PagesBox(int currentPage, String search, boolean isSearch, FilterList filters, Listener<List<Anime>> onPageChanged) {
    this.currentPage = currentPage;
    this.search = search;
    this.isSearch = isSearch;
    this.filters = filters;
    this.onPageChanged = onPageChanged;
    setAlignment(Pos.CENTER);
    loadPages();
  }

  public void loadPages() {
    int totalPages = App.getSite().getPages();
    getChildren().clear();

    if (currentPage != 1)
      getChildren().add(new PageButton(1, true));
    if (currentPage > 2)
      getChildren().add(new PageButton(currentPage - 1, true));
    getChildren().add(new PageButton(currentPage, false));
    if (currentPage + 1 < totalPages)
      getChildren().add(new PageButton(currentPage + 1, true));
    if (currentPage != totalPages)
      getChildren().add(new PageButton(totalPages, true));
  }

  class PageButton extends Button {
    PageButton(int page, boolean isOtherPage) {
      super(page + "");
      setDisable(!isOtherPage);
      setOnMousePressed((a) -> {
        if (a.isPrimaryButtonDown()) {
          showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingSwitchPage"));
          Task<List<Anime>> pageChangeTask = new LoadPageAsync(filters, search, isSearch, page);
          new Thread(pageChangeTask).start();
          try {
            onPageChanged.onChange(pageChangeTask.get());
          } catch (InterruptedException | ExecutionException e) {
            onPageChanged.onChange(new ArrayList<>());
          }
        }
      });
    }
  }
}
