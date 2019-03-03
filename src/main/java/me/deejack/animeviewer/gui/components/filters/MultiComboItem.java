package me.deejack.animeviewer.gui.components.filters;

import javafx.beans.property.SimpleBooleanProperty;

public class MultiComboItem {
  private final SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(false);
  private final String text;

  public MultiComboItem(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public SimpleBooleanProperty getBooleanProperty() {
    return booleanProperty;
  }

  public void setBoolean(boolean bool) {
    booleanProperty.set(bool);
  }

  @Override
  public String toString() {
    return text;
  }
}
