package de.iconten.client.rest;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.rest.json.DateFormatTypeAdapter;
import de.iconten.client.rest.json.FeatureDeserializer;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;

public class RestClient {
	private static final String REST_URI = "https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services";
	private final Client client = ClientBuilder.newClient();

	/**
	 * Gibt die Daten aller Landkreise aus
	 * 
	 * @return
	 */
	public List<FeatureLandkreis> getLandkreisData() {
		return getLandkreisData((String[]) null);
	}

	/**
	 * Gibt die Landkreisdaten für objectIds aus.
	 * 
	 * @param objectIds
	 * @return
	 */
	public List<FeatureLandkreis> getLandkreisData(String... objectIds) {
		WebTarget target = client.target(REST_URI).path("/RKI_Landkreisdaten/FeatureServer/0/query");

		target = target.queryParam("f", "json");
		target = target.queryParam("where", "1=1");
		target = target.queryParam("returnGeometry", "false");
		target = target.queryParam("cacheHint", "true");
		target = target.queryParam("orderByFields", "OBJECTID");
		target = target.queryParam("outFields",
				"OBJECTID,RS,AGS,GEN,BEZ,EWZ,AGS_0,BL,county,death_rate,cases,deaths,cases_per_100k,cases_per_population,last_update");

		if (objectIds != null) {
			target = target.queryParam("objectIds", String.join(",", objectIds));
		}

		final Builder requestBuilder = target.request().accept(MediaType.APPLICATION_JSON);
		final List<FeatureLandkreis> features = fromJson(requestBuilder.get(String.class), FeatureLandkreis.class);
		return features;
	}

	/**
	 * Gibt die Daten aller Bundesländer aus
	 * 
	 * @return
	 */
	public List<FeatureBundesland> getBundeslandData() {
		return getBundeslandData((String[]) null);
	}

	/**
	 * Gibt die Bundeslanddaten für objectIds aus.
	 * 
	 * @param objectIds
	 * @return
	 */
	public List<FeatureBundesland> getBundeslandData(String... objectIds) {
		WebTarget target = client.target(REST_URI).path("/Coronafälle_in_den_Bundesländern/FeatureServer/0/query");

		target = target.queryParam("f", "json");
		target = target.queryParam("where", "1=1");
		target = target.queryParam("returnGeometry", "false");
		target = target.queryParam("cacheHint", "true");
		target = target.queryParam("orderByFields", "OBJECTID_1");
		target = target.queryParam("outFields",
				"OBJECTID,OBJECTID_1,LAN_ew_AGS,LAN_ew_GEN,LAN_ew_BEZ,LAN_ew_EWZ,Fallzahl,faelle_100000_EW,Death,Aktualisierung");

		if (objectIds != null) {
			target = target.queryParam("objectIds", String.join(",", objectIds));
		}

		final Builder requestBuilder = target.request().accept(MediaType.APPLICATION_JSON);
		final List<FeatureBundesland> features = fromJson(requestBuilder.get(String.class), FeatureBundesland.class);
		return features;
	}

	private static <T> List<T> fromJson(String content, Class<T> clazz) {
		final GsonBuilder builder = new GsonBuilder();

		final Type mapType = new TypeToken<List<T>>() {
		}.getType();

		builder.registerTypeAdapter(mapType, new FeatureDeserializer<>(clazz));
		builder.registerTypeAdapter(Date.class, new DateFormatTypeAdapter());

		final Gson gson = builder.create();
		final List<T> features = gson.fromJson(content, mapType);
		return features;
	}

}
