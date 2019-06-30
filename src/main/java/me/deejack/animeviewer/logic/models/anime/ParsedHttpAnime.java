package me.deejack.animeviewer.logic.models.anime;

import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.models.episode.Episode;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Definisco i vari metodi ma non so ancora come selezionare un episodio o il dettaglio, e non so come fare a
 * trasformarlo in un'istanza dopo averlo trovato quindi altri metodi astratti
 */
public abstract class ParsedHttpAnime extends HttpAnime {

  public ParsedHttpAnime(String url) {
    super(url);
  }

  @Override
  protected List<Episode> parseEpisodes(Connection.Response response) {
    Elements episodesElements;
    try {
      episodesElements = response.parse().select(episodeSelector());
    } catch (IOException e) {
      throw new RuntimeException("Error parsing episodes, response status: " + response.statusCode() + "(" + response.statusMessage() + ")", e);
    }
    System.out.println(episodesElements.size());
    List<Episode> episodes = new ArrayList<>();
    episodesElements.stream().map(this::parseEpisode).forEach(episodes::add);
    afterEpisodeLoaded(episodes);
    return episodes;
  }

  @Override
  protected AnimeInformation parseAnimeDetails(Connection.Response response) {
    try {
      if (response.statusCode() != 200)
        throw new HttpStatusException("HTTP ERROR: " + response.statusCode(), response.statusCode(), response.url().toString());
      return parseAnimeDetails(response.parse());
    } catch (IOException e) {
      throw new RuntimeException("Error parsing details, response status: " + response.statusCode() + "(" + response.statusMessage() + ")", e);
    }
  }

  protected abstract AnimeInformation parseAnimeDetails(Document document);

  protected abstract String episodeSelector();

  protected abstract Episode parseEpisode(Element element);
}
