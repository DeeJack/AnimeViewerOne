package me.deejack.animeviewer.logic.anime.dto;

public class KeyValuePair<T, E> {
  /**
   * A key for describing the sort mode
   */
  private final T key;
  /**
   * The value to give to the source as argument
   */
  private final E value;

  public KeyValuePair(T key, E value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Get the value of the current sort mode
   *
   * @return the value of the sort mode
   */
  public E getValue() {
    return value;
  }

  /**
   * Get the key of the current sort mode
   *
   * @return the key of the current sort mode
   */
  public T getKey() {
    return key;
  }

  /**
   * A custom equals method to check the equality between 2 instances
   *
   * @param otherSort An other instance of the class
   * @return true if the 2 instances has equals names and values, false otherwise
   */
  @Override
  public boolean equals(Object otherSort) {
    return otherSort instanceof KeyValuePair &&
            ((KeyValuePair) otherSort).getKey().equals(getKey()) &&
            ((KeyValuePair) otherSort).getValue().equals(getValue());
  }

  /**
   * A custom toString
   *
   * @return the key for this sort mode
   */
  @Override
  public String toString() {
    return getKey().toString();
  }
}
