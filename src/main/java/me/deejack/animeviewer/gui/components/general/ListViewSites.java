package me.deejack.animeviewer.gui.components.general;

import javafx.event.Event;
import javafx.scene.control.ListView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.LoadSiteAsync;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.logic.models.source.FilteredSource;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ListViewSites extends ListView<FilteredSource> {
  private boolean selected = false;

  public ListViewSites() {
    getItems().addAll(App.SITES);
    if (App.getSite() != null)
      getSelectionModel().select(App.getSite());
    registerListeners();
  }

  public void registerListeners() {
    getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (!selected) {
        selected = true;
        showWaitAndLoad(LocalizedApp.getInstance().getString("LoadingSite"));
        LoadSiteAsync siteAsync = new LoadSiteAsync(newValue);
        new Thread(siteAsync).start();
        siteAsync.setOnFailed((e) -> selected = false);
      }
    });
    setOnKeyPressed(Event::consume);
  }
}
