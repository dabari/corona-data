package de.iconten.client.rest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.rest.json.CroatiaItemDeserializer;
import de.iconten.client.rest.json.CroatiaRootDeserializer;
import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public class RestClientCroatia {
	private static final String REST_URI = "https://www.koronavirus.hr/json/";
	private final Client client = ClientBuilder.newClient();
	private static final Calendar calendarToday = Util.getCalendar();

	/**
	 * Gibt die aktuellen Daten aller Bundesländer
	 * 
	 * @param objectIds
	 * @return
	 */
	public List<DataItem> getStateDailyData() {
		WebTarget target = client.target(REST_URI);

		target = target.queryParam("action", "po_danima_zupanijama_zadnji");

		final Builder requestBuilder = target.request().accept(MediaType.APPLICATION_JSON);
		final Map<String, List<DataItem>> fromJson = fromJson(requestBuilder.get(String.class));

		final String key = Util.simpleISODateFormat.format(calendarToday.getTime());

		if (fromJson.containsKey(key)) {
			return fromJson.get(key);
		}

		return new ArrayList<>();
	}

	/**
	 * Gibt die Daten aller dBundesländer an einem bestimmten Tag
	 * 
	 * @param objectIds
	 * @return
	 */
	public List<DataItem> getStateDailyData(Date date) {
		WebTarget target = client.target(REST_URI);

		target = target.queryParam("action", "po_danima_zupanijama");

		final Builder requestBuilder = target.request().accept(MediaType.APPLICATION_JSON);

		final Map<String, List<DataItem>> fromJson = fromJson(requestBuilder.get(String.class));

		final String key = Util.simpleISODateFormat.format(date);

		if (fromJson.containsKey(key)) {
			return fromJson.get(key);
		}

		return new ArrayList<>();
	}

	private static Map<String, List<DataItem>> fromJson(String content) {
		final GsonBuilder builder = new GsonBuilder();

		final Type mapType = new TypeToken<Map<String, List<DataItem>>>() {
		}.getType();

		builder.registerTypeAdapter(mapType, new CroatiaRootDeserializer());
		builder.registerTypeAdapter(DataItem.class, new CroatiaItemDeserializer());

		final Gson gson = builder.create();
		final Map<String, List<DataItem>> features = gson.fromJson(content, mapType);
		return features;
	}

}
