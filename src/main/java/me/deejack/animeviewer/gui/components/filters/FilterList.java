package me.deejack.animeviewer.gui.components.filters;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.FilterAsync;
import me.deejack.animeviewer.gui.components.general.HiddenSideBar;
import me.deejack.animeviewer.gui.utils.LocalizedApp;

public class FilterList {
  private final Filter[] filters;
  private final HiddenSideBar sideBar;

  public FilterList(Button controlButton, FilterList previousFilters) {
    sideBar = new HiddenSideBar(controlButton);
    if (previousFilters != null) {
      filters = new Filter[previousFilters.getFilters().length];
      for (int i = 0; i < previousFilters.getFilters().length; i++) {
        filters[i] = previousFilters.getFilters()[i].cloneFilter();
      }
    } else filters = App.getSite().getFilters();
    initialize();
  }

  private void initialize() {
    addChildren();
    sideBar.setSpacing(30);
    sideBar.setPadding(new Insets(20));
    sideBar.setPrefWidth(200);
  }

  public void addChildren() {
    for (Filter filter : filters) {
      Label label = new Label(filter.getLabel());
      label.setLabelFor(filter.getNode());
      HBox box = new HBox(label, filter.getNode());
      box.setSpacing(50);

      sideBar.getChildren().add(box);
    }
    Button button = new Button(LocalizedApp.getInstance().getString("ApplyFilterButton"));
    button.setOnAction((event) -> new Thread(new FilterAsync(this, 1)).start());
    sideBar.getChildren().add(button);
  }

  public Filter[] getFilters() {
    return filters;
  }

  public HiddenSideBar getSideBar() {
    return sideBar;
  }
}
