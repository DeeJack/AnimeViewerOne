package me.deejack.animeviewer.gui.components.filters;

import javafx.scene.Node;

public interface Filter {
  String getFilterValue();

  String getLabel();

  Node getNode();

  String getFilterId();

  Filter cloneFilter();
}
