package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.ariadne.routeformat.location.Location.Builder2;

/**
 * A very basic version of a location, in its minimal form it only contains a
 * coordinate.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Location.class, name = "Location"),
        @JsonSubTypes.Type(value = PointOfInterest.class, name = "PointOfInterest"),
        @JsonSubTypes.Type(value = PublicTransportStop.class, name = "PublicTransportStop"),
        @JsonSubTypes.Type(value = SharingStation.class, name = "SharingStation") })
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class Location {
    private final GeoJSONFeature<GeoJSONPoint> coordinate;
    private final Optional<Address> address;
    private final Map<String, String> additionalInfo;

    public GeoJSONFeature<GeoJSONPoint> getCoordinate() {
        return coordinate;
    }

    public Optional<Address> getAddress() {
        return address;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    Location(Builder<?> builder) {
        this.coordinate = builder.coordinate;
        this.address = builder.address;
        this.additionalInfo = builder.additionalInfo;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    @Override
    public String toString() {
        return "Location [coordinate=" + coordinate + ", address=" + address + ", additionalInfo=" + additionalInfo
                + "]";
    }

    // builder pattern for subclassing:
    // http://webcache.googleusercontent.com/search?q=cache:OdpLdr2dVJgJ:https://www.java.net/blog/emcmanus/archive/2010/10/25/using-builder-pattern-subclasses+&cd=1&hl=de&ct=clnk&gl=at&client=ubuntu
    public static abstract class Builder<T extends Builder<T>> {

        private GeoJSONFeature<GeoJSONPoint> coordinate;
        private Optional<Address> address = Optional.empty();
        private Map<String, String> additionalInfo = Collections.emptyMap();

        protected abstract T self();

        public T withCoordinate(GeoJSONFeature<GeoJSONPoint> coordinate) {
            this.coordinate = coordinate;
            return self();
        }

        public T withAddress(Address address) {
            this.address = Optional.ofNullable(address);
            return self();
        }

        public T withAdditionalInfo(Map<String, String> additionalInfo) {
            this.additionalInfo = ImmutableSortedMap.copyOf(additionalInfo);
            return self();
        }

        public Location build() {
            validate();
            return new Location(this);
        }

        void validate() {
            Preconditions.checkArgument(coordinate != null, "coordinate is mandatory but missing");
        }
    }

    static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
