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

import de.iconten.client.rest.model.DataItem;

public class RKIRootDeserializer implements JsonDeserializer<List<DataItem>> {

	public RKIRootDeserializer() {

	}

	@Override
	public List<DataItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		final JsonArray features = jsonObject.get("features").getAsJsonArray();

		final List<DataItem> collect = new ArrayList<>();

		for (final JsonElement jsonElement : features) {
			final JsonObject jsonObject1 = jsonElement.getAsJsonObject();
			final JsonElement attributesElement = jsonObject1.get("attributes");
			final DataItem feature = context.deserialize(attributesElement, DataItem.class);
			collect.add(feature);
		}
		return collect;
	}

}
