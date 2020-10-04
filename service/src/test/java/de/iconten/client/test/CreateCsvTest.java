package de.iconten.client.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.iconten.client.rest.RestClient;
import de.iconten.client.rest.csv.CsvProvider;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;

public class CreateCsvTest {

	private final RestClient client = new RestClient();

	private static final File USERDIR = new File(System.getProperty("user.dir"));

	@Test
	public void writeLandkreisCsvTest() {
		final List<FeatureLandkreis> features = client.getLandkreisData();

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
					formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(new File(USERDIR.getParentFile(), "data/landkreis.csv").getAbsolutePath(), data);
	}

	@Test
	public void writeBundeslandCsvTest() {
		final List<FeatureBundesland> features = client.getBundeslandData();

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
					formatDate(feature.getLastUpdate())
					
					// @formatter:on
			});
		}
		CsvProvider.writeData(new File(USERDIR.getParentFile(), "data/bundesland.csv").getAbsolutePath(), data);
	}

	private String formatDate(Date date) {
		final String pattern = "yyyy-MM-dd";
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

}
