package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import me.deejack.animeviewer.gui.components.filters.ComboBoxFilter;
import me.deejack.animeviewer.logic.filters.Filter;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.source.ParsedHttpSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.Map;

public class DreamSubSource extends ParsedHttpSource {
  Map<String, String> genres = new LinkedHashMap<>();

  public DreamSubSource() {
    super("https://www.dreamsub.stream", "https://web.archive.org/web/20190512024350im_/https://www.dreamsub.stream/res/img/logoDS2.png");
  }

  @Override
  public String animeSelector() {
    return "#main-content > div.goblock > div.goblock-content > div.tvBlock";
  }

  @Override
  public Anime animeFromElement(Element element) {
    String name = element.getElementsByClass("tvTitle").first().text();
    String url = getBaseUrl() + element.getElementsByClass("showStreaming").first()
            .getElementsByTag("a").get(0).attr("href");
    String imageUrl = "https:" +
      element.getElementsByClass("cover").get(0).attr("style")
                    .replaceAll("--image-url:url\\(", "").replaceAll("\\) no-repeat center", "").replace(")", "");

    return new DreamsubAnime(name, url, imageUrl);
  }

  @Override
  public Connection.Response popularAnimeRequest(int page) {
    return null;
  }

  @Override
  public Connection.Response searchAnimeRequest(int page, String search) {
    return ConnectionUtility.connect(getBaseUrl() + "/search/?q=" + search + "&page=" + page, true);
  }

  @Override
  public Connection.Response filterRequest(int page, Filter[] filters) {
    StringBuilder url = new StringBuilder(getBaseUrl() + "/filter?");
    for (Filter filter : filters) {
      url.append(filter.getFilterId()).append("=").append(filter.getFilterValue()).append("&");
    }
    url.append("page=").append(page);
    return ConnectionUtility.connect(url.toString(), false);
  }

  @Override
  public Filter[] getFilters() {
    return new Filter[]{
            new ComboBoxFilter("genere", "Genres", getGenres()),
            new ComboBoxFilter("conclusi", "Status", getStatus()),
            new ComboBoxFilter("ordina", "Sort", getSort())
    };
  }

  private Map<String, String> getGenres() {
    /*if (genres.isEmpty()) {
      Document homePage = ConnectionUtility.getPage(getBaseUrl(), false);
      Elements genreElements = homePage.getElementById("genere").getElementsByTag("option");
      for (Element el : genreElements)
        genres.put(el.text(), el.text().toLowerCase());
    }*/
    return genres;
  }

  private Map<String, String> getStatus() {
    Map<String, String> status = new LinkedHashMap<>();
    status.put("Tutto", "tutti");
    status.put("Completati", "conclusi");
    status.put("In Corso", "in-corso");
    status.put("Future Release", "prossimamente");
    return status;
  }

  private Map<String, String> getSort() {
    Map<String, String> sorts = new LinkedHashMap<>();
    sorts.put("Popolarità", "popolarità");
    sorts.put("Alfabetico", "A-Z");
    sorts.put("Voto", "rating");
    sorts.put("Ultime Aggiunte", "recenti");
    sorts.put("Anno", "anno");
    sorts.put("Numero Episodi", "episodi");
    sorts.put("Visualizzazione", "views");
    return sorts;
  }

  @Override
  protected String pagesSelector() {
    return "ul.pages > li:last-child";
  }

  @Override
  public String getName() {
    return "Dreamsub";
  }
}
