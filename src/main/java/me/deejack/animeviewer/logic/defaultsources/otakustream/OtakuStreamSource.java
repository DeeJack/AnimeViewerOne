package me.deejack.animeviewer.logic.defaultsources.otakustream;

import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.AnimeStatus;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.source.ParsedHttpSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class OtakuStreamSource extends ParsedHttpSource {

  public OtakuStreamSource() {
    super("https://otakustream.tv", "https://otakustream.tv/wp-content/themes/otakustream/static/assets/img/basic/otakustream_black.png");
  }

  @Override
  protected String animeSelector() {
    return "div.ep-boxes > div.ep-box";
  }

  @Override
  protected String pagesSelector() {
    return "";
  }

  @Override
  protected String getPagesByDoc(Document document) {
    Element lastElement = document.getElementsByClass("last").first();
    if (lastElement == null) {
      Element currentPage = document.getElementsByClass("current").first();
      if (currentPage == null)
        return "1";
      return currentPage.text();
    }
    String href = lastElement.attr("href");
    String page = href.split("/")[href.split("/").length - 2];
    System.err.println(href);
    return page;
  }

  @Override
  public Anime animeFromElement(Element element) {
    String imgUrl = element.getElementsByTag("img").attr("src");
    Element a = element.getElementsByTag("a").first();
    String title = a.text();
    String url = a.attr("href");
    AnimeStatus status = AnimeStatus.UNKNOWN;
    if (!element.getElementsByClass("anime-status").isEmpty())
      status = element.getElementsByClass("anime-status").text().equalsIgnoreCase("ongoing") ?
              AnimeStatus.ONGOING : AnimeStatus.COMPLETED;
    String yearRelease = element.select("a[href*=premiered]").text();
    return new OtakuStreamAnime(url, new AnimeInformation(yearRelease, title, -1, 0, new ArrayList<>(), imgUrl, status));
  }

  @Override
  public Connection.Response popularAnimeRequest(int page) {
    return null;
  }

  @Override
  public Connection.Response searchAnimeRequest(int page, String search) {
    return ConnectionUtility.connect(getBaseUrl() + "/page/" + page + "/?s=" + search, false);
  }

  @Override
  public Connection.Response filterRequest(int page, HiddenSidebarBuilder filters) {
    StringBuilder url = new StringBuilder(getBaseUrl() + "/anime/page/" + page + "/?");
    for (Filter filter : filters.getFilters()) {
      if (filter.getFilterValue() != null && !filter.getFilterValue().equals(""))
        url.append(filter.getFilterId()).append("=").append(filter.getFilterValue()).append("&");
    }
    System.out.println(url);
    return ConnectionUtility.connect(url.substring(0, url.length() - 1), true);
  }

  @Override
  public Filter[] getFilters() {
    return OtakuStreamFilters.getFilters();
  }

  @Override
  public String getName() {
    return "OtakusStream";
  }
}
