package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONPolygon implements GeoJSONGeometryObject {

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.Polygon;

	/**
	 * Coordinates of a polygon are an array of LinearRing coordinate arrays
	 * (the first and the last coordinate must be the same, thereby closing the
	 * ring). The first element in the array represents the exterior ring. Any
	 * subsequent elements represent interior rings (or holes).
	 * <p>
	 * The inner list of {@link BigDecimal} is always a pair of coordinates: X
	 * and Y (=longitude and latitude)
	 */
	@JsonProperty(required = true)
	public List<List<List<BigDecimal>>> coordinates = new ArrayList<>();

	public GeoJSONPolygon() {
	}

	public GeoJSONPolygon(List<List<CoordinatePoint>> points) {
		for (List<CoordinatePoint> linearRing : points) {
			List<List<BigDecimal>> ring = new ArrayList<>();
			for (CoordinatePoint point : linearRing)
				ring.add(point.asNewList());
			coordinates.add(ring);
		}
	}

	@Override
	public String toWKT() {
		StringBuilder sb = new StringBuilder(type.name().toUpperCase()).append(" ");
		if (coordinates.isEmpty())
			return sb.append("EMPTY").toString();

		sb.append(WKTUtil.getCoordinateStringPolygon(coordinates));
		return sb.toString();
	}

	@Override
	public boolean isEmpty() {
		return coordinates.isEmpty();
	}

}