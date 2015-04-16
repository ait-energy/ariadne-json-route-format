package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.RouteFormatRoot.Builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
public class RouteFormatRoot {

	public enum Status {
		OK, ERROR;
	}

	private long id;
	private ZonedDateTime calculationTime;
	private Status status;
	private Optional<String> debugMessage;
	private String coordinateReferenceSystem;
	
	private Optional<RoutingRequest> request;
	private List<Route> routes;

	@JsonProperty(required = true)
	public long getId() {
		return id;
	}
	
	@JsonProperty(required = true)
	public String getCalculationTime() {
		return calculationTime.toString();
	}

	@JsonProperty(required = true)
	public Status getStatus() {
		return status;
	}

	/** debug message explaining errors */
	public Optional<String> getDebugMessage() {
		return debugMessage;
	}

	/** in the form of EPSG:*, e.g. EPSG:4326 */
	@JsonProperty(required = true)
	public String getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}
	
	/**
	 * @return The original request used to calculate the route(s). It is
	 *         guaranteed that if at least one route is returned there is also a
	 *         request here. The request will only be omitted if the request
	 *         itself could not be created due to invalid request parameters.
	 */
	public Optional<RoutingRequest> getRequest() {
		return request;
	}
	
	@JsonProperty(required = true)
	public List<Route> getRoutes() {
		return routes;
	}

	private RouteFormatRoot(Builder builder) {
		this.id = builder.id;
		this.calculationTime = builder.calculationTime;
		this.status = builder.status;
		this.debugMessage = builder.debugMessage;
		this.coordinateReferenceSystem = builder.coordinateReferenceSystem;
		this.request = builder.request;
		this.routes = builder.routes;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private Long id;
		private ZonedDateTime calculationTime;
		private Status status;
		private Optional<String> debugMessage = Optional.empty();
		private String coordinateReferenceSystem;
		private Optional<RoutingRequest> request;
		private List<Route> routes;
		
		public Builder withId(long id) {
			this.id = id;
			return this;
		}
		
		public Builder withCalculationTimeNow() {
			this.calculationTime = ZonedDateTime.now();
			return this;
		}

		@JsonIgnore
		public Builder withCalculationTime(ZonedDateTime calculationTime) {
			this.calculationTime = calculationTime;
			return this;
		}

		@JsonProperty
		public Builder withCalculationTime(String calculationTime) {
			this.calculationTime = SprouteUtils.parseZonedDateTime(calculationTime, "calculationTime");
			return this;
		}

		public Builder withStatus(Status status) {
			this.status = status;
			return this;
		}

		public Builder withDebugMessage(String debugMessage) {
			this.debugMessage = Optional.ofNullable(debugMessage);
			return this;
		}

		public Builder withCoordinateReferenceSystem(String coordinateReferenceSystem) {
			this.coordinateReferenceSystem = coordinateReferenceSystem;
			return this;
		}
		
		public Builder withRequest(RoutingRequest request) {
			this.request = Optional.ofNullable(request);
			return this;
		}
		
		public Builder withRoutes(List<Route> routes) {
			this.routes = new ArrayList<Route>(routes);
			return this;
		}

		public RouteFormatRoot build() {
			validate();
			return new RouteFormatRoot(this);
		}

		private void validate() {
			Preconditions.checkArgument(id != null, "id is mandatory but missing");
			Preconditions.checkArgument(calculationTime != null, "calculationTime is mandatory but missing");
			Preconditions.checkArgument(status != null, "status is mandatory but missing");
			Preconditions.checkArgument(coordinateReferenceSystem != null,
					"coordinateReferenceSystem is mandatory but missing");
			Preconditions.checkArgument(coordinateReferenceSystem.startsWith("EPSG:"),
					"coordinateReferenceSystem must start with EPSG:");
			
			if(!request.isPresent() && routes.size() > 0) {
				throw new IllegalArgumentException("request is mandatory but missing (if at least one route is present)");
			}
		}
	}

}
