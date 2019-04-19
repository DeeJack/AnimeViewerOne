package me.deejack.animeviewer.gui.async;

import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.controllers.HomeController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.source.FilteredSource;

import static me.deejack.animeviewer.gui.App.setSite;

public class LoadSiteAsync extends Task {
  private final FilteredSource site;

  public LoadSiteAsync(FilteredSource site) {
    this.site = site;
  }

  @Override
  protected Void call() {
    setSite(site);
    return null;
  }

  @Override
  protected void succeeded() {
    new HomeController().initialize();
  }

  @Override
  protected void failed() {
    SceneUtility.handleException(getException());
    super.failed();
  }
}
