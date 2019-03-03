package me.deejack.animeviewer.logic.anime.dto;

import com.google.gson.annotations.Expose;

/**
 * The class representing a genre in the source
 * For example action anime
 */
public class Genre {
  /**
   * The name which describe the current genre
   */
  @Expose
  private final String name;
  /**
   * The value to send to the source as argument
   */
  private final String value;

  public Genre(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Secondary constructor, the value will be the same as the name
   *
   * @param name The name (and value) of the genre
   */
  public Genre(String name) {
    this(name, name);
  }

  /**
   * Get the name of the current genre
   *
   * @return the name of the current genre
   */
  public String getName() {
    return name;
  }

  /**
   * Get the value of the current genre
   *
   * @return the value of the genre
   */
  public String getValue() {
    return value;
  }

  /**
   * A custom equals method to check the equality between 2 instances
   *
   * @param otherGenre another instance of this class
   * @return true if the names and the values are equals, false otherwise
   */
  @Override
  public boolean equals(Object otherGenre) {
    return otherGenre instanceof Genre &&
            ((Genre) otherGenre).getName().equals(getName()) &&
            ((Genre) otherGenre).getValue().equals(getValue());
  }

  /**
   * A custom override of toString
   *
   * @return the name of the genre
   */
  @Override
  public String toString() {
    return getName();
  }
}
