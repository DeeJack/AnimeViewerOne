package me.deejack.animeviewer.gui.components.animescene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.LoadPageAsync;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class PagesBox extends HBox {
  private final String search;
  private final FilterList filters;
  private final boolean isSearch;
  private final int currentPage;

  public PagesBox(int currentPage, String search, boolean isSearch, FilterList filters) {
    this.currentPage = currentPage;
    this.search = search;
    this.isSearch = isSearch;
    this.filters = filters;
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
          new Thread(new LoadPageAsync(filters, search, isSearch, page)).start();
        }
      });
    }
  }
}
