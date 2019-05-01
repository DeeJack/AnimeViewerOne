package me.deejack.animeviewer.logic.defaultsources.otakustream;

import java.util.LinkedHashMap;
import java.util.Map;
import me.deejack.animeviewer.gui.components.filters.ComboBoxFilter;
import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.MultiSelectionFilter;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.nodes.Document;

public final class OtakuStreamFilters {

  private OtakuStreamFilters() {
  }

  public static Filter[] getFilters() {
    Document page = ConnectionUtility.getPage("https://otakustream.tv/anime/?", false);
    return new Filter[]{
            getGenres(page),
            getPremiere(page),
            getStatus(page),
            getLang(page),
            getSort(page)
    };
  }

  private static Filter getGenres(Document page) {
    Map<String, String> items = getItemsFromUL(page, "genre", "");
    System.out.println(items);
    return new MultiSelectionFilter("genre", "Genres", items);
  }

  private static Filter getPremiere(Document page) {
    Map<String, String> items = getItemsFromUL(page, "premiere", "");
    System.out.println(items);
    return new MultiSelectionFilter("premiered", "Premiere", items);
  }

  private static Filter getStatus(Document page) {
    Map<String, String> items = getItemsFromUL(page, "sort", ":nth-last-child(4)");
    System.out.println(items);
    return new ComboBoxFilter("anime_status", "Status", items);
  }

  private static Filter getLang(Document page) {
    Map<String, String> items = getItemsFromUL(page, "sort", ":nth-last-child(3)");
    System.out.println(items);
    return new ComboBoxFilter("language", "Language", items);
  }

  private static Filter getSort(Document page) {
    Map<String, String> items = getItemsFromUL(page, "sort", ":nth-last-child(2)");
    System.out.println(items);
    return new ComboBoxFilter("sort_by", "Sort", items);
  }

  private static Map<String, String> getItemsFromUL(Document page, String cssClass, String options) {
    Map<String, String> items = new LinkedHashMap<>();
    page.select("div#custom-filter > form.filters > div.filter" + options + " > ul." + cssClass + " > li")
            .forEach((element) -> items.put(element.wholeText(),
                    element.select("input").attr("value")));
    return items;
  }
}
