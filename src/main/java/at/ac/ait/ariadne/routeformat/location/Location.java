package at.ac.ait.ariadne.routeformat.location;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.Validatable;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;

/**
 * A generic {@link Location}.
 * <p>
 * In its minimal form it only contains a coordinate point.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Location.class), @JsonSubTypes.Type(value = PointOfInterest.class),
		@JsonSubTypes.Type(value = PublicTransportStop.class), @JsonSubTypes.Type(value = SharingStation.class),
		@JsonSubTypes.Type(value = Parking.class) })
@JsonInclude(Include.NON_EMPTY)
public class Location<T extends Location<T>> implements Validatable {
	private GeoJSONFeature<GeoJSONPoint> coordinate;
	private Optional<GeoJSONFeature<?>> complexGeometry = Optional.empty();
	private Optional<Address> address = Optional.empty();
	private Map<String, String> additionalInfo = new TreeMap<>();

	// -- getters

	/**
	 * Convenience getter for the actual coordinate wrapped in the GeoJSON
	 * feature
	 * 
	 * @throws NullPointerException
	 *             in case the {@link Location}'s point feature did not contain
	 *             a coordinate
	 */
	@JsonIgnore
	public GeoJSONCoordinate getSimpleCoordinate() {
		return coordinate.getGeometry().getCoordinates().get();
	}

	/**
	 * Get a point representing this {@link Location}. This information is
	 * mandatory and also available for more complex locations, e.g. for a
	 * {@link PointOfInterest} polygon (see {@link #getComplexGeometry()}) of a
	 * building.
	 * <p>
	 * In case this {@link Location} is part of a {@link RoutingRequest}
	 * (from/to/via) this point is used as input for routing.
	 */
	public GeoJSONFeature<GeoJSONPoint> getCoordinate() {
		return coordinate;
	}

	/**
	 * Get the real / complex geometry (e.g. a line or a polygon) of this
	 * location in case the representation as a simple point provided in
	 * {@link #getCoordinate()} is not detailed enough.
	 */
	public Optional<GeoJSONFeature<?>> getComplexGeometry() {
		return complexGeometry;
	}

	public Optional<Address> getAddress() {
		return address;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	@SuppressWarnings("unchecked")
	@JsonProperty
	public T setCoordinate(GeoJSONFeature<GeoJSONPoint> coordinate) {
		this.coordinate = coordinate;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public T setCoordinate(GeoJSONCoordinate coordinate) {
		this.coordinate = GeoJSONFeature.createPointFeature(coordinate);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setComplexGeometry(GeoJSONFeature<?> complexGeometry) {
		this.complexGeometry = Optional.ofNullable(complexGeometry);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setAddress(Address address) {
		this.address = Optional.ofNullable(address);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return (T) this;
	}

	// --

	public static Location<?> createMinimal(GeoJSONCoordinate position) {
		return new Location<>().setCoordinate(position);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(coordinate != null, "coordinate is mandatory but missing");
		coordinate.validate();
		complexGeometry.ifPresent(c -> c.validate());
		address.ifPresent(a -> a.validate());
	}

	@Override
	public String toString() {
		return "Location [coordinate=" + coordinate + ", address=" + address + ", additionalInfo=" + additionalInfo
				+ "]";
	}

}
