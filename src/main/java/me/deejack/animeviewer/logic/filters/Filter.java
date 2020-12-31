package me.deejack.animeviewer.logic.filters;

import javafx.scene.Node;

public interface Filter {
  String getFilterValue();

  String getLabel();

  Node getNode();

  String getFilterId();

  Filter cloneFilter();
}
