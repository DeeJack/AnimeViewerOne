package me.deejack.animeviewer.logic.models.anime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Definisco i vari metodi ma non so ancora come selezionare un episodio o il dettaglio, e non so come fare a
 * trasformarlo in un'istanza dopo averlo trovato quindi altri metodi astratti
 */
public abstract class ParsedHttpAnime extends HttpAnime {

  public ParsedHttpAnime(String url) {
    super(url);
  }

  @Override
  public List<Episode> parseEpisodes(Connection.Response response) {
    Elements episodesElements;
    try {
      episodesElements = response.parse().select(episodeSelector());
    } catch (IOException e) {
      throw new RuntimeException("Error parsing episodes, response status: " + response.statusCode() + "(" + response.statusMessage() + ")", e);
    }
    List<Episode> episodes = new ArrayList<>();
    episodesElements.stream().map(this::parseEpisode).forEach(episodes::add);
    return episodes;
  }

  @Override
  public AnimeInformation parseAnimeDetails(Connection.Response response) {
    try {
      return parseAnimeDetails(response.parse());
    } catch (IOException e) {
      throw new RuntimeException("Error parsing details, response status: " + response.statusCode() + "(" + response.statusMessage() + ")", e);
    }
  }

  public abstract AnimeInformation parseAnimeDetails(Document document);

  public abstract String episodeSelector();

  public abstract Episode parseEpisode(Element element);
}
