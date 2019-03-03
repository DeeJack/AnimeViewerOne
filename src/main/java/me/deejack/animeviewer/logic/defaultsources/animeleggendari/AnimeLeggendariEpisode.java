package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnimeLeggendariEpisode extends Episode {
  public AnimeLeggendariEpisode(String title, int number, String pageLink) {
    super(title, number, pageLink, null);
  }

  @Override
  public List<StreamingLink> getStreamingLinks() {
    List<StreamingLink> links = new ArrayList<>();
    Document page = ConnectionUtility.getPage(getUrl(), false);
    Elements aNodes = page.getElementsByTag("a");
    for (Element a : aNodes) {
      if (a.text().equalsIgnoreCase("openload"))
        links.add(new StreamingLink("?", a.attr("href"), -1, "Openload"));
      else if (a.text().equalsIgnoreCase("streamango"))
        links.add(new StreamingLink("?", a.attr("href"), -1, "Streamango"));
    }
    return links;
  }

  @Override
  public boolean hasNextEpisode() {
    return false;
  }

  @Override
  public Episode getNextEpisode() {
    return null;
  }
}
