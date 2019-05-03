package me.deejack.animeviewer.logic.defaultsources.animeplanet;

import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.source.ParsedHttpSource;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;

public class AnimePlanetSource extends ParsedHttpSource {

  public AnimePlanetSource() {
    super("https://www.anime-planet.com/", "https://www.anime-planet.com/inc/img/logo.png");
  }

  @Override
  protected String animeSelector() {
    return null;
  }

  @Override
  protected String pagesSelector() {
    return null;
  }

  @Override
  public Anime animeFromElement(Element element) {
    return null;
  }

  @Override
  public Connection.Response popularAnimeRequest(int page) {
    return null;
  }

  @Override
  public Connection.Response searchAnimeRequest(int page, String search) {
    return null;
  }

  @Override
  public Connection.Response filterRequest(int page, HiddenSidebarBuilder filters) {
    return null;
  }

  @Override
  public Filter[] getFilters() {
    return new Filter[0];
  }

  @Override
  public String getName() {
    return null;
  }
}
