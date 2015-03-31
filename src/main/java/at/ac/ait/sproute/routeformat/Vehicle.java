package at.ac.ait.sproute.routeformat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mstraub
 */
public class Vehicle {
	
	public enum VehicleType { 
		// "classic" public transport
		TRAIN, LIGHTRAIL, SUBWAY, MONORAIL, TRAM, BUS, TROLLEYBUS, AERIALWAY, FERRY,
		// individual transport
		PRIVATE_BICYCLE, SHARED_BICYCLE, PRIVATE_MOTORCYCLE, SHARED_MOTORCYCLE, PRIVATE_CAR, SHARED_CAR, TAXI, CALL_TAXI
	}
	
	@JsonProperty(required = true)
	public VehicleType type;
	
	public boolean electric;
	
	// Regionalbus vs. Lokaler Bus?
	// ICE vs. S-Bahn, REX,..
	// ÖBB vs. Westbahn, Wiener Linien, Dr. Richard
	
	public String id; // =numberplate, bike-sharing-bicycle-nr,..
	// waggonnr (sitzplatz?)
	
	

}
