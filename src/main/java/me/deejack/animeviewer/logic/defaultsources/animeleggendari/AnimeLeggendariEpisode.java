package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.episode.EpisodeImpl;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnimeLeggendariEpisode extends EpisodeImpl {
  private final String animeTitle;

  public AnimeLeggendariEpisode(String title, int number, String url, LocalDate releaseDate, String animeTitle) {
    super(title, number, url, releaseDate);
    this.animeTitle = animeTitle;
  }

  @Override
  protected List<StreamingLink> getStreamingLinks(Connection.Response response) {
    List<StreamingLink> streamingLinks = new ArrayList<>();
    Document document;
    try {
      document = response.parse();
    } catch (Exception exc) {
      exc.printStackTrace();
      return null;
    }
    Elements streamingFrames = document.getElementsByTag("iframe");
    if (streamingFrames.isEmpty()) {
      Elements scripts = document.select("div.entry-content > p > script");
      scripts.stream()
              .map(Element::dataNodes)
              .forEach((dataNodes) ->
                      dataNodes.stream()
                              .map(DataNode::getWholeData)
                              .map(this::decode)
                              .map(this::getStreamingUrl)
                              .map((link) -> new StreamingLink(animeTitle.contains("SUB") ? "SUB ITA" : "ITA", link, -1, link.split("/")[2]))
                              .forEach(streamingLinks::add));
    }
    streamingFrames.stream()
            .map((frame) -> frame.attr("src"))
            .map(link -> new StreamingLink(animeTitle.contains("SUB") ? "SUB ITA" : "ITA", link, -1, link.split("/")[2]))
            .forEach(streamingLinks::add);
    return streamingLinks;
  }

  private String decode(String script) {
    try {
      System.out.println(script);
      return URLDecoder.decode(script.replaceAll("@", "%").trim().split(" ")[0], "US-ASCII");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "123";
    }
  }

  private String getStreamingUrl(String decodedScript) {
    Elements iframe = Jsoup.parse(decodedScript).getElementsByTag("iframe");
    if (!iframe.isEmpty())
      return iframe.first().attr("src");
    return "";
  }

  @Override
  public Connection.Response episodePageRequest() {
    return ConnectionUtility.connect(getUrl(), true);
  }
}
