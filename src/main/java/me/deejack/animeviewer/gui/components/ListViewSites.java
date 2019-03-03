package me.deejack.animeviewer.gui.components;

import javafx.event.Event;
import javafx.scene.control.ListView;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.LoadSiteAsync;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class ListViewSites extends ListView<Site> {
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
        showWaitAndLoad("Caricando il sito");
        LoadSiteAsync siteAsync = new LoadSiteAsync(newValue);
        new Thread(siteAsync).start();
        siteAsync.setOnFailed((e) -> selected = false);
      }
    });
    setOnKeyPressed(Event::consume);
  }
}
