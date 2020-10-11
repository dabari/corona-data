package de.iconten.client.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.iconten.client.rest.RestClientCroatia;
import de.iconten.client.rest.csv.CsvProvider;
import de.iconten.client.rest.json.JsonProvider;
import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public class DataProviderCroatia extends AbsDataProvider {
	public static final String COUNTRY = "croatia";
	public static final String FEATURE_STATE_NAME = "state";

	protected final RestClientCroatia client = new RestClientCroatia();

	public void writeStatesData() throws Exception {
		final List<DataItem> features = client.getStateDailyData();
		final List<DataItem> dayBeforFeatures = getDayBeforFeatures(calendarToday, COUNTRY, FEATURE_STATE_NAME);
		writeJsonData(FEATURE_STATE_NAME, features, dayBeforFeatures, getJsonFile(COUNTRY, FEATURE_STATE_NAME, calendarToday.getTime()));
		JsonProvider.toJson(features, getJsonFile(COUNTRY, FEATURE_STATE_NAME, calendarToday.getTime()));
		writeLandkreisCsvData(FEATURE_STATE_NAME, features);
	}

	public void writeStatesData(Date date) throws Exception {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		final List<DataItem> features = client.getStateDailyData(date);
		final List<DataItem> dayBeforFeatures = getDayBeforFeatures(calendar, COUNTRY, FEATURE_STATE_NAME);
		writeJsonData(FEATURE_STATE_NAME, features, dayBeforFeatures, getJsonFile(COUNTRY, FEATURE_STATE_NAME, date));
		JsonProvider.toJson(features, getJsonFile(COUNTRY, FEATURE_STATE_NAME, date));
		writeLandkreisCsvData(FEATURE_STATE_NAME, features);
	}

	public static void writeLandkreisCsvData(final String featureType, List<DataItem> features) {

		final List<String[]> data = new ArrayList<>();
		data.add(new String[] { "ObjectId", "Zupanija", "Zarazenih", "Umrlih", "Aktualisiert" });

		for (final DataItem feature : features) {
			data.add(new String[] {
					// @formatter:off
					
					String.valueOf(feature.getObjectId()), 
					feature.getName(), 
					String.valueOf(feature.getCases()),  
					String.valueOf(feature.getDeaths()),
					Util.formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(getCsvFile(COUNTRY, featureType).getAbsolutePath(), data);
	}

}
