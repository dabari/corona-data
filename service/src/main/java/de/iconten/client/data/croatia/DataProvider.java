package de.iconten.client.data.croatia;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import de.iconten.client.rest.model.FeatureLandkreis;

public class DataProvider {

	private final RestClient client = new RestClient();

	public void writeLandkreisData() throws Exception {
		final List<FeatureLandkreis> features = client.getLandkreisData();

	}

	public static class RestClient {
		private static final String REST_URI = "https://www.koronavirus.hr/json/";
		private final Client client = ClientBuilder.newClient();

		public List<FeatureLandkreis> getLandkreisData() {
			WebTarget target = client.target(REST_URI);

			target = target.queryParam("action", "po_danima_zupanijama_zadnji");

			final Builder requestBuilder = target.request().accept(MediaType.APPLICATION_JSON);
			final String result = requestBuilder.get(String.class);

			return new ArrayList<>();
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
}
