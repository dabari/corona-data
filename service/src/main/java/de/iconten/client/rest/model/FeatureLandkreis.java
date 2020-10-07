package de.iconten.client.rest.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FeatureLandkreis {

	/*
	"OBJECTID": 234,
	"RS": "09179",
	"AGS": "09179",
	"GEN": "Fürstenfeldbruck",
	"BEZ": "Landkreis",
	"EWZ": 219320,
	"AGS_0": "09179000",
	"BL": "Bayern",
	"county": "LK Fürstenfeldbruck",
	"death_rate": 2.65291083271923,
	"cases": 1357,
	"deaths": 36,
	"cases_per_100k": 618.730621922305,
	"cases_per_population": 0.618730621922305
	*/

	@SerializedName(value = "OBJECTID")
	private long objectId;

	@SerializedName(value = "RS")
	private String rs;

	@SerializedName(value = "AGS")
	private String ags;

	@SerializedName(value = "GEN")
	private String gen;

	@SerializedName(value = "BEZ")
	private String bez;

	@SerializedName(value = "EWZ")
	private int ewz;

	@SerializedName(value = "AGS_0")
	private String ags0;

	@SerializedName(value = "BL")
	private String bl;

	@SerializedName(value = "country")
	private String country;

	@SerializedName(value = "death_rate")
	private double deathRate;

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

	@SerializedName(value = "cases_per_100k")
	private double casesPer100k;

	/**
	 * Fälle der letzten 7 Tage
	 */
	@SerializedName(value = "cases7day")
	private List<Integer> cases7day;

	@SerializedName(value = "cases_per_population")
	private double casesPerPopulation;

	/**
	 * Aktualisiert
	 */
	@SerializedName(value = "last_update")
	private Date lastUpdate;

	public FeatureLandkreis() {
		cases7day = new LinkedList<>();
	}

}
