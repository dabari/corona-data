package de.iconten.client.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.iconten.client.rest.RestClient;
import de.iconten.client.rest.json.DateFormatTypeAdapter;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;

public class CreateJsonDaily {

	private final RestClient client = new RestClient();

	private static final File USERDIR = new File(System.getProperty("user.dir"));

	private static final Calendar calendarToday = getCalendar();

	@Test
	public void writeLandkreisJsonTest() throws Exception {
		final String featureType = "landkreis";
		try {
			final List<FeatureLandkreis> features = client.getLandkreisData();

			final Type mapType = new TypeToken<List<FeatureLandkreis>>() {
			}.getType();

			final List<FeatureLandkreis> dayBeforFeatures = getDayBeforFeatures(featureType, mapType);

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

			toJson(features, getFile(featureType, calendarToday.getTime()));
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Test
	public void writeBundeslandJsonTest() throws Exception {
		final String featureType = "bundesland";
		try {
			final List<FeatureBundesland> features = client.getBundeslandData();

			final Type mapType = new TypeToken<List<FeatureBundesland>>() {
			}.getType();

			final List<FeatureBundesland> dayBeforFeatures = getDayBeforFeatures(featureType, mapType);

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

			toJson(features, getFile(featureType, calendarToday.getTime()));
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
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

	private static <T> List<T> fromJson(File file, Type mapType) throws Exception {
		final GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(Date.class, new DateFormatTypeAdapter());

		final Gson gson = builder.create();

		try (FileReader reader = new FileReader(file)) {
			final List<T> features = gson.fromJson(reader, mapType);
			return features;
		}
	}

	private <T> List<T> getDayBeforFeatures(String pathSegment, Type mapType) throws Exception {
		final Calendar calendar = getCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		final File file = getFile(pathSegment, calendar.getTime());
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found!");
		}
		return fromJson(file, mapType);
	}

	private static File getFile(String featureType, Date date) {
		final String dateTodayFormat = formatDate(date);
		return new File(USERDIR.getParentFile(), "data/daily/" + featureType + "/" + dateTodayFormat + ".json");
	}

	private static String formatDate(Date date) {
		final String pattern = "yyyy-MM-dd";
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	private static Calendar getCalendar() {
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

}
