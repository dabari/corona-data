package de.iconten.client.rest.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DataItem {

	@SerializedName(value = "objectId")
	private long objectId;

	@SerializedName(value = "objectId1")
	private long objectId1;

	@SerializedName(value = "ags")
	private String ags;

	/**
	 * Name
	 */
	@SerializedName(value = "name")
	private String name;

	/**
	 * Typ [Land, Landkreis] usw.
	 */
	@SerializedName(value = "type")
	private String type;

	/**
	 * Bundesland
	 */
	@SerializedName(value = "state")
	private String state;

	/**
	 * Einwohnerzahl
	 */
	@SerializedName(value = "population")
	private int population;

	/**
	 * Infektionen
	 */
	@SerializedName(value = "cases")
	private int cases;

	/**
	 * Todesfälle
	 */
	@SerializedName(value = "deaths")
	private int deaths;

	@SerializedName(value = "deathRate")
	private double deathRate;

	/**
	 * Fälle der letzten tage
	 */
	@SerializedName(value = "lastDayCases")
	private List<Integer> lastDayCases;

	/**
	 * Aktualisiert
	 */
	@SerializedName(value = "lastUpdate")
	private Date lastUpdate;

	public DataItem() {
		lastDayCases = new LinkedList<>();
	}
}
