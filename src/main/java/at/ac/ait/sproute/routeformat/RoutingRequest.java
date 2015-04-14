package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.RoutingRequest.Builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
public class RoutingRequest {
	private int nr;
	private Location from;
	private Location to;
	private Optional<ZonedDateTime> departureTime;
	private Optional<ZonedDateTime> arrivalTime;
	private List<Route> routes;

	@JsonProperty(required = true)
	public int getNr() {
		return nr;
	}
	
	@JsonProperty(required = true)
	public Location getFrom() {
		return from;
	}

	@JsonProperty(required = true)
	public Location getTo() {
		return to;
	}

	@JsonProperty
	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	@JsonProperty
	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	@JsonProperty(required = true)
	public List<Route> getRoutes() {
		return routes;
	}

	private RoutingRequest(Builder builder) {
		this.nr = builder.nr.get();
		this.from = builder.from;
		this.to = builder.to;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.routes = builder.routes;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Optional<Integer> nr = Optional.empty();
		private Location from;
		private Location to;
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private List<Route> routes;

		public Builder withNr(int nr) {
			this.nr = Optional.of(nr);
			return this;
		}
		
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
			this.departureTime = Optional.of(departureTime);
			return this;
		}
		
		@JsonProperty
        public Builder withDepartureTime(String departureTime) {
        	this.departureTime = Optional.of(SprouteUtils.parseZonedDateTime(departureTime, "departureTime"));
            return this;
        }

        @JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = Optional.of(arrivalTime);
			return this;
		}
		
        @JsonProperty
        public Builder withArrivalTime(String arrivalTime) {
        	this.arrivalTime = Optional.of(SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime"));
            return this;
        }

		public Builder withRoutes(List<Route> routes) {
			this.routes = new ArrayList<Route>(routes);
			return this;
		}

		public RoutingRequest build() {
			validate();
			return new RoutingRequest(this);
		}

		private void validate() {
			Preconditions.checkArgument(nr.isPresent(), "nr is mandatory but missing");
			Preconditions.checkNotNull(from, "from is mandatory but missing");
			Preconditions.checkNotNull(to, "to is mandatory but missing");
		}
	}

}
