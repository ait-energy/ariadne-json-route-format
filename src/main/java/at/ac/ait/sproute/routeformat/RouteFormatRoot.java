package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mstraub
 *
 */
public class RouteFormatRoot {

	public enum Status {
		OK, ERROR
	}

	@JsonProperty(required = true)
	public RoutingRequest request;

	private ZonedDateTime calculationTime;

	public String getCalculationTime() {
		return calculationTime == null ? null : calculationTime.toString();
	}

	public void setCalculationTime(String calculationTime) {
		this.calculationTime = ZonedDateTime.parse(calculationTime);
	}
	
	public void setCalculationTimeNow() {
		this.calculationTime = ZonedDateTime.now();
	}

	@JsonProperty(required = true)
	public Status status;

	/** optional debug message explaining the error */
	public String debugMessage;
	
	/** in the form of EPSG:*, e.g. EPSG:4326 */
	@JsonProperty(required = true)
	public String coordinateReferenceSystem;

}
