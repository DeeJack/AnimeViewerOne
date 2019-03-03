package me.deejack.animeviewer.logic.models.source.asd;

import java.util.List;
import me.deejack.animeviewer.gui.components.filters.FilterList;
import me.deejack.animeviewer.logic.anime.Anime;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.models.source.ParsedHttpSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;

public class DreamSubSource extends ParsedHttpSource {

  public DreamSubSource() {
    super("https://www.dreamsub.stream");
  }

  public static void main(String[] args) {
    new DreamSubSource().searchAnime("one", 1);
  }

  @Override
  public String animeSelector() {
    return "div.tvSeriesList > li";
  }

  @Override
  public Anime animeFromElement(Element element) {
    String name = element.getElementsByClass("tvTitle").first().text();
    String url = element.getElementsByClass("showStreaming").first()
            .getElementsByTag("a").get(1).attr("href");
    String imageUrl = getBaseUrl().substring(0, getBaseUrl().length() - 1) +
            element.getElementsByClass("cover").get(0).attr("style")
                    .replaceAll("background: url\\(", "").replaceAll("\\) no-repeat center", "");

    return new DreamsubAnime(name, url, url.contains("/movie/"), imageUrl);
  }

  @Override
  public Connection.Response popularAnimeRequest(int page) {
    return null;
  }

  @Override
  public Connection.Response searchAnimeRequest(int page, String search) {
    return ConnectionUtility.connect(getBaseUrl() + "/search/" + search + "?page=" + page, false);
  }

  @Override
  public Connection.Response filterRequest(int page, FilterList filters) {
    return null;
  }

  @Override
  public Anime parseAnimeDetails(Connection.Response response) {
    return null;
  }

  @Override
  public List<Episode> parseEpisodes(Connection.Response response) {
    return null;
  }

  @Override
  public Connection.Response episodeListRequest(Anime anime) {
    return null;
  }

  @Override
  public Connection.Response getPagesRequest() {
    return null;
  }

  @Override
  public List<Anime> filter(int page, FilterList filters) {
    return null;
  }

  @Override
  public int getPages() {
    return 0;
  }

  @Override
  public String getName() {
    return "Dreamsub";
  }
}
