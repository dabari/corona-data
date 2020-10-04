package de.iconten.client.rest.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class FeatureDeserializer<T> implements JsonDeserializer<List<T>> {
	private final Class<T> clazz;

	public FeatureDeserializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		final JsonArray features = jsonObject.get("features").getAsJsonArray();

		final List<T> collect = new ArrayList<>();

		for (final JsonElement jsonElement : features) {
			final JsonObject jsonObject1 = jsonElement.getAsJsonObject();
			final JsonElement attributesElement = jsonObject1.get("attributes");
			final T feature = context.deserialize(attributesElement, clazz);
			collect.add(feature);
		}
		return collect;
	}

}
