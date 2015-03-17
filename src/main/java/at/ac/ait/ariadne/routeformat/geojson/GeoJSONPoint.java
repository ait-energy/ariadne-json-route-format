package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoJSONPoint implements GeoJSONGeometryObject{
	
	public GeoJSONPoint() {
	}
	
	public GeoJSONPoint(CoordinatePoint point) {
		coordinates = point.asNewList();
	}

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.Point;

	@JsonProperty(required = true)
	/** a pair of coordinates: X and Y (=longitude and latitude) */
	public List<BigDecimal> coordinates = new ArrayList<>();

}
