package de.iconten.client.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.iconten.client.rest.RestClient;
import de.iconten.client.rest.model.FeatureBundesland;
import de.iconten.client.rest.model.FeatureLandkreis;

public class ClientTest {

	private final RestClient client = new RestClient();

	@Test
	public void getLandkreisDatenTest() {
		final List<FeatureLandkreis> data = client.getLandkreisData(new String[] { "234", "235" });
		assertEquals(2, data.size());
		assertEquals("Fürstenfeldbruck", data.get(0).getGen());
		assertEquals("Garmisch-Partenkirchen", data.get(1).getGen());
	}

	@Test
	public void getBundeslandDatenTest() {
		final List<FeatureBundesland> data = client.getBundeslandData(new String[] { "1", "2" });
		assertEquals(2, data.size());
		assertEquals("Schleswig-Holstein", data.get(0).getGen());
		assertEquals("Hamburg", data.get(1).getGen());
	}

}
