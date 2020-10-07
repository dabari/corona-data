package de.iconten.client.data.germany;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.rest.RestClient;
import de.iconten.client.rest.csv.CsvProvider;
import de.iconten.client.rest.json.DateFormatTypeAdapter;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;
import de.iconten.client.rest.utils.Util;

public class DataProvider {
	public static final String FEATURE_LANDKREIS_NAME = "landkreis";
	public static final String FEATURE_BUNDESLAND_NAME = "bundesland";

	private final RestClient client = new RestClient();
	private static final Calendar calendarToday = Util.getCalendar();

	/**
	 * 
	 * @throws Exception
	 */
	public void writeLandkreisData() throws Exception {
		final List<FeatureLandkreis> features = client.getLandkreisData();
		final List<FeatureLandkreis> dayBeforFeatures = getDayBeforFeatures(FEATURE_LANDKREIS_NAME, new TypeToken<List<FeatureLandkreis>>() {
		}.getType());

		writeLandkreisJsonData(features, dayBeforFeatures);
		writeLandkreisCsvData(features);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void writeBundeslandData() throws Exception {
		final List<FeatureBundesland> features = client.getBundeslandData();
		final List<FeatureBundesland> dayBeforFeatures = getDayBeforFeatures(FEATURE_BUNDESLAND_NAME, new TypeToken<List<FeatureBundesland>>() {
		}.getType());

		writeBundeslandJsonData(features, dayBeforFeatures);
		writeBundeslandCsvData(features);
	}

	public static void writeLandkreisJsonData(final List<FeatureLandkreis> features, final List<FeatureLandkreis> dayBeforFeatures) throws Exception {
		try {
			final Map<Long, FeatureLandkreis> collect = dayBeforFeatures.stream().collect(Collectors.toMap(e -> e.getObjectId(), d -> d));

			for (final FeatureLandkreis todayFeature : features) {

				final FeatureLandkreis dayBeforFeature = collect.get(todayFeature.getObjectId());
				if (dayBeforFeature == null) {
					throw new Exception("Should not happen!");
				}

				final List<Integer> cases7day = todayFeature.getCases7day();
				if (cases7day.size() == 7) {
					cases7day.remove(cases7day.size() - 1);
				} else {
					cases7day.add(todayFeature.getCases() - dayBeforFeature.getCases());
				}
			}

			toJson(features, getJsonFile(FEATURE_LANDKREIS_NAME, calendarToday.getTime()));
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	public static void writeLandkreisCsvData(List<FeatureLandkreis> features) {

		final List<String[]> data = new ArrayList<>();
		data.add(new String[] { "ObjectId", "Landkreis", "Bundesland", "Infektionen", "Infektionsrate", "Neuinfektionen pro 100k EW", "Todesfälle",
				"Letalitätsrate", "Aktualisiert" });

		for (final FeatureLandkreis feature : features) {
			data.add(new String[] {
					// @formatter:off
					
					String.valueOf(feature.getObjectId()), 
					feature.getGen(), 
					feature.getBl(), 
					String.valueOf(feature.getCases()),  
					String.valueOf(feature.getCasesPerPopulation()),
					String.valueOf(feature.getCasesPer100k()),
					String.valueOf(feature.getDeaths()),
					String.valueOf(feature.getDeathRate()),
					Util.formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(getCsvFile(FEATURE_LANDKREIS_NAME).getAbsolutePath(), data);
	}

	public static void writeBundeslandJsonData(final List<FeatureBundesland> features, final List<FeatureBundesland> dayBeforFeatures)
			throws Exception {
		try {
			final Map<Long, FeatureBundesland> collect = dayBeforFeatures.stream().collect(Collectors.toMap(e -> e.getObjectId(), d -> d));

			for (final FeatureBundesland todayFeature : features) {

				final FeatureBundesland dayBeforFeature = collect.get(todayFeature.getObjectId());
				if (dayBeforFeature == null) {
					throw new Exception("Should not happen!");
				}

				final List<Integer> cases7day = todayFeature.getCases7day();
				if (cases7day.size() == 7) {
					cases7day.remove(cases7day.size() - 1);
				} else {
					cases7day.add(todayFeature.getCases() - dayBeforFeature.getCases());
				}
			}

			toJson(features, getJsonFile(FEATURE_BUNDESLAND_NAME, calendarToday.getTime()));
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void writeBundeslandCsvData(List<FeatureBundesland> features) {

		final List<String[]> data = new ArrayList<>();
		data.add(new String[] { "ObjectId", "Bundesland", "Infektionen", "Infektionsrate", "Neuinfektionen pro 100k EW", "Todesfälle",
				"Letalitätsrate", "Aktualisiert" });

		for (final FeatureBundesland feature : features) {
			data.add(new String[] {
					// @formatter:off
					
					String.valueOf(feature.getObjectId1()), 
					feature.getGen(), 
					String.valueOf(feature.getCases()),  
					String.valueOf("0.0"),
					String.valueOf(feature.getCasesPer100k()),
					String.valueOf(feature.getDeaths()),
					String.valueOf("0.0"),
					Util.formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(getCsvFile(FEATURE_BUNDESLAND_NAME).getAbsolutePath(), data);
	}

	public static <T> List<T> getDayBeforFeatures(String pathSegment, Type mapType) throws Exception {
		final Calendar calendar = Util.getCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		final File file = getJsonFile(pathSegment, calendar.getTime());
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found!");
		}
		return fromJson(file, mapType);
	}

	private static <T> List<T> fromJson(File file, Type mapType) throws Exception {
		final GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(Date.class, new DateFormatTypeAdapter());

		final Gson gson = builder.create();

		try (FileReader reader = new FileReader(file)) {
			final List<T> features = gson.fromJson(reader, mapType);
			return features;
		}
	}

	private static <T> void toJson(List<T> features, File file) throws Exception {
		final GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();

		builder.registerTypeAdapter(Date.class, new DateFormatTypeAdapter());

		final Gson gson = builder.create();

		try (FileWriter writer = new FileWriter(file)) {
			gson.toJson(features, writer);
		}
	}

	private static File getCsvFile(String featureType) {
		return new File(Util.USERDIR.getParentFile(), "data/germany/" + featureType + ".csv");
	}

	private static File getJsonFile(String featureType, Date date) {
		final String dateFormat = Util.formatDate(date);
		return new File(Util.USERDIR.getParentFile(), "data/germany/daily/" + featureType + "/" + dateFormat + ".json");
	}

}
