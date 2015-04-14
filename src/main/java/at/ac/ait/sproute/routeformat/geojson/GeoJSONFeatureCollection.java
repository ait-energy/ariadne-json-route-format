package at.ac.ait.sproute.routeformat.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A feature collection restricted to features of just one type
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class GeoJSONFeatureCollection<T extends GeoJSONGeometryObject> {

	public GeoJSONFeatureCollection() {
	}

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.FeatureCollection;

	@JsonProperty(required = true)
	public List<GeoJSONFeature<T>> features = new ArrayList<>();

}