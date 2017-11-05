package at.ac.ait.ariadne.routeformat.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = GeoJSONPoint.class, name = "Point"),
        @Type(value = GeoJSONLineString.class, name = "LineString"),
        @Type(value = GeoJSONPolygon.class, name = "Polygon"),
        @Type(value = GeoJSONMultiPolygon.class, name = "MultiPolygon") })
public interface GeoJSONGeometryObject extends Validatable {

    @JsonIgnore
    String getTypeName();

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
