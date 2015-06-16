package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.Route.Builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Route {

	private Location from;
	private Location to;
	private Optional<ZonedDateTime> departureTime;
	private Optional<ZonedDateTime> arrivalTime;
	private int lengthMeters;
	private int durationSeconds;
	private List<RouteSegment> segments;

	@JsonProperty(required = true)
	public Location getFrom() {
		return from;
	}

	@JsonProperty(required = true)
	public Location getTo() {
		return to;
	}

	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	@JsonProperty(required = true)
	public int getLengthMeters() {
		return lengthMeters;
	}

	@JsonProperty(required = true)
	public int getDurationSeconds() {
		return durationSeconds;
	}

	@JsonProperty(required = true)
	public List<RouteSegment> getSegments() {
		return segments;
	}

	private Route(Builder builder) {
		this.from = builder.from;
		this.to = builder.to;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.lengthMeters = builder.lengthMeters;
		this.durationSeconds = builder.durationSeconds;
		this.segments = builder.segments;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Location from;
		private Location to;
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private Integer lengthMeters;
		private Integer durationSeconds;
		private List<RouteSegment> segments = new ArrayList<>();

		public Builder withFrom(Location from) {
			this.from = from;
			return this;
		}

		public Builder withTo(Location to) {
			this.to = to;
			return this;
		}

		@JsonIgnore
		public Builder withDepartureTime(ZonedDateTime departureTime) {
			this.departureTime = Optional.ofNullable(departureTime);
			return this;
		}
		
		@JsonProperty
        public Builder withDepartureTime(String departureTime) {
        	this.departureTime = Optional.of(SprouteUtils.parseZonedDateTime(departureTime, "departureTime"));
            return this;
        }

        @JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = Optional.ofNullable(arrivalTime);
			return this;
		}
		
        @JsonProperty
        public Builder withArrivalTime(String arrivalTime) {
        	this.arrivalTime = Optional.of(SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime"));
            return this;
        }

		public Builder withLengthMeters(int lengthMeters) {
			this.lengthMeters = lengthMeters;
			return this;
		}

		public Builder withDurationSeconds(int durationSeconds) {
			this.durationSeconds = durationSeconds;
			return this;
		}

		public Builder withSegments(List<RouteSegment> segments) {
			this.segments = new ArrayList<RouteSegment>(segments);
			return this;
		}

		public Route build() {
			validate();
			return new Route(this);
		}

		private void validate() {
			Preconditions.checkArgument(from != null, "from is mandatory but missing");
			Preconditions.checkArgument(to != null, "to is mandatory but missing");
			Preconditions.checkArgument(lengthMeters != null, "lengthMeters is mandatory but missing");
			Preconditions.checkArgument(durationSeconds != null, "durationSeconds is mandatory but missing");
			
			Preconditions.checkArgument(lengthMeters >= 0, "lengthMeters must be >= 0, but was %s", lengthMeters);
			Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s", durationSeconds);
			
			Preconditions.checkArgument(!segments.isEmpty(), "segments must not be empty");
		}
	}

}
