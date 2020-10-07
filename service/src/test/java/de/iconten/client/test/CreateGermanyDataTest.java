package de.iconten.client.test;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.reflect.TypeToken;

import de.iconten.client.data.germany.DataProvider;
import de.iconten.client.rest.RestClient;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;

public class CreateGermanyDataTest {

	@Test
	public void writeLandkreisAllDataTest() throws Exception {
		final DataProvider dataProvider = new DataProvider();
		dataProvider.writeLandkreisData();
	}

	@Test
	public void writeLandkreisCsvTest() throws Exception {
		final RestClient client = new RestClient();
		final List<FeatureLandkreis> features = client.getLandkreisData();
		DataProvider.writeLandkreisCsvData(features);
	}

	@Test
	public void writeLandkreisJsonTest() throws Exception {
		final RestClient client = new RestClient();
		final List<FeatureLandkreis> features = client.getLandkreisData();

		final List<FeatureLandkreis> dayBeforFeatures = DataProvider.getDayBeforFeatures(DataProvider.FEATURE_LANDKREIS_NAME,
				new TypeToken<List<FeatureLandkreis>>() {
				}.getType());

		DataProvider.writeLandkreisJsonData(features, dayBeforFeatures);
	}

	@Test
	public void writeBundeslandAllDataTest() throws Exception {
		final DataProvider dataProvider = new DataProvider();
		dataProvider.writeBundeslandData();
	}

	@Test
	public void writeBundeslandCsvTest() throws Exception {
		final RestClient client = new RestClient();
		final List<FeatureBundesland> features = client.getBundeslandData();
		DataProvider.writeBundeslandCsvData(features);
	}

	@Test
	public void writeBundeslandJsonTest() throws Exception {
		final RestClient client = new RestClient();
		final List<FeatureBundesland> features = client.getBundeslandData();
		final List<FeatureBundesland> dayBeforFeatures = DataProvider.getDayBeforFeatures(DataProvider.FEATURE_BUNDESLAND_NAME,
				new TypeToken<List<FeatureBundesland>>() {
				}.getType());
		DataProvider.writeBundeslandJsonData(features, dayBeforFeatures);
	}

}
