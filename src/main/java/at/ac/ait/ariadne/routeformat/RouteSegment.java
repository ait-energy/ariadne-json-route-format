package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A RouteSegment must contain at least one of the two possible geometry types.
 * 
 * @author mstraub
 */
public class RouteSegment {
	
	public RouteSegment() {
	}
	
	public RouteSegment(int nr, Location from, Location to,
			String departureTime, String arrivalTime,
			int lengthMeters, int durationSeconds, ModeOfTransport modeOfTransport) {
		super();
		this.nr = nr;
		this.from = from;
		this.to = to;
		setDepartureTime(departureTime);
		setArrivalTime(arrivalTime);
		this.lengthMeters = lengthMeters;
		this.durationSeconds = durationSeconds;
		this.modeOfTransport = modeOfTransport;
	}

	public enum ModeOfTransport {FOOT, BICYCLE, MOTORCYCLE, CAR, PUBLIC_TRANSPORT};
	
	/** number of the segment in the route (starts with 1) */
	@JsonProperty(required = true)
	public int nr;
	
	@JsonProperty(required = true) // TODO immer From & To speichern? ist in der Mitte immer doppelt.
	public Location from;
	@JsonProperty(required = true)
	public Location to;
	/** e.g. public transport stops on the way */
	public List<Location> intermediateStops; // TODO also store when these stations are reached?
	
	private ZonedDateTime departureTime;
	private ZonedDateTime arrivalTime;
	
	@JsonProperty(required = true)
	public int lengthMeters;
	@JsonProperty(required = true)
	public int durationSeconds;
	// other attributes can be added here (on demand)
	
	@JsonProperty(required = true)
	public ModeOfTransport modeOfTransport;
	
	public Vehicle vehicle;
	public Operator operator;
	
	// TODO price, type of ticket?
	
	
	/**
	 * segment geometry in "Encoded Polyline Algorithm Format":
	 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
	 */
	public String geometryEncodedPolyLine;
	
	/** segment geometry as a single LineString-Feature */
	public GeoJSONFeature<GeoJSONLineString> geometryGeoJson;

	/**
	 * segment geometry as a collection of LineStrings (one for each edge in
	 * the routing graph) with debugging information for each edge
	 */
	public GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges;

	public List<NavigationInstruction> navigationInstructions = new ArrayList<>();
	
	
	@JsonProperty(required = true)
	public String getDepartureTime() {
		return departureTime == null ? null : departureTime.toString();
	}

	@JsonProperty(required = true)
	public void setDepartureTime(String departureTime) {
		this.departureTime = ZonedDateTime.parse(departureTime);
	}

	@JsonProperty(required = true)
	public String getArrivalTime() {
		return arrivalTime == null ? null : arrivalTime.toString();
	}

	@JsonProperty(required = true)
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = ZonedDateTime.parse(arrivalTime);
	}
	
}
