package de.iconten.client.rest.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public class CroatiaRootDeserializer implements JsonDeserializer<Map<String, List<DataItem>>> {

	public CroatiaRootDeserializer() {

	}

	@Override
	public Map<String, List<DataItem>> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonArray days = json.getAsJsonArray();

		final Map<String, List<DataItem>> map = new HashMap<>();

		for (final JsonElement day : days) {
			final JsonObject jsonObject = day.getAsJsonObject();
			final Date lastUpdate = getDateFromString(jsonObject.get("Datum").getAsString());
			final String key = Util.simpleISODateFormat.format(lastUpdate);

			if (!map.containsKey(key)) {
				map.put(key, extracted(context, jsonObject, lastUpdate));
			} else {
				System.out.println("Contains " + key);
			}
		}
		return map;
	}

	private List<DataItem> extracted(JsonDeserializationContext context, final JsonObject jsonObject, final Date lastUpdate) {
		final JsonArray dataArray = jsonObject.get("PodaciDetaljno").getAsJsonArray();

		final List<DataItem> collect = new ArrayList<>();

		for (int i = 0; i < dataArray.size(); i++) {
			final JsonElement jsonElement = dataArray.get(i);
			final DataItem item = context.deserialize(jsonElement, DataItem.class);
			item.setLastUpdate(lastUpdate);
			item.setObjectId(i + 1);
			item.setObjectId1(item.getObjectId());
			collect.add(item);
		}

		return collect;
	}

	private static Date getDateFromString(String date) {
		try {
			return Util.simpleISODateTimeFormat.parse(date.trim());
		} catch (final ParseException e) {
			throw new JsonParseException(e);
		}
	}

}
