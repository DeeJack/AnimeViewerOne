package me.deejack.animeviewer.logic.defaultsources.otakustream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.models.anime.AnimeImpl;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OtakuStreamAnime extends AnimeImpl {

  public OtakuStreamAnime(String url, AnimeInformation animeInformation) {
    super(url, animeInformation);
  }

  @Override
  protected AnimeInformation parseAnimeDetails(Document document) {
    System.out.println(document.select(".ep-list"));
    String plot = document.getElementsByClass("summary").first().getElementsByClass("some-more-info").first().wholeText();
    String genresText = document.getElementsByClass("summary2").first()
            .getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();
    List<Genre> genres = Arrays.stream(genresText.split(", ")).map(Genre::new).collect(Collectors.toList());
    getAnimeInformation().setPlot(plot);
    getAnimeInformation().setGenres(genres);
    return getAnimeInformation();
  }

  @Override
  protected String episodeSelector() {
    return "div.ep-list > ul > li > a";
  }

  @Override
  protected Episode parseEpisode(Element element) {
    String title = element.text();
    int number = GeneralUtility.tryParse(title.split(" ")[1]).orElse(-1);
    System.out.println(element);
    return new OtakuStreamEpisode(title, number, element.attr("href"), null);
  }
}
