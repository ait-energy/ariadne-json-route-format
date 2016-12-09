package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A point that may be empty, i.e. not contain a coordinate.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONPoint implements GeoJSONGeometryObject {

    private Optional<GeoJSONCoordinate> coordinates = Optional.empty();

    // -- getters

    @JsonProperty(required = true)
    public Optional<GeoJSONCoordinate> getCoordinates() {
        return coordinates;
    }

    // -- setters

    public GeoJSONPoint setCoordinates(GeoJSONCoordinate coordinates) {
        this.coordinates = Optional.ofNullable(coordinates);
        return this;
    }

    // --

    public static GeoJSONPoint create(GeoJSONCoordinate point) {
        return new GeoJSONPoint().setCoordinates(point);
    }

    @Override
    public boolean isEmpty() {
        return !coordinates.isPresent();
    }

    @Override
    public void validate() {
    }

    @Override
    public String toWKT() {
        List<GeoJSONCoordinate> list = coordinates.isPresent() ? Arrays.asList(coordinates.get())
                : Collections.emptyList();
        return getTypeName() + " " + WKTUtil.getCoordinateStringPointOrLineString(list);
    }

    @Override
    public String toString() {
        return toWKT();
    }

}
