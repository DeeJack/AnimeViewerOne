package me.deejack.animeviewer.logic.serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import me.deejack.animeviewer.logic.anime.Episode;

public class EpisodeTypeAdapter implements JsonSerializer<Collection<Episode>>, JsonDeserializer<Collection<Episode>> {
  @Override
  public JsonElement serialize(Collection<Episode> episodes, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonArray array = new JsonArray();
    for (Episode episode : episodes) {
      if (episode.getSecondsWatched() > 0) {
        array.add(new GeneralTypeAdapter<Episode>().serialize(episode, type, jsonSerializationContext));
      }
    }
    return array;
  }

  @Override
  public Collection<Episode> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return new GeneralListTypeAdapter<Episode>().deserialize(jsonElement, type, jsonDeserializationContext);
  }
}