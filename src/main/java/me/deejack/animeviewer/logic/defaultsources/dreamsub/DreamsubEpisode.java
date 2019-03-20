package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.episode.EpisodeImpl;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DreamsubEpisode extends EpisodeImpl {

  public DreamsubEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  @Override
  protected List<StreamingLink> getStreamingLinks(Connection.Response response) {
    List<StreamingLink> streamingLinks = new ArrayList<>();
    Document document = null;
    try {
      document = response.parse();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    if (document.html().contains("Link download non disponibile"))
      return streamingLinks;
    String episodeInfo = document.html().substring(document.html().indexOf("LINK DOWNLOAD") + "LINK DOWNLOAD</b>:\n".length());
    String[] links = episodeInfo.substring(0,
            episodeInfo.indexOf("<br>")).split(" - ");

    for (String link : links) {
      Elements elements = Jsoup.parse(link).getElementsByTag("a");
      if (elements.isEmpty())
        continue;
      Element elementLink = elements.first();
      String url = elementLink.attr("href");
      if (url.contains("keepem"))
        url = ConnectionUtility.connect(url, true).url().toString();
      Pattern pattern = Pattern.compile(".*(\\d{4})p|(\\d{3})p.*");
      Matcher matcher = pattern.matcher(url);
      int resolution = matcher.find() ? GeneralUtility.tryParse(matcher.group(1))
              .orElse(GeneralUtility.tryParse(matcher.group(2)).orElse(-1))
              : -1;
      streamingLinks.add(new StreamingLink(
              url.contains("SUB") ? "Sub Ita" : "Ita",
              url,
              resolution,
              elementLink.text()));
    }
    return streamingLinks;
  }
}
