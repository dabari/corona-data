package de.iconten.client.rest.json;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public class RKIFeatureDeserializer implements JsonDeserializer<DataItem> {

	public RKIFeatureDeserializer() {

	}

	@Override
	public DataItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		if (jsonObject.has("OBJECTID_1")) {
			// Bundeslanddaten
			return getBundeslandIten(jsonObject);
		} else {
			// Landkreisdaten
			return getLandkreisIten(jsonObject);
		}
	}

	private DataItem getBundeslandIten(JsonObject jsonObject) {
		final DataItem item = new DataItem();
		item.setObjectId(jsonObject.get("OBJECTID").getAsLong());
		item.setObjectId1(jsonObject.get("OBJECTID_1").getAsLong());
		item.setAgs(jsonObject.get("LAN_ew_AGS").getAsString());
		item.setName(jsonObject.get("LAN_ew_GEN").getAsString());
		item.setType(jsonObject.get("LAN_ew_BEZ").getAsString());
		item.setState(item.getName());
		item.setPopulation(jsonObject.get("LAN_ew_EWZ").getAsInt());
		item.setCases(jsonObject.get("Fallzahl").getAsInt());
		item.setDeaths(jsonObject.get("Death").getAsInt());
		item.setLastUpdate(new Date(jsonObject.get("Aktualisierung").getAsLong()));
		return item;
	}

	private DataItem getLandkreisIten(JsonObject jsonObject) {
		final DataItem item = new DataItem();
		item.setObjectId(jsonObject.get("OBJECTID").getAsLong());
		item.setAgs(jsonObject.get("AGS").isJsonNull() ? "" : jsonObject.get("AGS").getAsString());
		item.setName(jsonObject.get("GEN").getAsString());
		item.setType(jsonObject.get("BEZ").getAsString());
		item.setState(jsonObject.get("BL").getAsString());
		item.setPopulation(jsonObject.get("EWZ").getAsInt());
		item.setCases(jsonObject.get("cases").getAsInt());
		item.setDeaths(jsonObject.get("deaths").getAsInt());
		item.setDeathRate(jsonObject.get("death_rate").getAsDouble());
		item.setLastUpdate(getDateFromString(jsonObject.get("last_update").getAsString()));
		return item;
	}

	public Date getDateFromString(String string) throws JsonParseException {
		try {
			final String value = string.split(",")[0];
			return Util.simpleISODateFormat.parse(value.trim());
		} catch (final ParseException e) {
			throw new JsonParseException(e);
		}
	}

}
