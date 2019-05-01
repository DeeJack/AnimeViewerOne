package me.deejack.animeviewer.logic.defaultsources.otakustream;

import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.models.episode.EpisodeImpl;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class OtakuStreamEpisode extends EpisodeImpl {
  public OtakuStreamEpisode(String title, int number, String url, LocalDate releaseDate) {
    super(title, number, url, releaseDate);
  }

  @Override
  protected List<StreamingLink> getStreamingLinks(Document document) {
    Elements players = document.getElementsByClass("player");
    String json = document.getElementsByClass("video-wrap")
            .first().getElementsByTag("script").first()
            .html().replaceAll("var ddData = ", "");
    json = json.substring(0, json.length() - 1);
    OtakuStreamJsonObj[] objects = new GsonBuilder().setLenient().create().fromJson(json, OtakuStreamJsonObj[].class);
    List<StreamingLink> links = new ArrayList<>();
    for (OtakuStreamJsonObj streamJsonObj : objects) {
      if (streamJsonObj == null)
        continue;
      Document streamingSite = ConnectionUtility.getPage("https://otakustream.tv" + streamJsonObj.getUrl(), false);
      String url = streamingSite.getElementsByTag("iframe").first().attr("src");
      links.add(new StreamingLink("en", url, -1, streamJsonObj.getText(), !url.contains("vidlox")));
    }
    return links;
  }
}
