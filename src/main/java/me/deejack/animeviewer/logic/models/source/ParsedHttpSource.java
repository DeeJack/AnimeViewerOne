package me.deejack.animeviewer.logic.models.source;

import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

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
      logError(e);
      return new ArrayList<>();
    }
    Elements animeElements = document.select(animeSelector());
    List<Anime> animeList = new ArrayList<>();
    for (Element animeElement : animeElements) {
      animeList.add(animeFromElement(animeElement));
    }
    String page = getPagesByDoc(document);
    GeneralUtility.tryParse(page).ifPresent(this::setPages);
    return animeList;
  }

  protected String getPagesByDoc(Document document) {
    Elements pages = document.select(pagesSelector());
    return pages.isEmpty() ? "1" : pages.get(pages.size() - 1).text();
  }

  protected abstract String animeSelector();

  protected abstract String pagesSelector();

  public abstract Anime animeFromElement(Element element);
}
