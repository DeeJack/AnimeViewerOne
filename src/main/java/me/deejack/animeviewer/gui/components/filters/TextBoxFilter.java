package me.deejack.animeviewer.gui.components.filters;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class TextBoxFilter extends TextField implements Filter {
  private final String textLabel;
  private final String filterId;

  public TextBoxFilter(String filterId, String textLabel, String promptText) {
    this.textLabel = textLabel;
    this.filterId = filterId;
    setPromptText(promptText);
  }

  public TextBoxFilter(String filterId, String textLabel) {
    this(filterId, textLabel, "");
  }

  @Override
  public String getFilterValue() {
    return getText();
  }

  @Override
  public String getLabel() {
    return textLabel;
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public String getFilterId() {
    return filterId;
  }

  @Override
  public Filter cloneFilter() {
    return new TextBoxFilter(filterId, textLabel, getPromptText());
  }
}
