package me.deejack.animeviewer.logic.async.properties;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import me.deejack.animeviewer.logic.async.events.Listener;

public class Property<T> {
  /**
   * A set of listener who will be called when the value changes
   */
  private final Set<Listener<T>> listeners = new HashSet<>();
  /**
   * The current value
   */
  private T value;

  /**
   * @param value The initial value
   */
  public Property(T value) {
    this.value = value;
  }

  public Property() {
  }

  /**
   * Get a unmodifiable list containing the listeners
   *
   * @return A unmodifiable list of the listeners
   */
  public Collection<Listener<T>> getListeners() {
    return Collections.unmodifiableSet(listeners);
  }

  public void addListener(Listener<T> listener) {
    listeners.add(listener);
  }

  public T getValue() {
    return value;
  }

  /**
   * Change the current value and call the registered listeners
   *
   * @param value The new value
   */
  public void setValue(T value) {
    this.value = value;
    for (Listener<T> listener : getListeners())
      listener.onChange(getValue());
  }
}
