package me.deejack.animeviewer.logic.async.events;

@FunctionalInterface
public interface Listener<T> {
  void onChange(T t);
}
