package de.iconten.client.test;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.data.AbsDataProvider;
import de.iconten.client.data.DataProviderGermany;
import de.iconten.client.rest.RestClientGermany;
import de.iconten.client.rest.json.JsonProvider;
import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.model.FeatureLandkreis;
import de.iconten.client.rest.utils.Util;

public class CreateGermanyDataTest {

	@Test
	public void writeAllDataDailyTest() throws Exception {
		final DataProviderGermany dataProvider = new DataProviderGermany();
		dataProvider.writeLandkreisData();
		dataProvider.writeBundeslandData();
	}

	@Test
	public void writeLandkreisAllDataTest() throws Exception {
		final DataProviderGermany dataProvider = new DataProviderGermany();
		dataProvider.writeLandkreisData();
	}

	@Test
	public void writeLandkreisCsvTest() throws Exception {
		final RestClientGermany client = new RestClientGermany();
		final List<DataItem> features = client.getLandkreisData();
		DataProviderGermany.writeLandkreisCsvData(DataProviderGermany.FEATURE_LANDKREIS_NAME, features);
	}

	@Test
	public void writeLandkreisJsonTest() throws Exception {
		final RestClientGermany client = new RestClientGermany();
		final List<DataItem> features = client.getLandkreisData();

		final List<DataItem> dayBeforFeatures = DataProviderGermany.getDayBeforFeatures(AbsDataProvider.calendarToday, DataProviderGermany.COUNTRY,
				DataProviderGermany.FEATURE_LANDKREIS_NAME);

		DataProviderGermany.writeJsonData(DataProviderGermany.FEATURE_LANDKREIS_NAME, features, dayBeforFeatures, AbsDataProvider
				.getJsonFile(DataProviderGermany.COUNTRY, DataProviderGermany.FEATURE_LANDKREIS_NAME, AbsDataProvider.calendarToday.getTime()));
	}

	@Test
	public void writeBundeslandAllDataTest() throws Exception {
		final DataProviderGermany dataProvider = new DataProviderGermany();
		dataProvider.writeBundeslandData();
	}

	@Test
	public void writeBundeslandCsvTest() throws Exception {
		final RestClientGermany client = new RestClientGermany();
		final List<DataItem> features = client.getBundeslandData();
		DataProviderGermany.writeBundeslandCsvData(DataProviderGermany.FEATURE_BUNDESLAND_NAME, features);
	}

	@Test
	public void writeBundeslandJsonTest() throws Exception {
		final RestClientGermany client = new RestClientGermany();
		final List<DataItem> features = client.getBundeslandData();
		final List<DataItem> dayBeforFeatures = DataProviderGermany.getDayBeforFeatures(AbsDataProvider.calendarToday, DataProviderGermany.COUNTRY,
				DataProviderGermany.FEATURE_BUNDESLAND_NAME);
		DataProviderGermany.writeJsonData(DataProviderGermany.FEATURE_BUNDESLAND_NAME, features, dayBeforFeatures, AbsDataProvider
				.getJsonFile(DataProviderGermany.COUNTRY, DataProviderGermany.FEATURE_BUNDESLAND_NAME, AbsDataProvider.calendarToday.getTime()));
	}

	private static File getJsonFile(String featureType, Date date) {
		final String dateFormat = Util.formatDate(date);
		return new File(Util.USERDIR.getParentFile(), "data/germany/daily/" + featureType + "/" + dateFormat + ".json");
	}

	@Test
	public void ffTest() throws Exception {
		final Calendar calendar = Util.getCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -4);

		final File file = getJsonFile(DataProviderGermany.FEATURE_LANDKREIS_NAME, calendar.getTime());

		final List<FeatureLandkreis> fromJson = fromJson(new File(file.getParentFile(), "_" + file.getName()));

		final Map<Long, FeatureLandkreis> collect = fromJson.stream().collect(Collectors.toMap(e -> e.getObjectId(), d -> d));

		final List<DataItem> fromJson2 = JsonProvider.fromJson(file);

		for (final DataItem dataItem : fromJson2) {
			final FeatureLandkreis featureLandkreis = collect.get(dataItem.getObjectId());

			if (featureLandkreis != null) {
				dataItem.setState(featureLandkreis.getBl());
				dataItem.setDeathRate(featureLandkreis.getDeathRate());
			} else {
				throw new Exception("Fehler" + dataItem.getObjectId());
			}
		}

		JsonProvider.toJson(fromJson2, file);

		System.out.println(fromJson);
	}

	private static List<FeatureLandkreis> fromJson(File file) throws Exception {
		final GsonBuilder builder = new GsonBuilder();

		final Type mapType = new TypeToken<List<FeatureLandkreis>>() {
		}.getType();

		builder.registerTypeAdapter(mapType, new RKIRootDeserializer2());
		builder.registerTypeAdapter(FeatureLandkreis.class, new RKIFeatureDeserializer2());

		final Gson gson = builder.create();

		try (FileReader reader = new FileReader(file)) {
			final List<FeatureLandkreis> features = gson.fromJson(reader, mapType);
			return features;
		}
	}

	private static class RKIRootDeserializer2 implements JsonDeserializer<List<FeatureLandkreis>> {

		public RKIRootDeserializer2() {

		}

		@Override
		public List<FeatureLandkreis> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			final JsonArray features = json.getAsJsonArray();

			final List<FeatureLandkreis> collect = new ArrayList<>();

			for (final JsonElement jsonElement : features) {
				final FeatureLandkreis feature = context.deserialize(jsonElement, FeatureLandkreis.class);
				collect.add(feature);
			}
			return collect;
		}
	}

	private static class RKIFeatureDeserializer2 implements JsonDeserializer<FeatureLandkreis> {
		public RKIFeatureDeserializer2() {
		}

		@Override
		public FeatureLandkreis deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			final JsonObject jsonObject = json.getAsJsonObject();

			// Landkreisdaten
			return getLandkreisIten(jsonObject);

		}

		private FeatureLandkreis getLandkreisIten(JsonObject jsonObject) {
			final FeatureLandkreis item = new FeatureLandkreis();
			item.setObjectId(jsonObject.get("OBJECTID").getAsLong());
			item.setDeathRate(jsonObject.get("death_rate").getAsDouble());
			item.setBl(jsonObject.get("BL").getAsString());
			return item;
		}

	}
}
