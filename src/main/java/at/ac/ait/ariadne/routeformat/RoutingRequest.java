package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoutingRequest {
	@JsonProperty(required = true)
	public String id;

	@JsonProperty(required = true)
	public Location from;
	@JsonProperty(required = true)
	public Location to;

	private ZonedDateTime departureTime;
	private ZonedDateTime arrivalTime;

	@JsonProperty(required = true)
	public List<Route> routes = new ArrayList<>();

	public String getDepartureTime() {
		return departureTime == null ? null : departureTime.toString();
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = (departureTime == null) ? null : ZonedDateTime.parse(departureTime);
	}

	public String getArrivalTime() {
		return arrivalTime == null ? null : arrivalTime.toString();
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = (arrivalTime == null) ? null : ZonedDateTime.parse(arrivalTime);
	}

}
