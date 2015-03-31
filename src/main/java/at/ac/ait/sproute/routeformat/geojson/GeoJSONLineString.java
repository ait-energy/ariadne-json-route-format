package at.ac.ait.sproute.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJSONLineString implements GeoJSONGeometryObject {

	public GeoJSONLineString() {
	}
	
	public GeoJSONLineString(List<CoordinatePoint> points) {
		for(CoordinatePoint point : points)
			coordinates.add(point.asNewList());
	}

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.LineString;

	@JsonProperty(required = true)
	/** pairs of coordinates: X and Y (=longitude and latitude) */
	public List<List<BigDecimal>> coordinates = new ArrayList<>();

}
