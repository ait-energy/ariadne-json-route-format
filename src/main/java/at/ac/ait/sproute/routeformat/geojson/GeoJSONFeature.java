package at.ac.ait.sproute.routeformat.geojson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mstraub
 */
public class GeoJSONFeature<T extends GeoJSONGeometryObject> {
	
	public static GeoJSONFeature<GeoJSONPoint> newPointFeature(CoordinatePoint point) {
		GeoJSONFeature<GeoJSONPoint> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONPoint(point);
		return feature;
	}
	
	public static GeoJSONFeature<GeoJSONLineString> newLineStringFeature(List<CoordinatePoint> points) {
		GeoJSONFeature<GeoJSONLineString> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONLineString(points);
		return feature;
	}
	
	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.Feature;

	@JsonProperty(required = true)
	public T geometry;
	
	@JsonProperty(required = true)
	/**
	 * Unrestricted possibility to store additional information, e.g. properties
	 * to be used in visualizations
	 */
	public Map<String, Object> properties = new HashMap<>();

}
