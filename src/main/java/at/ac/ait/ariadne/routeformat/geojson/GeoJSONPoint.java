package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONPoint implements GeoJSONGeometryObject {

	private Optional<Coordinate> coordinates = Optional.empty();

	// -- getters

	@JsonProperty(required = true)
	public Optional<Coordinate> getCoordinates() {
		return coordinates;
	}

	// -- setters

	public GeoJSONPoint setCoordinates(Coordinate coordinates) {
		this.coordinates = Optional.ofNullable(coordinates);
		return this;
	}

	// --

	public static GeoJSONPoint create(Coordinate point) {
		return new GeoJSONPoint().setCoordinates(point);
	}

	@Override
	public String toWKT() {
		List<Coordinate> list = coordinates.isPresent() ? Arrays.asList(coordinates.get()) : Collections.emptyList();
		return getTypeName() + " " + WKTUtil.getCoordinateStringPointOrLineString(list);
	}

	@Override
	public boolean isEmpty() {
		return !coordinates.isPresent();
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(coordinates.isPresent(), "coordinate is mandatory but missing (for valid GeoJSON)");
	}

	@Override
	public String toString() {
		return toWKT();
	}

}
