package at.ac.ait.sproute.routeformat;

import java.util.List;

import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mstraub
 */
public class Location {
	
	public static Location newCoordinateLocation(GeoJSONFeature<GeoJSONPoint> coordinate) {
		Location location = new Location();
		location.type = LocationType.COORDINATE;
		location.coordinate = coordinate;
		return location;
	}
	
	public static Location newAddressLocation(GeoJSONFeature<GeoJSONPoint> coordinate, String country, String city,
			String postcode, String streetname, String housenumber) {
		Location address = new Location();
		address.type = LocationType.ADDRESS;
		address.coordinate = coordinate;
		address.country = country;
		address.city = city;
		address.postcode = postcode;
		address.streetname = streetname;
		address.housenumber = housenumber;
		return address;
	}
	
	// TODO more static constructor methods
	
	public enum LocationType {
		/** simple coordinate without additional information */
		COORDINATE,
		/** house number */
		ADDRESS,
		/** point of interest (probably with house number) */
		POI, PUBLIC_TRANSPORT_STOP // TODO more? e.g. Bike-Sharing-Station,...
	};

	@JsonProperty(required = true)
	public GeoJSONFeature<GeoJSONPoint> coordinate;

	@JsonProperty(required = true)
	/** defines which fields are set */
	public LocationType type;

	// fields for: ADDRESS & POI & PUBLIC_TRANSPORT_STOP
	public String country;
	public String city;
	public String postcode;
	public String streetname;
	public String housenumber;
	
	
	// fields for: POI & PUBLIC_TRANSPORT_STOP
	public String name;
	
	// fields for: POI
	public String poiType;

	// fields for: PUBLIC_TRANSPORT_STOP
	/** lines the user can change to at this stop */
	public List<String> relatedLines;
	
	// TODO barrier free information
	public boolean wheelChairAccessible;
}
