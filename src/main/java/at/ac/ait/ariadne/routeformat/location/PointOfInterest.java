package at.ac.ait.ariadne.routeformat.location;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;

/**
 * A point of interest.
 * <p>
 * In its minimal form it does not contain any additional attributes, but just
 * the information that this {@link Location} is a point of interest.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class PointOfInterest extends Location<PointOfInterest> {

	private Optional<String> poiType = Optional.empty();
	private Optional<String> name = Optional.empty();

	// -- getters

	public Optional<String> getPoiType() {
		return poiType;
	}

	public Optional<String> getName() {
		return name;
	}

	// -- setters

	public PointOfInterest setPoiType(String poiType) {
		this.poiType = Optional.ofNullable(poiType);
		return this;
	}

	public PointOfInterest setName(String name) {
		this.name = Optional.ofNullable(name);
		return this;
	}

	// --

	public static PointOfInterest createMinimal(CoordinatePoint position) {
		return new PointOfInterest().setCoordinate(position);
	}

	@Override
	public void validate() {
		super.validate();
		// no other requirements
	}

	@Override
	public String toString() {
		return "PointOfInterest [poiType=" + poiType + ", name=" + name + ", address=" + getAddress() + "]";
	}

}
