package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.SiteElement;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Sort;
import me.deejack.animeviewer.logic.anime.dto.Status;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnimeLeggendariSite extends Site {
  private Document homePage; // da mettere nel padre?
  private Document currentPage;
  private Genre currentGenre;
  private Sort currentSort;
  private Status currentStatus;
  private String currentSearch; // Mettere robe nella superclasse, fare una interface?

  public AnimeLeggendariSite() {
    super("AnimeLeggendari", "https://animeleggendari.com/",
            "https://animeleggendari.com/page/%s/?s=", "https://animeleggendari.com/wp-content/uploads/2018/12/bg00.jpg");
  }

  @Override
  public List<SiteElement> search(String search, int page) throws NoConnectionException {
    String link = String.format(getFilterLink(), page) + search.replaceAll(" ", "+");
    Connection.Response response = ConnectionUtility.connect(link, false); // Controllare se va bene per più spazi, tab ecc
    if (!pageExist(response))
      return new ArrayList<>();
    currentSearch = search;
    return loadPageByDoc(ConnectionUtility.getPage(response));
  }

  @Override
  public void loadHomePage() throws NoConnectionException {
    Connection.Response response = ConnectionUtility.connect(getBaseUrl(), false);
    if (!pageExist(response))
      return;
    homePage = ConnectionUtility.getPage(response);
  }

  @Override
  public void loadGenreList() {
    Elements genres = homePage.getElementById("cat").getElementsByTag("option");
    for (int i = 1; i < genres.size(); i++)
      addGenre(new Genre(genres.get(i).text(), genres.get(i).text()));
  }

  @Override
  public void loadSortByList() {
    addSort(new Sort("Tutto", ""));
  }

  public List<SiteElement> loadPageByDoc(Document document) {
    if (document.getElementsByClass("nav-links").isEmpty()) setPages(1);
    else {
      Element pageDiv = document.getElementsByClass("nav-links").get(0);
      if (pageDiv != null && pageDiv.hasText()) {
        if (GeneralUtility.tryParse(pageDiv.children().get(pageDiv.children().size() - 1).text()).isPresent())
          setPages(Integer.parseInt(pageDiv.children().get(pageDiv.children().size() - 1).text()));
        else
          setPages(Integer.parseInt(pageDiv.children().get(pageDiv.children().size() - 2).text()));
      }
    }

    Elements rows = document.getElementById("main-content").getElementsByClass("mh-row");
    clearElements();
    currentPage = document;
    for (Element row : rows) {
      for (Element child : row.children()) {
        addElements(new AnimeLeggendariAnime(
                AnimeLeggendariAnimeInformation.getPreviewByElement(child.getElementsByTag("article").first())));
      }
    }
    return getSiteElements();
  }

  @Override
  public List<SiteElement> filter(Genre genre, Sort sort, Status status, int page) throws NoConnectionException {
    String url = getBaseUrl() + "category/" + genre.getName().toLowerCase().replaceAll(" ", "-");
    url += page != 1 ? "/page/" + page + "/" : "";
    if (!url.endsWith("/")) url += "/";
    Connection.Response response = ConnectionUtility.connect(url, false);
    if (!pageExist(response))
      return new ArrayList<>();
    currentGenre = genre;
    currentSort = sort;
    currentStatus = status;
    return loadPageByDoc(ConnectionUtility.getPage(response));
  }

  @Override
  public List<SiteElement> loadPage(int page) {
    return currentPage.location().contains("/?s=") ?
            search(currentSearch, page) :
            filter(currentGenre, currentSort, currentStatus, page);
  }

  @Override
  public boolean pageExist(Connection.Response response) {
    return response.statusCode() != 404;
  }

  @Override
  public void loadStatuses() {
    addStatuses(AnimeLeggendariStatus.values());
  }

  public enum AnimeLeggendariStatus implements Status {
    TUTTO;

    @Override
    public String getName() {
      return "Tutto";
    }

    @Override
    public String getValue() {
      return "";
    }
  }
}
