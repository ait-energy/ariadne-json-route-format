package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Route {
	
	public Route() {
	}
	
	public Route(Location from, Location to, String departureTime,
			String arrivalTime, int lengthMeters, int durationSeconds) {
		this.from = from;
		this.to = to;
		setDepartureTime(departureTime);
		setArrivalTime(arrivalTime);
		this.lengthMeters = lengthMeters;
		this.durationSeconds = durationSeconds;
	}

	@JsonProperty(required = true)
	public Location from;
	@JsonProperty(required = true)
	public Location to;

	private ZonedDateTime departureTime;
	private ZonedDateTime arrivalTime;

	@JsonProperty(required = true)
	public int lengthMeters;
	@JsonProperty(required = true)
	public int durationSeconds;
	// other attributes can be added here (on demand)

	@JsonProperty(required = true)
	// minItems=1
	public List<RouteSegment> segments = new ArrayList<>();

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
