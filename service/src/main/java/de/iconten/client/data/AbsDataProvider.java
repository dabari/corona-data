package de.iconten.client.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.iconten.client.rest.json.JsonProvider;
import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public abstract class AbsDataProvider {
	public static final Calendar calendarToday = Util.getCalendar();

	public static File getCsvFile(String country, String featureType) {
		return new File(Util.USERDIR.getParentFile(), "data/" + country + "/" + featureType + ".csv");
	}

	public static File getJsonFile(String country, String featureType, Date date) {
		final String dateFormat = Util.formatDate(date);
		return new File(Util.USERDIR.getParentFile(), "data/" + country + "/daily/" + featureType + "/" + dateFormat + ".json");
	}

	public static void writeJsonData(final String featureType, final List<DataItem> features, final List<DataItem> dayBeforFeatures, File file)
			throws Exception {
		try {
			final Map<Long, DataItem> collect = dayBeforFeatures.stream().collect(Collectors.toMap(e -> e.getObjectId(), d -> d));

			for (final DataItem todayFeature : features) {

				final DataItem dayBeforFeature = collect.get(todayFeature.getObjectId());
				if (dayBeforFeature == null) {
					throw new Exception("Should not happen!");
				}

				final List<Integer> lastDayCases = todayFeature.getLastDayCases();
				lastDayCases.addAll(dayBeforFeature.getLastDayCases());

				if (lastDayCases.size() == 7) {
					lastDayCases.remove(lastDayCases.size() - 1);
				} else {
					lastDayCases.add(todayFeature.getCases() - dayBeforFeature.getCases());
				}
			}

			JsonProvider.toJson(features, file);
		} catch (final Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static List<DataItem> getDayBeforFeatures(final Calendar calendar, final String country, final String featureType) throws Exception {
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		final File file = getJsonFile(country, featureType, calendar.getTime());
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found!");
		}
		return JsonProvider.fromJson(file);
	}
}
