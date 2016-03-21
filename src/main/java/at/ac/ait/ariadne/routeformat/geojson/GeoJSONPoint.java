package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONPoint implements GeoJSONGeometryObject {

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.Point;

	@JsonProperty(required = true)
	/** a pair of coordinates: X and Y (=longitude and latitude) */
	public List<BigDecimal> coordinates = new ArrayList<>();

	public GeoJSONPoint() {
	}

	public GeoJSONPoint(CoordinatePoint point) {
		coordinates = point.asNewList();
	}

	@Override
	public String toString() {
		return "GeoJSONPoint [coordinates=" + coordinates + "]";
	}

	@Override
	public String toWKT() {
		StringBuilder sb = new StringBuilder(type.name().toUpperCase()).append(" ");
		if (coordinates.isEmpty())
			return sb.append("EMPTY").toString();

		sb.append(WKTUtil.getCoordinateStringPointOrLineString(Arrays.asList(coordinates)));
		return sb.toString();
	}

}
