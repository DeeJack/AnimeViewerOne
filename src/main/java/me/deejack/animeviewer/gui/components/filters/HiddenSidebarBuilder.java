package me.deejack.animeviewer.gui.components.filters;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.async.FilterAsync;
import me.deejack.animeviewer.gui.components.general.HiddenSideBar;
import me.deejack.animeviewer.logic.filters.Filter;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class HiddenSidebarBuilder {
  private HiddenSideBar sideBar;
  private Filter[] filters;
  private HiddenSidebarBuilder previousFilters;
  private HBox controlButton;

  public HiddenSidebarBuilder setPreviousFilter(HiddenSidebarBuilder previousFilters) {
    this.previousFilters = previousFilters;
    return this;
  }

  public HiddenSidebarBuilder setControlButton(HBox controlButton) {
    this.controlButton = controlButton;
    return this;
  }

  public HiddenSideBar build() {
    sideBar = new HiddenSideBar(controlButton);
    loadFilters(previousFilters);
    return sideBar;
  }

  private void loadFilters(HiddenSidebarBuilder previousFilters) {
    new Thread(() -> {
      if (previousFilters == null)
        filters = App.getSite().getFilters();
      else {
        filters = new Filter[previousFilters.getFilters().length];
        for (int i = 0; i < previousFilters.getFilters().length; i++) {
          filters[i] = previousFilters.getFilters()[i].cloneFilter();
        }
      }
      Platform.runLater(this::initialize);
    }).start();
  }

  private void initialize() {
    addChildren();
    sideBar.getSideBar().setSpacing(30);
    sideBar.getSideBar().setPadding(new Insets(20));
    sideBar.getSideBar().setPrefWidth(350);
  }

  public void addChildren() {
    for (Filter filter : filters) {
      if (filter == null)
        continue;
      sideBar.getSideBar().getChildren().add(createFilter(filter));
    }
    Button applyFilterButton = new Button(LocalizedApp.getInstance().getString("ApplyFilterButton"));
    applyFilterButton.setOnAction(onClickApply());
    sideBar.getSideBar().getChildren().add(applyFilterButton);
  }

  private EventHandler<ActionEvent> onClickApply() {
    return (event) -> {
      sideBar.close();
      new Thread(new FilterAsync(this, 1)).start();
    };
  }

  private HBox createFilter(Filter filter) {
    Label label = new Label(filter.getLabel());
    label.setLabelFor(filter.getNode());
    label.setMinWidth(Label.USE_COMPUTED_SIZE);
    label.setEllipsisString(filter.getLabel());
    HBox box = new HBox(label, filter.getNode());
    box.setSpacing(50);
    return box;
  }

  public Filter[] getFilters() {
    return filters.clone();
  }
}
