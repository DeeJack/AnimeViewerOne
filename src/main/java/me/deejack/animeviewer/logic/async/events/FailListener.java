package me.deejack.animeviewer.logic.async.events;

/**
 * A functional interface (so it can be used with lambdas) with a method that is called when something go wrong,
 * with the exception as a parameter
 */
@FunctionalInterface
public interface FailListener {
  /**
   * Called when something go wrong
   *
   * @param exception The exception of what has happened
   */
  void onFail(Exception exception);
}
