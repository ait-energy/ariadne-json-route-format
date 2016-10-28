package at.ac.ait.ariadne.routeformat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONMultiPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * Encapsulates a {@link ModeOfTransport} and attributes exclusive to this mode
 * of transport.
 * <p>
 * In its minimal form it only consists of a {@link ModeOfTransport}.
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
		@JsonSubTypes.Type(value = RequestPTModeOfTransport.class, name = "RequestPTModeOfTransport") })
@JsonInclude(Include.NON_EMPTY)
public class RequestModeOfTransport<T extends RequestModeOfTransport<T>> implements Validatable {

	ModeOfTransport modeOfTransport;
	private Optional<Integer> maximumDistanceMeters = Optional.empty();
	private Optional<Integer> maximumTravelTimeSeconds = Optional.empty();
	private Optional<String> speed = Optional.empty();
	private List<Location<?>> locations = new ArrayList<>();
	private Optional<GeoJSONFeature<GeoJSONMultiPolygon>> forbiddenAreas = Optional.empty();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

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
	public List<Location<?>> getLocations() {
		return locations;
	}

	/**
	 * A multipolygon defining areas the route must not cross
	 */
	@JsonProperty
	public Optional<GeoJSONFeature<GeoJSONMultiPolygon>> getForbiddenAreas() {
		return forbiddenAreas;
	}

	/**
	 * Other attributes exclusive to this {@link ModeOfTransport}.
	 */
	@JsonProperty
	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	@SuppressWarnings("unchecked")
	public T setModeOfTransport(ModeOfTransport modeOfTransport) {
		this.modeOfTransport = modeOfTransport;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setMaximumDistanceMeters(Integer maximumDistanceMeters) {
		this.maximumDistanceMeters = Optional.ofNullable(maximumDistanceMeters);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setMaximumTravelTimeSeconds(Integer maximumTravelTimeSeconds) {
		this.maximumTravelTimeSeconds = Optional.ofNullable(maximumTravelTimeSeconds);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setSpeed(String speed) {
		this.speed = Optional.ofNullable(speed);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setLocations(List<Location<?>> locations) {
		this.locations = new ArrayList<>(locations);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setForbiddenAreas(GeoJSONFeature<GeoJSONMultiPolygon> forbiddenAreas) {
		this.forbiddenAreas = Optional.ofNullable(forbiddenAreas);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return (T) this;
	}

	// --

	public static RequestModeOfTransport<?> createMinimal(ModeOfTransport modeOfTransport) {
		return new RequestModeOfTransport<>().setModeOfTransport(modeOfTransport);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(modeOfTransport != null, "modeOfTransport is mandatory but missing");
		modeOfTransport.validate();
		locations.forEach(l -> l.validate());
		Utils.checkPositiveIntegerOrEmpty(maximumDistanceMeters, "maximumDistanceMeters");
		Utils.checkPositiveIntegerOrEmpty(maximumTravelTimeSeconds, "maximumTravelTimeSeconds");
	}

	@Override
	public String toString() {
		return "RequestModeOfTransport [modeOfTransport=" + modeOfTransport + ", maximumDistanceMeters="
				+ maximumDistanceMeters + ", maximumTravelTimeSeconds=" + maximumTravelTimeSeconds + ", speed=" + speed
				+ ", locations=" + locations + ", forbiddenAreas=" + forbiddenAreas + ", additionalInfo="
				+ additionalInfo + "]";
	}

}
