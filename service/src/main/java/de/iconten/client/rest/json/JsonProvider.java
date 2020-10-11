package de.iconten.client.rest.json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.rest.model.DataItem;

public class JsonProvider {

	public static void toJson(List<DataItem> features, File file) throws Exception {
		final GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		builder.setPrettyPrinting();

		final Gson gson = builder.create();

		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(features, writer);
		}
	}

	public static List<DataItem> fromJson(File file) throws Exception {
		final GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");

		final Type mapType = new TypeToken<List<DataItem>>() {
		}.getType();

		final Gson gson = builder.create();

		try (FileReader reader = new FileReader(file)) {
			final List<DataItem> features = gson.fromJson(reader, mapType);
			return features;
		}
	}
}
