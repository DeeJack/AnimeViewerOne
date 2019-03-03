package me.deejack.animeviewer.logic.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class GeneralTypeAdapter<T> implements JsonSerializer, JsonDeserializer {
  @Override
  public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return new GeneralListTypeAdapter<T>().deserialize(jsonElement, type, jsonDeserializationContext).get(0);
  }

  @Override
  public JsonElement serialize(Object jsonObject, Type jsonType, JsonSerializationContext jsonSerializationContext) {
    return new GeneralListTypeAdapter<T>().serialize(jsonObject, jsonType, jsonSerializationContext);
  }
}
