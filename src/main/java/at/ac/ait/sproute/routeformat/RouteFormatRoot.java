package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.RouteFormatRoot.Builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RouteFormatRoot {

	public enum Status {
		/**
		 * Everything OK, route(s) are available.
		 */
		OK,
		/**
		 * Problems occurred when routing request parameters were parsed -
		 * neither request nor routes are available.
		 */
		INVALID_REQUEST,
		/**
		 * Error while routing (or general error). No routes are available.
		 */
		ERROR;
	}

	private String requestId;
	private ZonedDateTime processedTime;
	private Status status;
	private Optional<String> debugMessage;
	private String coordinateReferenceSystem;
	
	private Optional<RoutingRequest> request;
	private List<Route> routes;

	@JsonProperty(required = true)
	public String getRequestId() {
		return requestId;
	}
	
	/**
	 * Time when reuqest / calculations were finished or deemed not possible in
	 * case of an error.
	 */
	@JsonProperty(required = true)
	public String getProcessedTime() {
		return processedTime.toString();
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
		this.requestId = builder.requestId;
		this.processedTime = builder.processedTime;
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
		private String requestId;
		private ZonedDateTime processedTime;
		private Status status;
		private Optional<String> debugMessage = Optional.empty();
		private String coordinateReferenceSystem;
		private Optional<RoutingRequest> request = Optional.empty();
		private List<Route> routes = new ArrayList<>();
		
		public Builder withRequestId(String requestId) {
			this.requestId = requestId;
			return this;
		}
		
		public Builder withProcessedTimeNow() {
			this.processedTime = ZonedDateTime.now();
			return this;
		}

		@JsonIgnore
		public Builder withProcessedTime(ZonedDateTime processedTime) {
			this.processedTime = processedTime;
			return this;
		}

		@JsonProperty
		public Builder withProcessedTime(String processedTime) {
			this.processedTime = SprouteUtils.parseZonedDateTime(processedTime, "processedTime");
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
		
		public Builder withDefaultCoordinateReferenceSystem() {
			this.coordinateReferenceSystem = "EPSG:4326";
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
			Preconditions.checkArgument(requestId != null, "id is mandatory but missing");
			Preconditions.checkArgument(processedTime != null, "processedTime is mandatory but missing");
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
