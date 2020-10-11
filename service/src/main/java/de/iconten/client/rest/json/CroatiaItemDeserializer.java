package de.iconten.client.rest.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.iconten.client.rest.model.DataItem;

public class CroatiaItemDeserializer implements JsonDeserializer<DataItem> {

	public CroatiaItemDeserializer() {

	}

	@Override
	public DataItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		return getItem(jsonObject);
	}

	private DataItem getItem(JsonObject jsonObject) {
		final DataItem item = new DataItem();

		item.setName(jsonObject.get("Zupanija").getAsString());
		item.setType("Zupanija");
		item.setState(item.getName());
		//item.setPopulation(jsonObject.get("EWZ").getAsInt());
		item.setCases(jsonObject.get("broj_zarazenih").getAsInt());
		item.setDeaths(jsonObject.get("broj_umrlih").getAsInt());
		item.setAgs(jsonObject.get("broj_aktivni").getAsString());
		//item.setDeathRate(jsonObject.get("death_rate").getAsDouble());
		return item;
	}
}
