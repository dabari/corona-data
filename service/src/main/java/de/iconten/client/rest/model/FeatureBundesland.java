package de.iconten.client.rest.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FeatureBundesland {
	/*
	"OBJECTID_1" : 1, 
	"LAN_ew_AGS" : "01", 
	"LAN_ew_GEN" : "Schleswig-Holstein", 
	"LAN_ew_BEZ" : "Land", 
	"LAN_ew_EWZ" : 2896712, 
	"OBJECTID" : 15, 
	"Fallzahl" : 4914, 
	"Aktualisierung" : 1601762400000, 
	"faelle_100000_EW" : 169.640613219402, 
	"Death" : 162
	*/

	@SerializedName(value = "OBJECTID")
	private long objectId;

	@SerializedName(value = "OBJECTID_1")
	private long objectId1;

	@SerializedName(value = "LAN_ew_AGS")
	private String ags;

	@SerializedName(value = "LAN_ew_GEN")
	private String gen;

	@SerializedName(value = "LAN_ew_BEZ")
	private String bez;

	/**
	 * Einwohnerzahl
	 */
	@SerializedName(value = "LAN_ew_EWZ")
	private int ewz;

	/**
	 * Infektionen
	 */
	@SerializedName(value = "Fallzahl")
	private int cases;

	/**
	 * Todesfälle
	 */
	@SerializedName(value = "Death")
	private int deaths;

	/**
	 * Fälle/100.000 EW
	 */
	@SerializedName(value = "faelle_100000_EW")
	private double casesPer100k;

	/**
	 * Fälle der letzten 7 Tage
	 */
	@SerializedName(value = "cases7day")
	private List<Integer> cases7day;

	/**
	 * Aktualisiert
	 */
	@SerializedName(value = "Aktualisierung")
	private Date lastUpdate;

	public FeatureBundesland() {
		cases7day = new LinkedList<>();
	}
}
