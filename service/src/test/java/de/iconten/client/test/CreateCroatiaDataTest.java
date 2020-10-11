package de.iconten.client.test;

import org.junit.jupiter.api.Test;

import de.iconten.client.data.DataProviderCroatia;

public class CreateCroatiaDataTest {

	@Test
	public void writeLandkreisAllDataTest() throws Exception {
		final DataProviderCroatia dataProvider = new DataProviderCroatia();
		dataProvider.writeStatesData();
	}

}
