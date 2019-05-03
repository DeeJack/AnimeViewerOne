package me.deejack.animeviewer.logic.favorite;

import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;
import me.deejack.animeviewer.logic.serialization.JsonValidator;
import me.deejack.animeviewer.logic.utils.FilesManager;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.deejack.animeviewer.logic.history.History.CONFIG_DIR;

public final class Favorite {
  private static final Favorite instance = new Favorite();
  private final Set<FavoriteAnime> favorites = new LinkedHashSet<>();
  private final AnimeSerializer<FavoriteAnime> serializer = new AnimeSerializer<>(FavoriteAnime.class);

  private Favorite() {
  }

  public static Favorite getInstance() {
    return instance;
  }

  public void addFavorite(Anime element) {
    FavoriteAnime favorite = new FavoriteAnime(element);
    favorites.add(favorite);
    favorite.getAnime().saveImageToFile(favorite.getImagePath());
  }

  public void removeFavorite(Anime animeToRemove) {
    for (FavoriteAnime anime : favorites) {
      if (anime.getAnime().equals(animeToRemove)) {
        favorites.remove(anime);
        if (anime.getImagePath().exists())
          anime.getImagePath().delete();
        return;
      }
    }
  }

  public Set<FavoriteAnime> getFavorites() {
    return Collections.unmodifiableSet(favorites);
  }

  public boolean saveToFile() throws IOException {
    if (!CONFIG_DIR.exists())
      CONFIG_DIR.mkdir();
    return saveToFile(new File(CONFIG_DIR.getPath() + File.separator + "favorites.json"));
  }

  public boolean saveToFile(File output) throws IOException {
    if (output == null)
      return false;
    String json = serializer.serialize(new ArrayList<>(favorites));
    if (!JsonValidator.isValid(json))
      throw new IOException("Json is invalid!");
    FilesManager.writeToFile(output, json);
    return true;
  }

  private void loadEpisodes() {
    favorites.forEach((favoriteAnime) -> favoriteAnime.getAnime().getEpisodes().addAll(favoriteAnime.getEpisodes()));
  }

  public boolean loadFromFile() throws IOException {
    return loadFromFile(new File(CONFIG_DIR + File.separator + "favorites.json"));
  }

  public boolean loadFromFile(File input) throws IOException {
    if (!input.exists())
      return false;
    String json = FilesManager.readFromFile(input);
    if (!JsonValidator.isValid(json))
      throw new IOException("Json invalid!");
    List<FavoriteAnime> elements = serializer.deserializeList(json);
    if (!elements.isEmpty())
      FavoriteAnime.setCounter(elements.get(elements.size() - 1).getId() + 1);

    favorites.addAll(elements);
    loadEpisodes();
    return true;
  }

  public boolean contains(Anime anime) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getAnime().getUrl().equals(anime.getUrl()))
        return true;
    return false;
  }

  public Optional<FavoriteAnime> get(String url) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getAnime().getUrl().equals(url))
        return Optional.of(favorite);
    return Optional.empty();
  }

  public Optional<FavoriteAnime> get(int id) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getId() == id)
        return Optional.of(favorite);
    return Optional.empty();
  }
}
