package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.RouteFormatRoot.Builder;
import at.ac.ait.ariadne.routeformat.Sproute.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RouteFormatRoot {

	private final String routeFormatVersion;
	private final String requestId;
	private final ZonedDateTime processedTime;
	private final Status status;
	private final Optional<String> debugMessage;
	private final String coordinateReferenceSystem;
	private final Optional<RoutingRequest> request;
	private final List<Route> routes;
	private final Map<String, Object> additionalInfo;

	@JsonProperty(required = true)
	public String getRouteFormatVersion() {
		return routeFormatVersion;
	}

	@JsonProperty(required = true)
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Time when request / calculations were finished or deemed not possible in case of an error.
	 */
	@JsonProperty(required = true)
	public String getProcessedTime() {
		return processedTime.toString();
	}
	
	@JsonIgnore
	public ZonedDateTime getProcessedTimeAsZonedDateTime() {
		return processedTime;
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
	 * @return The original request used to calculate the route(s). It is guaranteed that if at least one route is
	 *         returned there is also a request here. The request will only be omitted if the request itself could not
	 *         be created due to invalid request parameters.
	 */
	public Optional<RoutingRequest> getRequest() {
		return request;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	@JsonProperty(required = true)
	public List<Route> getRoutes() {
		return routes;
	}

	private RouteFormatRoot(Builder builder) {
		this.routeFormatVersion = builder.routeFormatVersion;
		this.requestId = builder.requestId;
		this.processedTime = builder.processedTime;
		this.status = builder.status;
		this.debugMessage = builder.debugMessage;
		this.coordinateReferenceSystem = builder.coordinateReferenceSystem;
		this.request = builder.request;
		this.routes = builder.routes;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String routeFormatVersion;
		private String requestId;
		private ZonedDateTime processedTime;
		private Status status;
		private Optional<String> debugMessage = Optional.empty();
		private String coordinateReferenceSystem;
		private Optional<RoutingRequest> request = Optional.empty();
		private List<Route> routes = Collections.emptyList();
		private Map<String, Object> additionalInfo = Collections.emptyMap();

		public Builder withRouteFormatVersion(String routeFormatVersion) {
			this.routeFormatVersion = routeFormatVersion;
			return this;
		}

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
			this.routes = ImmutableList.copyOf(routes);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public RouteFormatRoot build() {
			validate();
			return new RouteFormatRoot(this);
		}

		private void validate() {
			Preconditions.checkArgument(routeFormatVersion != null, "routeFormatVersion is mandatory but missing");
			Preconditions.checkArgument(requestId != null, "id is mandatory but missing");
			Preconditions.checkArgument(processedTime != null, "processedTime is mandatory but missing");
			Preconditions.checkArgument(status != null, "status is mandatory but missing");
			Preconditions.checkArgument(coordinateReferenceSystem != null,
					"coordinateReferenceSystem is mandatory but missing");
			Preconditions.checkArgument(coordinateReferenceSystem.startsWith("EPSG:"),
					"coordinateReferenceSystem must start with EPSG:");

			if (!request.isPresent() && routes.size() > 0) {
				throw new IllegalArgumentException(
						"request is mandatory but missing (if at least one route is present)");
			}
		}
	}

}