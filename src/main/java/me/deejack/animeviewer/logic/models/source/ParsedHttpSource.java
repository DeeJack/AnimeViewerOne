package me.deejack.animeviewer.logic.models.source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.Anime;
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
      System.out.println(animeElement.text());
      animeList.add(animeFromElement(animeElement));
    }

    return animeList;
  }

  @Override
  public List<Anime> searchAnime(String search, int page) {
    return parseAnimeList(searchAnimeRequest(page, search));
  }

  public abstract String animeSelector();

  public abstract Anime animeFromElement(Element element);
}
