package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
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

	private RoutingRequest request;
	private ZonedDateTime calculationTime;
	private Status status;
	private Optional<String> debugMessage;
	private String coordinateReferenceSystem;

	@JsonProperty(required = true)
	public RoutingRequest getRequest() {
		return request;
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

	private RouteFormatRoot(Builder builder) {
		this.request = builder.request;
		this.calculationTime = builder.calculationTime;
		this.status = builder.status;
		this.debugMessage = builder.debugMessage;
		this.coordinateReferenceSystem = builder.coordinateReferenceSystem;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private RoutingRequest request;
		private ZonedDateTime calculationTime;
		private Status status;
		private Optional<String> debugMessage = Optional.empty();
		private String coordinateReferenceSystem;

		public Builder withRequest(RoutingRequest request) {
			this.request = request;
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
			this.debugMessage = Optional.of(debugMessage);
			return this;
		}

		public Builder withCoordinateReferenceSystem(String coordinateReferenceSystem) {
			this.coordinateReferenceSystem = coordinateReferenceSystem;
			return this;
		}

		public RouteFormatRoot build() {
			validate();
			return new RouteFormatRoot(this);
		}

		private void validate() {
			Preconditions.checkNotNull(request, "request is mandatory but missing");
			Preconditions.checkNotNull(calculationTime, "calculationTime is mandatory but missing");
			Preconditions.checkNotNull(status, "status is mandatory but missing");
			Preconditions.checkNotNull(coordinateReferenceSystem,
					"coordinateReferenceSystem is mandatory but missing");

			Preconditions.checkArgument(coordinateReferenceSystem.startsWith("EPSG:"),
					"coordinateReferenceSystem must start with EPSG:");
		}
	}

}
