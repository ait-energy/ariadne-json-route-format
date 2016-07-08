package at.ac.ait.ariadne.routeformat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.RequestModeOfTransport.Builder2;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * Encapsulates a {@link ModeOfTransport} and attributes exclusive to this mode
 * of transport.
 * <p>
 * For simple cases use the static standard modes such as
 * {@link ModeOfTransport#STANDARD_BICYCLE} to build a
 * {@link RequestModeOfTransport}.
 * <p>
 * For complex cases, such as public transport or sharing providers the routing
 * service would ideally provide an interface to retrieve the supported modes.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = RequestModeOfTransport.class, name = "RequestModeOfTransport"),
        @JsonSubTypes.Type(value = RequestPublicTransportModeOfTransport.class, name = "RequestPublicTransportModeOfTransport") })
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class RequestModeOfTransport {

    private final ModeOfTransport modeOfTransport;
    private final Optional<Integer> maximumDistanceMeters;
    private final Optional<Integer> maximumTravelTimeSeconds;
    private final Optional<String> speed;
    private final List<Location> locations;
    private final Map<String, Object> additionalInfo;

    RequestModeOfTransport(Builder<?> builder) {
        this.modeOfTransport = builder.modeOfTransport;
        this.maximumDistanceMeters = builder.maximumDistanceMeters;
        this.maximumTravelTimeSeconds = builder.maximumTravelTimeSeconds;
        this.speed = builder.speed;
        this.locations = builder.locations;
        this.additionalInfo = builder.additionalInfo;
    }

    @JsonProperty(required = true)
    public ModeOfTransport getModeOfTransport() {
        return modeOfTransport;
    }

    /**
     * maximum distance to be covered with this mode of transport in one
     * {@link RouteSegment}
     */
    public Optional<Integer> getMaximumDistanceMeters() {
        return maximumDistanceMeters;
    }

    /**
     * maximum travel time with this mode of transport in one
     * {@link RouteSegment}
     */
    public Optional<Integer> getMaximumTravelTimeSeconds() {
        return maximumTravelTimeSeconds;
    }

    /**
     * speed for this mode of transport - either one of {@link Constants.Speed}
     * or a number in kph
     */
    public Optional<String> getSpeed() {
        return speed;
    }

    /**
     * Locations where this mode of transport is available. E.g. the location of
     * private vehicles (typically a bicycle, car and/or motorcycle) that can be
     * used when calculating the route. If not set vehicles can be assumed at
     * their default location, e.g. private vehicles are available at the
     * starting point of the route.
     */
    @JsonProperty
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Other attributes exclusive to this {@link ModeOfTransport}.
     */
    @JsonProperty
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    public static abstract class Builder<T extends Builder<T>> {
        ModeOfTransport modeOfTransport;
        private Optional<Integer> maximumDistanceMeters = Optional.empty();
        private Optional<Integer> maximumTravelTimeSeconds = Optional.empty();
        private Optional<String> speed = Optional.empty();
        private List<Location> locations = Collections.emptyList();
        private Map<String, Object> additionalInfo = Collections.emptyMap();

        protected abstract T self();

        public T withModeOfTransport(ModeOfTransport modeOfTransport) {
            this.modeOfTransport = modeOfTransport;
            return self();
        }

        public T withMaximumDistanceMeters(Integer maximumDistanceMeters) {
            this.maximumDistanceMeters = Optional.ofNullable(maximumDistanceMeters);
            return self();
        }

        public T withMaximumTravelTimeSeconds(Integer maximumTravelTimeSeconds) {
            this.maximumTravelTimeSeconds = Optional.ofNullable(maximumTravelTimeSeconds);
            return self();
        }

        public T withSpeed(String speed) {
            this.speed = Optional.ofNullable(speed);
            return self();
        }

        public T withLocations(List<Location> locations) {
            this.locations = ImmutableList.copyOf(locations);
            return self();
        }

        public T withAdditionalInfo(Map<String, Object> additionalInfo) {
            this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
            return self();
        }

        public RequestModeOfTransport build() {
            validate();
            return new RequestModeOfTransport(this);
        }

        void validate() {
            Preconditions.checkArgument(modeOfTransport != null, "modeOfTransport is mandatory but missing");
            Utils.enforcePositiveInteger(maximumDistanceMeters, "maximumDistanceMeters");
            Utils.enforcePositiveInteger(maximumTravelTimeSeconds, "maximumTravelTimeSeconds");
        }
    }

    static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
