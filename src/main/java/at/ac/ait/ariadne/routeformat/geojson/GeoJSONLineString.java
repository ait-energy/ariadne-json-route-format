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
public class GeoJSONLineString implements GeoJSONGeometryObject {

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.LineString;

	@JsonProperty(required = true)
	/** pairs of coordinates: X and Y (=longitude and latitude) */
	public List<List<BigDecimal>> coordinates = new ArrayList<>();

	public GeoJSONLineString() {
	}

	public GeoJSONLineString(List<CoordinatePoint> points) {
		for (CoordinatePoint point : points)
			coordinates.add(point.asNewList());
	}

	/**
	 * @param fromIndex
	 *            low coordinate-pair (inclusive) of the subLineString
	 * @param toIndex
	 *            high coordinate-pair (exclusive) of the subLineString
	 * @return a deep copy of the requested sub part
	 */
	public GeoJSONLineString subLineString(int fromIndex, int toIndex) {
		GeoJSONLineString sub = new GeoJSONLineString();
		sub.coordinates = new ArrayList<>(coordinates.subList(fromIndex, toIndex));
		return sub;
	}

	@Override
	public String toWKT() {
		StringBuilder sb = new StringBuilder(type.name().toUpperCase()).append(" ");
		sb.append(WKTUtil.getCoordinateStringPointOrLineString(coordinates));
		return sb.toString();
	}
}
