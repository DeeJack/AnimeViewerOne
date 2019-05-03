package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import me.deejack.animeviewer.gui.components.filters.ComboBoxFilter;
import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.AnimeStatus;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.source.ParsedHttpSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnimeLeggendariSource extends ParsedHttpSource {

  public AnimeLeggendariSource() {
    super("https://animepertutti.com/", "https://animepertutti.com/wp-content/uploads/2018/08/bgAL.jpg");
  }

  @Override
  protected String animeSelector() {
    return "div#main-content > div.mh-row > div.mh-col-1-2 > article";
  }

  @Override
  protected String pagesSelector() {
    return "nav.pagination > div.nav-links > a.page-numbers:nth-last-child(2)";
  }

  @Override
  public Anime animeFromElement(Element element) {
    Element aTag = element.children().first().getElementsByTag("a").first();
    String link = aTag.attr("href");
    String title = aTag.attr("title");
    String imgUrl = aTag.children().first().attr("src");
    return new AnimeLeggendariAnime(link, new AnimeInformation("Not released", title, -1, 0, new ArrayList<>(), imgUrl, AnimeStatus.UNKNOWN));
  }

  @Override
  public Connection.Response popularAnimeRequest(int page) {
    return null;
  }

  @Override
  public Connection.Response searchAnimeRequest(int page, String search) {
    return ConnectionUtility.connect(getBaseUrl() + "page/" + page + "/?s=" + search, false);
  }

  @Override
  public Connection.Response filterRequest(int page, HiddenSidebarBuilder filters) {
    Filter genres = filters.getFilters()[0];
    String url = getBaseUrl() + "/category/" + genres.getFilterValue() + "/page/" + page + "/";
    return ConnectionUtility.connect(url, true);
  }

  @Override
  public Filter[] getFilters() {
    return new Filter[]{
            new ComboBoxFilter("genres", LocalizedApp.getInstance().getString("Genres"), getGenres())
    };
  }

  private Map<String, String> getGenres() {
    Map<String, String> genres = new LinkedHashMap<>();
    Document homePage = ConnectionUtility.getPage(getBaseUrl(), false);
    Elements genresEl = homePage.getElementById("cat").getElementsByTag("option");
    genresEl.remove(0);
    genresEl.stream().map(Element::text).forEach((genre) -> genres.put(genre, genre.replaceAll(" ", "-")));
    return genres;
  }

  @Override
  public String getName() {
    return "AnimeLeggendari";
  }
}
