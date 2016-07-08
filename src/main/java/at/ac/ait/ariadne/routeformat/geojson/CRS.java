package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.ALWAYS)
public class CRS {

	public static final CRS WGS84 = new CRS();

	@JsonProperty(required = true)
	public final String type = "name";

	@JsonProperty(required = true)
	public Map<String, Object> properties = new TreeMap<>();

	/**
	 * creates a new WGS84 {@link CRS}
	 */
	public CRS() {
		properties.put("name", "urn:ogc:def:crs:OGC:1.3:CRS84");
	}

}
