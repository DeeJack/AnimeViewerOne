package me.deejack.animeviewer.logic.history;

import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;
import me.deejack.animeviewer.logic.serialization.JsonValidator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class History {
  public static final File CONFIG_DIR = new File(System.getProperty("user.home") + File.separator +
          ".animeviewer");
  public static final File HISTORY_FILE = new File(CONFIG_DIR.getPath() + File.separator + "history.json");
  private static final History instance = new History();
  private static final AnimeSerializer<HistoryElement> serializer = new AnimeSerializer<>(HistoryElement.class);
  private final List<HistoryElement> viewedElements = new ArrayList<>();

  private History() {
  }

  public static History getHistory() {
    return instance;
  }

  public boolean contains(Anime anime) {
    return viewedElements.stream().map(HistoryElement::getViewedElement).anyMatch(element -> element.equals(anime) ||
            element.getUrl().equals(anime.getUrl()));
  }

  public void remove(Anime anime) {
    if (!contains(anime))
      return;
    viewedElements.remove(get(anime).get());
  }

  public Optional<HistoryElement> get(Anime anime) {
    for (HistoryElement historyElement : viewedElements) {
      if (historyElement.getViewedElement().equals(anime) ||
              historyElement.getViewedElement().getUrl().equals(anime.getUrl()))
        return Optional.of(historyElement);
    }
    return Optional.empty();
  }

  public Optional<HistoryEpisode> getLastEpisodeOf(Anime anime) {
    Optional<HistoryElement> element = get(anime);
    return element.map(historyElement -> historyElement.getEpisodesHistory().get(historyElement.getEpisodesHistory().size() - 1));
  }

  public Optional<HistoryEpisode> getHistoryEpisode(HistoryElement historyElement, Episode episode) {
    return historyElement.getEpisodesHistory().stream().filter((historyEpisode -> historyEpisode.getEpisode().equals(episode))).findFirst();
  }

  public void add(HistoryElement element) {
    viewedElements.add(element);
  }

  public List<HistoryElement> getViewedElements() {
    return Collections.unmodifiableList(viewedElements);
  }

  public void saveToFile() throws IOException {
    if (!CONFIG_DIR.exists())
      CONFIG_DIR.mkdir();
    saveToFile(HISTORY_FILE);
  }

  public void saveToFile(File output) throws IOException {
    if (output == null)
      return;
    if (!output.exists())
      output.createNewFile();
    String json = serializer.serialize(viewedElements);
    if (!JsonValidator.isValid(json))
      throw new IOException("Json is invalid!");
    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))) {
      writer.append(json);
      writer.flush();
    }
  }

  public boolean loadFromFile(File inputFile) throws IOException {
    if (inputFile == null || !inputFile.exists())
      return false;
    String json = String.join("\n", Files.readAllLines(inputFile.toPath(), StandardCharsets.UTF_8));
    if (!JsonValidator.isValid(json))
      throw new IOException("Json is invalid!");
    List<HistoryElement> elements = serializer.deserializeList(json);
    sort();

    viewedElements.addAll(elements);
    return true;
  }

  public void sort() {
    viewedElements.sort((firstElement, secondElement) -> secondElement.getEpisodesHistory().get(secondElement.getEpisodesHistory().size() - 1).getViewedDate()
            .compareTo(firstElement.getEpisodesHistory().get(firstElement.getEpisodesHistory().size() - 1).getViewedDate()));
  }

  public boolean loadFromFile() throws IOException {
    return loadFromFile(HISTORY_FILE);
  }
}
