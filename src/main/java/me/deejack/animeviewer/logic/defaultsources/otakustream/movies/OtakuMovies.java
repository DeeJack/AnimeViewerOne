package me.deejack.animeviewer.logic.defaultsources.otakustream.movies;

import me.deejack.animeviewer.gui.components.filters.Filter;
import me.deejack.animeviewer.gui.components.filters.HiddenSidebarBuilder;
import me.deejack.animeviewer.logic.defaultsources.otakustream.OtakuStreamFilters;
import me.deejack.animeviewer.logic.defaultsources.otakustream.OtakuStreamSource;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

public class OtakuMovies extends OtakuStreamSource {

  @Override
  public Connection.Response filterRequest(int page, HiddenSidebarBuilder filters) {
    StringBuilder url = new StringBuilder(getBaseUrl() + "/movie/page/" + page + "/?");
    for (Filter filter : filters.getFilters()) {
      if (filter.getFilterValue() != null && !filter.getFilterValue().equals(""))
        url.append(filter.getFilterId()).append("=").append(filter.getFilterValue()).append("&");
    }
    return ConnectionUtility.connect(url.substring(0, url.length() - 1), true);
  }

  @Override
  public Filter[] getFilters() {
    return OtakuStreamFilters.getMovieFilters();
  }

  @Override
  public String getName() {
    return "OtakusStream Movie";
  }
}
