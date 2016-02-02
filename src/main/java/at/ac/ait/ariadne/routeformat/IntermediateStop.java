package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.IntermediateStop.Builder;
import at.ac.ait.ariadne.routeformat.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * An {@link IntermediateStop} represents a stop on a {@link RouteSegment} of interest to the user, e.g. a public
 * transport stop where the public transport line the user is traveling with stops or a point of interest along a cycle
 * route.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class IntermediateStop {
	private final Location stop;
	private final Optional<ZonedDateTime> plannedArrivalTime;
	private final Optional<ZonedDateTime> plannedDepartureTime;
	private final Optional<ZonedDateTime> estimatedArrivalTime;
	private final Optional<ZonedDateTime> estimatedDepartureTime;

	public Location getStop() {
		return stop;
	}

	/**
	 * @return static time according to a time table
	 */
	public Optional<String> getPlannedArrivalTime() {
		return plannedArrivalTime.map(time -> time.toString());
	}

	/**
	 * @return static time according to a time table
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getPlannedArrivalTimeAsZonedDateTime() {
		return plannedArrivalTime;
	}

	/**
	 * @return static time according to a time table
	 */
	public Optional<String> getPlannedDepartureTime() {
		return plannedDepartureTime.map(time -> time.toString());
	}

	/**
	 * @return static time according to a time table
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getPlannedDepartureTimeAsZonedDateTime() {
		return plannedDepartureTime;
	}

	/**
	 * @return time estimated via real-time data
	 */
	public Optional<String> getEstimatedArrivalTime() {
		return estimatedArrivalTime.map(time -> time.toString());
	}

	/**
	 * @return time estimated via real-time data
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getEstimatedArrivalTimeAsZonedDateTime() {
		return estimatedArrivalTime;
	}

	/**
	 * @return time estimated via real-time data
	 */
	public Optional<String> getEstimatedDepartureTime() {
		return estimatedDepartureTime.map(time -> time.toString());
	}

	/**
	 * @return time estimated via real-time data
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getEstimatedDepartureTimeAsZonedDateTime() {
		return estimatedDepartureTime;
	}

	public IntermediateStop(Builder builder) {
		this.stop = builder.stop;
		this.plannedArrivalTime = builder.plannedArrivalTime;
		this.plannedDepartureTime = builder.plannedDepartureTime;
		this.estimatedArrivalTime = builder.estimatedArrivalTime;
		this.estimatedDepartureTime = builder.estimatedDepartureTime;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Location stop;
		private Optional<ZonedDateTime> plannedArrivalTime = Optional.empty();
		private Optional<ZonedDateTime> plannedDepartureTime = Optional.empty();
		private Optional<ZonedDateTime> estimatedArrivalTime = Optional.empty();
		private Optional<ZonedDateTime> estimatedDepartureTime = Optional.empty();

		public Builder withStop(Location stop) {
			this.stop = stop;
			return this;
		}

		@JsonIgnore
		public Builder withPlannedArrivalTime(ZonedDateTime plannedArrivalTime) {
			this.plannedArrivalTime = Optional.ofNullable(plannedArrivalTime);
			return this;
		}

		@JsonProperty
		public Builder withPlannedArrivalTime(String plannedArrivalTime) {
			this.plannedArrivalTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(plannedArrivalTime,
					"plannedArrivalTime"));
			return this;
		}

		@JsonIgnore
		public Builder withPlannedDepartureTime(ZonedDateTime plannedDepartureTime) {
			this.plannedDepartureTime = Optional.ofNullable(plannedDepartureTime);
			return this;
		}

		@JsonProperty
		public Builder withPlannedDepartureTime(String plannedDepartureTime) {
			this.plannedDepartureTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(plannedDepartureTime,
					"plannedDepartureTime"));
			return this;
		}

		@JsonIgnore
		public Builder withEstimatedArrivalTime(ZonedDateTime estimatedArrivalTime) {
			this.estimatedArrivalTime = Optional.ofNullable(estimatedArrivalTime);
			return this;
		}

		@JsonProperty
		public Builder withEstimatedArrivalTime(String estimatedArrivalTime) {
			this.estimatedArrivalTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(estimatedArrivalTime,
					"estimatedArrivalTime"));
			return this;
		}

		@JsonIgnore
		public Builder withEstimatedDepartureTime(ZonedDateTime estimatedDepartureTime) {
			this.estimatedDepartureTime = Optional.ofNullable(estimatedDepartureTime);
			return this;
		}

		@JsonProperty
		public Builder withEstimatedDepartureTime(String estimatedDepartureTime) {
			this.estimatedDepartureTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(estimatedDepartureTime,
					"estimatedDepartureTime"));
			return this;
		}

		public IntermediateStop build() {
			validate();
			return new IntermediateStop(this);
		}

		private void validate() {
			Preconditions.checkArgument(stop != null, "stop is mandatory but missing");
		}
	}
}
