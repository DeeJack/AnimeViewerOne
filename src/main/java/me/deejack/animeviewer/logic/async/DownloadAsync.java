package me.deejack.animeviewer.logic.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import me.deejack.animeviewer.logic.async.events.FailListener;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.async.properties.Property;

/**
 * A class to download a file to a location in another thread
 */
public class DownloadAsync implements Runnable {
  /**
   * The location where the file will be placed
   */
  private final File output;
  private final String downloadLink;
  /**
   * True if someone has requested the cancellation of the download
   */
  private final AtomicBoolean cancelled = new AtomicBoolean(false);
  /**
   * The current size of the file
   */
  private final Property<Long> size = new Property<>(0L);
  /**
   * The listener who will be called when the file has been download successfully
   */
  private final Set<SuccessListener> successListeners = new LinkedHashSet<>();
  /**
   * The listener who will be called when something has gone wrong
   */
  private final Set<FailListener> failListeners = new LinkedHashSet<>();
  /**
   * The listener who will be called when the file has been download successfully
   */
  private final Set<Listener<Boolean>> cancelListeners = new LinkedHashSet<>();
  private long totalDownloadSize = 0L;

  public DownloadAsync(File output, String downloadLink) {
    this.output = output;
    this.downloadLink = downloadLink;
  }

  private void createFileIfNotExists() throws IOException {
    if (!output.exists()) {
      output.createNewFile();
    }
  }

  /**
   * Execute the download
   */
  @Override
  public void run() {
    try {
      createFileIfNotExists();
      System.out.println(downloadLink);
      URL url = new URL(downloadLink);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      totalDownloadSize = connection.getContentLength();
      System.out.println(totalDownloadSize);
      try (FileOutputStream stream = new FileOutputStream(output)) {
        try (InputStream inputStream = connection.getInputStream()) {
          byte[] buffer = new byte[1024];
          int length;
          while ((length = inputStream.read(buffer)) != -1) {
            if (cancelled.get()) // If someone has requested the cancellations, interrupt the download
              return;
            stream.write(buffer, 0, length); // Write the buffer to the file
            size.setValue(stream.getChannel().size()); // Update the value of the size
          }
        }
      }
      // When the download has finished, call the success listeners
      for (SuccessListener successListener : successListeners)
        successListener.onSuccess();
    } catch (Exception e) {
      // If something go wrong, call the fail listeners with the exception
      cancelled.set(true);
      for (FailListener failListener : failListeners)
        failListener.onFail(e);
    }
  }

  /*
   * GETTERS
   */

  public void addCancelListener(Listener<Boolean> cancelListener) {
    cancelListeners.add(cancelListener);
  }

  public boolean getCancelled() {
    return cancelled.get();
  }

  public void setCancelled(boolean value) {
    for (Listener<Boolean> listener : cancelListeners)
      listener.onChange(value);
    cancelled.set(value);
  }

  public void addSuccessListener(SuccessListener successListener) {
    successListeners.add(successListener);
  }

  public void addFailListener(FailListener failListener) {
    failListeners.add(failListener);
  }

  public Property<Long> getSizeProperty() {
    return size;
  }

  public long getCurrentSize() {
    return size.getValue();
  }

  public long getTotalDownloadSize() {
    return totalDownloadSize;
  }

  public File getOutput() {
    return output;
  }
}
