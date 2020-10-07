package de.iconten.client.test;

import org.junit.jupiter.api.Test;

import de.iconten.client.data.croatia.DataProvider;

public class CreateCroatiaDataTest {

	@Test
	public void writeLandkreisAllDataTest() throws Exception {
		final DataProvider dataProvider = new DataProvider();
		dataProvider.writeLandkreisData();
	}

}
