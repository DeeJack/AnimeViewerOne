package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.SiteElement;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Sort;
import me.deejack.animeviewer.logic.anime.dto.Status;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DreamSubSite extends Site {
  public static final String BASE_URL = "https://www.dreamsub.stream/";
  private static final String SEARCH_LINK = BASE_URL + "search/%s/?page=%d";
  private Document homePage;
  private Document currentPage;
  private Genre currentGenre;
  private Sort currentSort;
  private Status currentStatus;
  private String currentSearch;

  public DreamSubSite() {
    super("DreamSub", BASE_URL,
            BASE_URL + "filter?genere=%s&conclusi=%s&ordina=%s&show=responsive&page=%d",
            BASE_URL + "res/img/logoDS2.png");
  }

  @Override
  public void loadHomePage() throws NoConnectionException {
    homePage = ConnectionUtility.getPage(getBaseUrl(), false);
  }

  @Override
  public void loadGenreList() {
    Elements genres = homePage.getElementById("genere").getElementsByTag("option");
    homePage = null;
    for (Element el : genres)
      addGenre(new Genre(el.text(), el.text()));
  }

  @Override
  public void loadSortByList() {
      addSort(new Sort("Popolarità", "popolarita"),
            new Sort("Alfabetico", "A-Z"),
            new Sort("Voto", "rating"),
            new Sort("Ultime aggiunte", "recenti"),
            new Sort("Anno", "anno"),
            new Sort("Numero episodi", "episodi"),
            new Sort("Visualizzazioni", "views"));
  }

  @Override
  public List<SiteElement> search(String search, int page) {
    String link = String.format(SEARCH_LINK, search, page);
    currentSearch = search;
    Connection.Response response = ConnectionUtility.connect(link, false);
    //Connection.Response response = ConnectionUtility.connect(BASE_URL + "/search/" + search.replaceAll(" ", "%20"), false);
    if (!pageExist(response))
      return new ArrayList<>();
    return loadPageByDoc(ConnectionUtility.getPage(response));
  }

  private List<SiteElement> loadPageByDoc(Document document) {
    if (!pageExist(document))
      return new ArrayList<>();
    Element pageDiv = document.getElementsByClass("pages").get(0);
    if (pageDiv != null && pageDiv.hasText())
      setPages(Integer.parseInt(pageDiv.children().last().text()));
    Element list = document.getElementsByClass("tvSeriesList").first();
    clearElements();
    for (Element element : list.children()) {
      addElements(new DreamSubAnime(DreamSubAnimeInformation.getPreviewByElement(element)));
    }
    currentPage = document;
    return getSiteElements();
  }

  @Override
  public List<SiteElement> filter(Genre genre, Sort sortBy, Status status, int page) {
    String link = String.format(getFilterLink(), genre.getName().toLowerCase(), status.getValue(), sortBy.getValue(), page);
    Connection.Response response = ConnectionUtility.connect(link, false);
    if (!pageExist(response))
      return new ArrayList<>();
    currentGenre = genre;
    currentSort = sortBy;
    currentStatus = status;
    return loadPageByDoc(ConnectionUtility.getPage(response));
  }

  @Override
  public boolean pageExist(Connection.Response response) {
    return response.statusCode() != 404;
  }

  private boolean pageExist(Document document) {
    return document != null &&
            (document.getElementsByClass("error").isEmpty() || document.getElementsByClass("error").get(0) == null)
            && (document.getElementsByClass("info").isEmpty() || document.getElementsByClass("info").get(0) == null);
  }

  @Override
  public List<SiteElement> loadPage(int page) {
    return currentPage.location().contains("/search/") ? search(currentSearch, page) : filter(currentGenre, currentSort, currentStatus, page);
  }

  @Override
  public void loadStatuses() {
    addStatuses(DreamSubAnimeStatus.values());
  }

  public enum DreamSubAnimeStatus implements Status {
    TUTTO("Tutto", "tutti"),
    COMPLETATE("Completate", "conclusi"),
    CORSO("In corso", "in-corso"),
    FUTURE("Future release", "prossimamente");

    private final String name;
    private final String value;

    DreamSubAnimeStatus(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getValue() {
      return value;
    }
  }
}
