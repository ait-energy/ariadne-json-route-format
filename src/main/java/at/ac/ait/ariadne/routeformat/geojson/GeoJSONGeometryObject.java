package at.ac.ait.ariadne.routeformat.geojson;

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
	String toWKT();

}
