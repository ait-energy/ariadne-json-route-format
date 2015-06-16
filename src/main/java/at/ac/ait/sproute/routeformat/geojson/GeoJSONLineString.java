package at.ac.ait.sproute.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
		for(CoordinatePoint point : points)
			coordinates.add(point.asNewList());
	}

}
