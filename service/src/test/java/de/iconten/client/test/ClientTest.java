package de.iconten.client.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.iconten.client.rest.RestClientGermany;
import de.iconten.client.rest.model.DataItem;

public class ClientTest {

	private final RestClientGermany client = new RestClientGermany();

	@Test
	public void getLandkreisDatenTest() {
		final List<DataItem> data = client.getLandkreisData(new String[] { "234", "235" });
		assertEquals(2, data.size());
		assertEquals("Fürstenfeldbruck", data.get(0).getName());
		assertEquals("Garmisch-Partenkirchen", data.get(1).getName());
	}

	@Test
	public void getBundeslandDatenTest() {
		final List<DataItem> data = client.getBundeslandData(new String[] { "1", "2" });
		assertEquals(2, data.size());
		assertEquals("Schleswig-Holstein", data.get(0).getName());
		assertEquals("Hamburg", data.get(1).getName());
	}

}
