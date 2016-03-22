package at.ac.ait.ariadne.routeformat.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@JsonSubTypes({  
//    @Type(value = GeoJSONLineString.class, name = "GeoJSONLineString"),
//    @Type(value = GeoJSONPoint.class, name = "GeoJSONPoint")
//})
/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public interface GeoJSONGeometryObject {

	/**
	 * @return the <a href="https://en.wikipedia.org/wiki/Well-known_text">well
	 *         known text</a> representation of this geometry object
	 */
	@JsonIgnore
	String toWKT();

	/**
	 * @return <code>true</code> if the element does not contain any coordinates
	 */
	@JsonIgnore
	boolean isEmpty();

}
