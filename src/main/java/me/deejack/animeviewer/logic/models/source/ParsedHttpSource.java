package me.deejack.animeviewer.logic.models.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Contains the methods that let the parse of the document took from the connectionhttp methods
 */
public abstract class ParsedHttpSource extends HttpSource {

  public ParsedHttpSource(String baseUrl, String iconUrl) {
    super(baseUrl, iconUrl);
  }

  @Override
  public List<Anime> parseAnimeList(Connection.Response response) {
    Document document;
    try {
      document = response.parse();
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
    Elements animeElements = document.select(animeSelector());
    List<Anime> animeList = new ArrayList<>();
    for (Element animeElement : animeElements) {
      animeList.add(animeFromElement(animeElement));
    }
    String page = getPagesByDoc(document);
    if (!page.isEmpty())
      GeneralUtility.tryParse(page).ifPresent(this::setPages);
    return animeList;
  }

  protected String getPagesByDoc(Document document) {
    Elements pages = document.select(pagesSelector());
    Element lastPage = pages.get(pages.size() - 1);
    return lastPage == null ? "" : lastPage.text();
  }

  protected abstract String animeSelector();

  protected abstract String pagesSelector();

  public abstract Anime animeFromElement(Element element);
}
