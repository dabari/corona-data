package de.iconten.client.data;

import java.util.ArrayList;
import java.util.List;

import de.iconten.client.rest.RestClientGermany;
import de.iconten.client.rest.csv.CsvProvider;
import de.iconten.client.rest.model.DataItem;
import de.iconten.client.rest.utils.Util;

public class DataProviderGermany extends AbsDataProvider {
	public static final String COUNTRY = "germany";
	public static final String FEATURE_LANDKREIS_NAME = "landkreis";
	public static final String FEATURE_BUNDESLAND_NAME = "bundesland";

	protected final RestClientGermany client = new RestClientGermany();

	/**
	 * 
	 * @throws Exception
	 */
	public void writeLandkreisData() throws Exception {
		final List<DataItem> features = client.getLandkreisData();
		final List<DataItem> dayBeforFeatures = getDayBeforFeatures(calendarToday, COUNTRY, FEATURE_LANDKREIS_NAME);

		writeJsonData(FEATURE_LANDKREIS_NAME, features, dayBeforFeatures, getJsonFile(COUNTRY, FEATURE_LANDKREIS_NAME, calendarToday.getTime()));
		writeLandkreisCsvData(FEATURE_LANDKREIS_NAME, features);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void writeBundeslandData() throws Exception {
		final List<DataItem> features = client.getBundeslandData();
		final List<DataItem> dayBeforFeatures = getDayBeforFeatures(calendarToday, COUNTRY, FEATURE_BUNDESLAND_NAME);

		writeJsonData(FEATURE_BUNDESLAND_NAME, features, dayBeforFeatures, getJsonFile(COUNTRY, FEATURE_BUNDESLAND_NAME, calendarToday.getTime()));
		writeBundeslandCsvData(FEATURE_BUNDESLAND_NAME, features);
	}

	public static void writeLandkreisCsvData(final String featureType, List<DataItem> features) {

		final List<String[]> data = new ArrayList<>();
		data.add(new String[] { "ObjectId", "Landkreis", "Bundesland", "Infektionen", "Todesfälle", "Letalitätsrate", "Aktualisiert" });

		for (final DataItem feature : features) {
			data.add(new String[] {
					// @formatter:off
					
					String.valueOf(feature.getObjectId()), 
					feature.getName(), 
					feature.getState(), 
					String.valueOf(feature.getCases()),  
					String.valueOf(feature.getDeaths()),
					String.valueOf(feature.getDeathRate()),
					Util.formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(getCsvFile(COUNTRY, featureType).getAbsolutePath(), data);
	}

	public static void writeBundeslandCsvData(final String featureType, List<DataItem> features) {

		final List<String[]> data = new ArrayList<>();
		data.add(new String[] { "ObjectId", "Bundesland", "Infektionen", "Todesfälle", "Letalitätsrate", "Aktualisiert" });

		for (final DataItem feature : features) {
			data.add(new String[] {
					// @formatter:off
					
					String.valueOf(feature.getObjectId1()), 
					feature.getName(), 
					String.valueOf(feature.getCases()),  
					String.valueOf(feature.getDeaths()),
					String.valueOf(feature.getDeathRate()),
					Util.formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(getCsvFile(COUNTRY, featureType).getAbsolutePath(), data);
	}

}
