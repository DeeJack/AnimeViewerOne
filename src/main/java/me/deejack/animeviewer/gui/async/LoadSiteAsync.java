package me.deejack.animeviewer.gui.async;

import javafx.concurrent.Task;
import me.deejack.animeviewer.gui.controllers.HomeController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.source.AnimeSource;

import static me.deejack.animeviewer.gui.App.setSite;

public class LoadSiteAsync extends Task {
  private final AnimeSource site;

  public LoadSiteAsync(AnimeSource site) {
    this.site = site;
  }

  @Override
  protected Void call() {
    setSite(site);
    return null;
  }

  @Override
  protected void succeeded() {
    new HomeController().setup();
  }

  @Override
  protected void failed() {
    SceneUtility.handleException(getException());
    super.failed();
  }
}
