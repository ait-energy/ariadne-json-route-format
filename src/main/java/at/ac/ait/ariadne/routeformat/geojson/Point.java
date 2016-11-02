package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class Point implements GeoJSONGeometryObject {

	private List<BigDecimal> coordinates = new ArrayList<>();

	// -- getters

	/** a pair of coordinates: X and Y (=longitude and latitude) */
	@JsonProperty(required = true)
	public List<BigDecimal> getCoordinates() {
		return coordinates;
	}

	// -- setters

	public Point setCoordinates(List<BigDecimal> coordinates) {
		this.coordinates = coordinates;
		return this;
	}

	// --

	public static Point create(CoordinatePoint point) {
		return new Point().setCoordinates(point.asNewList());
	}

	@Override
	public String toWKT() {
		return getTypeName() + " " + WKTUtil.getCoordinateStringPointOrLineString(Arrays.asList(coordinates));
	}

	@Override
	public boolean isEmpty() {
		return coordinates.isEmpty();
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(coordinates != null, "coordinate is mandatory but missing");
		Preconditions.checkArgument(coordinates.size() == 2 || coordinates.size() == 3,
				"coordinates must be of length 2 or 3");
	}

	@Override
	public String toString() {
		return toWKT();
	}

}
