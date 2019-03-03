package me.deejack.animeviewer.logic.async.events;

/**
 * A functional interface (so it can be used with lambdas) with a method that is called when the download finishes,
 */
@FunctionalInterface
public interface SuccessListener {
  /**
   * Called when the download finishes successfully
   */
  void onSuccess();
}
