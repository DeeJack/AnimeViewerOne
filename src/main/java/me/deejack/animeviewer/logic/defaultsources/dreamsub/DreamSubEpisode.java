package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO: Streamango, film, correlati
public class DreamSubEpisode extends Episode {
  private Document document;

  public DreamSubEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  public List<StreamingLink> getDreamSubLinks() throws NoConnectionException {
    if (document == null)
      document = ConnectionUtility.getPage(getUrl(), false);
    List<StreamingLink> streamingLinks = new ArrayList<>();

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

  @Override
  public boolean hasNextEpisode() {
    return false;
  }

  @Override
  public Episode getNextEpisode() {
    return null;
  }

  @Override
  public List<StreamingLink> getStreamingLinks() throws NoConnectionException {
    return getDreamSubLinks();
  }
}
