package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.location.PublicTransportStop;

import com.google.common.base.Preconditions;

public class IntermediateStop {
	private final PublicTransportStop stop;
	private final Optional<ZonedDateTime> plannedArrivalTime;
	private final Optional<ZonedDateTime> plannedDepartureTime;
	private final Optional<ZonedDateTime> estimatedArrivalTime;
	private final Optional<ZonedDateTime> estimatedDepartureTime;

	public PublicTransportStop getStop() {
		return stop;
	}

	/**
	 * @return static time according to a time table
	 */
	public Optional<ZonedDateTime> getPlannedArrivalTime() {
		return plannedArrivalTime;
	}

	/**
	 * @return static time according to a time table
	 */
	public Optional<ZonedDateTime> getPlannedDepartureTime() {
		return plannedDepartureTime;
	}

	/**
	 * @return time estimated via real-time data
	 */
	public Optional<ZonedDateTime> getEstimatedArrivalTime() {
		return estimatedArrivalTime;
	}

	/**
	 * @return time estimated via real-time data
	 */
	public Optional<ZonedDateTime> getEstimatedDepartureTime() {
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
		private PublicTransportStop stop;
		private Optional<ZonedDateTime> plannedArrivalTime = Optional.empty();
		private Optional<ZonedDateTime> plannedDepartureTime = Optional.empty();
		private Optional<ZonedDateTime> estimatedArrivalTime = Optional.empty();
		private Optional<ZonedDateTime> estimatedDepartureTime = Optional.empty();

		public Builder withStop(PublicTransportStop stop) {
			this.stop = stop;
			return this;
		}

		public Builder withPlannedArrivalTime(ZonedDateTime plannedArrivalTime) {
			this.plannedArrivalTime = Optional.ofNullable(plannedArrivalTime);
			return this;
		}

		public Builder withPlannedDepartureTime(ZonedDateTime plannedDepartureTime) {
			this.plannedDepartureTime = Optional.ofNullable(plannedDepartureTime);
			return this;
		}

		public Builder withEstimatedArrivalTime(ZonedDateTime estimatedArrivalTime) {
			this.estimatedArrivalTime = Optional.ofNullable(estimatedArrivalTime);
			return this;
		}

		public Builder withEstimatedDepartureTime(ZonedDateTime estimatedDepartureTime) {
			this.estimatedDepartureTime = Optional.ofNullable(estimatedDepartureTime);
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
