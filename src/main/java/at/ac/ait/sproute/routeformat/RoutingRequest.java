package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import at.ac.ait.sproute.routeformat.RoutingRequest.Builder;
import at.ac.ait.sproute.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.sproute.routeformat.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RoutingRequest {
	private final String serviceId;
	private final Location from;
	private final List<Location> via;
	private final Location to;
	private final Set<GeneralizedModeOfTransportType> modesOfTransport; // FIXME should be more detailed, otherwise we
																		// can not pick the detailed public MOT,
																		// sharing,...
	private final String optimizedFor;
	private final Optional<ZonedDateTime> departureTime;
	private final Optional<ZonedDateTime> arrivalTime;
	private final Optional<Integer> acceptedDelayMinutes;
	private final Set<Sproute.AccessibilityRestriction> accessibilityRestrictions;
	private final Map<GeneralizedModeOfTransportType, List<Location>> privateVehicleLocations;
	private final Optional<String> language;
	private final Map<String, Object> additionalInfo;

	/**
	 * Defines which routing service (a combination of maps, timeseries,..) will be / was used for routing
	 */
	@JsonProperty(required = true)
	public String getServiceId() {
		return serviceId;
	}

	@JsonProperty(required = true)
	public Location getFrom() {
		return from;
	}

	public List<Location> getVia() {
		return via;
	}

	@JsonProperty(required = true)
	public Location getTo() {
		return to;
	}

	/**
	 * One or more modes of transport that will be / were used for routing. In case of a single mode of transport
	 * unimodal routing is requested, in case of several modes of transport intermodal routing is requested.
	 * <p>
	 * In case of intermodal routing it is guaranteed that the returned set contains
	 * {@link GeneralizedModeOfTransportType#FOOT}.
	 */
	@JsonProperty(required = true)
	public Set<GeneralizedModeOfTransportType> getModesOfTransport() {
		return modesOfTransport;
	}

	/**
	 * Criteria the route will be / was optimized for, e.g. shortest travel time, which is also the default
	 */
	@JsonProperty(required = true)
	public String getOptimizedFor() {
		return optimizedFor;
	}

	/**
	 * Requested departure time for the route. Mutual exclusive with {@link #getArrivalTime()}, it is guaranteed that
	 * exactly one of the two times is set.
	 * <p>
	 * If neither departure time nor arrival time were set in the builder a departure time of 'now' is automatically
	 * added.
	 * <p>
	 * The supported formats are defined in {@link ZonedDateTime} which uses ISO 8601 with time zone. One example is
	 * "YYYY-MM-DDTHH:MMZ", where T is the letter T, Z is the time zone (in either HH:MM, HHMM, HH format or the letter
	 * Z for UTC). E.g. "2015-01-31T18:05+0100". As output the default toString() of {@link ZonedDateTime} is used.
	 */
	@JsonProperty
	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	/**
	 * @see #getDepartureTime()
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getDepartureTimeAsZonedDateTime() {
		return departureTime;
	}

	/**
	 * Requested arrival time for the route. Mutual exclusive with {@link #getDepartureTime()}, it is guaranteed that
	 * exactly one of the two times is set.
	 * <p>
	 * The format is the same as for {@link #getDepartureTime()}.
	 */
	@JsonProperty
	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	/**
	 * @see #getArrivalTime()
	 */
	@JsonIgnore
	public Optional<ZonedDateTime> getArrivalTimeAsZonedDateTime() {
		return arrivalTime;
	}

	/**
	 * If present then this specifies the accepted delay for {@link #getDepartureTime()} or {@link #getArrivalTime()} -
	 * depending on which one is set. The accepted interval is specified as follows: arrival time until arrival time +
	 * delay, departure time until departure time + delay.
	 * <p>
	 * Note, that for non-public transport routes this probably has no effect.
	 * 
	 * @return empty / a non-negative number of minutes
	 */
	public Optional<Integer> getAcceptedDelayMinutes() {
		return acceptedDelayMinutes;
	}

	public Set<Sproute.AccessibilityRestriction> getAccessibilityRestrictions() {
		return accessibilityRestrictions;
	}

	/**
	 * @return the locations for private vehicles (typically a bicycle, car and/or motorcycle) that can be used when
	 *         calculating the route. Note, that the vehicle probably won't be used in the route if its mode of
	 *         transport is net set in {@link #getModesOfTransport()}.
	 */
	public Map<GeneralizedModeOfTransportType, List<Location>> getPrivateVehicleLocations() {
		return privateVehicleLocations;
	}

	/**
	 * @return the preferred language of the user. E.g. street or POI names can be provided in this language if
	 *         available
	 */
	public Optional<String> getLanguage() {
		return language;
	}

	/**
	 * @return a map of parameters to be considered during the routing process
	 */
	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	private RoutingRequest(Builder builder) {
		this.serviceId = builder.serviceId;
		this.from = builder.from;
		this.via = builder.via;
		this.to = builder.to;
		this.modesOfTransport = builder.modesOfTransport;
		this.optimizedFor = builder.optimizedFor;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.acceptedDelayMinutes = builder.acceptedDelayMinutes;
		this.accessibilityRestrictions = builder.accessibilityRestrictions;
		this.privateVehicleLocations = builder.privateVehicleLocations;
		this.language = builder.language;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String serviceId;
		private Location from;
		private List<Location> via = Collections.emptyList();
		private Location to;
		private Set<GeneralizedModeOfTransportType> modesOfTransport = Collections.emptySet();
		private String optimizedFor;
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private Optional<Integer> acceptedDelayMinutes = Optional.empty();
		private Set<Sproute.AccessibilityRestriction> accessibilityRestrictions = Collections.emptySet();
		private Map<GeneralizedModeOfTransportType, List<Location>> privateVehicleLocations = Collections.emptyMap();
		private Optional<String> language = Optional.empty();
		private Map<String, Object> additionalInfo = Collections.emptyMap();

		public Builder withServiceId(String serviceId) {
			this.serviceId = serviceId;
			return this;
		}

		public Builder withFrom(Location from) {
			this.from = from;
			return this;
		}

		public Builder withVia(List<Location> via) {
			this.via = ImmutableList.copyOf(via);
			return this;
		}

		public Builder withTo(Location to) {
			this.to = to;
			return this;
		}

		@JsonIgnore
		public Builder withModesOfTransport(String modesOfTransport) {
			this.modesOfTransport = SprouteUtils.parseModesOfTransport(modesOfTransport, "modesOfTransport");
			return this;
		}

		@JsonProperty
		public Builder withModesOfTransport(Set<GeneralizedModeOfTransportType> modesOfTransport) {
			this.modesOfTransport = ImmutableSet.copyOf(modesOfTransport);
			return this;
		}

		public Builder withOptimizedFor(String optimizedFor) {
			this.optimizedFor = optimizedFor;
			return this;
		}

		@JsonIgnore
		public Builder withDepartureTime(ZonedDateTime departureTime) {
			this.departureTime = Optional.ofNullable(departureTime);
			return this;
		}

		@JsonProperty
		public Builder withDepartureTime(String departureTime) {
			if (departureTime == null) {
				this.departureTime = Optional.empty();
			} else if (departureTime.equalsIgnoreCase("NOW")) {
				this.departureTime = Optional.of(ZonedDateTime.now());
			} else {
				this.departureTime = Optional.of(SprouteUtils.parseZonedDateTime(departureTime, "departureTime"));
			}
			return this;
		}

		@JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = Optional.ofNullable(arrivalTime);
			return this;
		}

		@JsonProperty
		public Builder withArrivalTime(String arrivalTime) {
			if (arrivalTime == null) {
				this.arrivalTime = Optional.empty();
			} else if (arrivalTime.equalsIgnoreCase("NOW")) {
				this.arrivalTime = Optional.of(ZonedDateTime.now());
			} else {
				this.arrivalTime = Optional.of(SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime"));
			}
			return this;
		}

		public Builder withAcceptedDelayMinutes(Integer acceptedDelayMinutes) {
			this.acceptedDelayMinutes = Optional.ofNullable(acceptedDelayMinutes);
			return this;
		}

		public Builder withAccessibilityRestrictions(Set<Sproute.AccessibilityRestriction> accessibilityRestrictions) {
			this.accessibilityRestrictions = ImmutableSet.copyOf(accessibilityRestrictions);
			return this;
		}

		public Builder withPrivateVehicleLocations(
				Map<GeneralizedModeOfTransportType, List<Location>> privateVehicleLocations) {
			this.privateVehicleLocations = privateVehicleLocations;
			return this;
		}

		public Builder withLanguage(String language) {
			this.language = Optional.ofNullable(language);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public RoutingRequest build() {
			validate();
			return new RoutingRequest(this);
		}

		private void validate() {
			Preconditions.checkArgument(serviceId != null, "serviceId is mandatory but missing");
			Preconditions.checkArgument(from != null, "from is mandatory but missing");
			Preconditions.checkArgument(to != null, "to is mandatory but missing");
			Preconditions.checkArgument(modesOfTransport != null || modesOfTransport.isEmpty(),
					"modesOfTransport is mandatory but missing/empty");
			Preconditions.checkArgument(modesOfTransport.size() >= 1, ">= 1 modesOfTransport must be used");
			if (modesOfTransport.size() > 1 && !modesOfTransport.contains(GeneralizedModeOfTransportType.FOOT)) {
				modesOfTransport.add(GeneralizedModeOfTransportType.FOOT);
			}

			if (optimizedFor == null) {
				optimizedFor = "TRAVELTIME";
			}

			Preconditions.checkArgument(!(departureTime.isPresent() && arrivalTime.isPresent()),
					"departureTime and arrivalTime are mutually exclusive, only one can be set at once");
			if (!departureTime.isPresent() && !arrivalTime.isPresent()) {
				departureTime = Optional.of(ZonedDateTime.now());
			}

			if (acceptedDelayMinutes.isPresent())
				Preconditions.checkArgument(acceptedDelayMinutes.get() >= 0, "accepted delay must not be negative");
		}
	}

}
