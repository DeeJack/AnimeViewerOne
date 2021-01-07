package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.episode.EpisodeImpl;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DreamsubEpisode extends EpisodeImpl {

  public DreamsubEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  @Override
  protected List<StreamingLink> getStreamingLinks(Document document) {
    List<StreamingLink> streamingLinks = new ArrayList<>();

    /*if (document.html().contains("Link download non disponibile")) {
      Elements vvvidEl = document.select("a[href*=\"vvvvid.it\"]");
      System.out.println(vvvidEl.size());
      if (!vvvidEl.isEmpty())
        streamingLinks.add(new StreamingLink("", vvvidEl.get(0).attr("href"), -1, "VVVID", false));
      return streamingLinks;
    }*/
    Elements links = document.select("a.dwButton");

    for (var link : links) {
      streamingLinks.add(new StreamingLink(
              "",
              link.attr("href"),
              GeneralUtility.tryParse(link.text().replaceAll("p", "")).orElse(720),
              "Dreamsub",
              true));
      /*Elements elements = Jsoup.parse(link).getElementsByTag("a");
      if (elements.isEmpty())
        continue;
      Element elementLink = elements.first();
      String url = elementLink.attr("href");
      //if (url.contains("keepem"))
      //url = ConnectionUtility.connect(url, true).url().toString();
      Pattern pattern = Pattern.compile(".*(\\d{4})p|(\\d{3})p.*");
      Matcher matcher = pattern.matcher(url);
      int resolution = matcher.find() ? GeneralUtility.tryParse(matcher.group(1))
              .orElse(GeneralUtility.tryParse(matcher.group(2)).orElse(-1))
              : -1;*/
    }
    return streamingLinks;
  }
}
