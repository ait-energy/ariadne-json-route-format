package at.ac.ait.ariadne.routeformat.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

/**
 * A linestring that may be empty, i.e. not contain a coordinate, but may never
 * consist of a single coordinate.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONLineString implements GeoJSONGeometryObject {

    private List<GeoJSONCoordinate> coordinates = new ArrayList<>();

    // -- getters

    @JsonProperty(required = true)
    public List<GeoJSONCoordinate> getCoordinates() {
        return coordinates;
    }

    // -- setters

    public GeoJSONLineString setCoordinates(List<GeoJSONCoordinate> coordinates) {
        this.coordinates = new ArrayList<>(coordinates);
        return this;
    }

    // --

    public static GeoJSONLineString create(List<GeoJSONCoordinate> points) {
        return new GeoJSONLineString().setCoordinates(points);
    }

    /**
     * @param fromIndex
     *            low coordinate-pair (inclusive) of the subLineString
     * @param toIndex
     *            high coordinate-pair (exclusive) of the subLineString
     * @return a shallow copy of the requested sub part, i.e. the sublist and
     *         the old list are independent from each other but use the same
     *         {@link GeoJSONCoordinate}s
     */
    public GeoJSONLineString subLineString(int fromIndex, int toIndex) {
        return GeoJSONLineString.create(coordinates.subList(fromIndex, toIndex));
    }

    @Override
    public boolean isEmpty() {
        return coordinates.isEmpty();
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(isEmpty() || coordinates.size() >= 2,
                "coordinates must either be emtpy or >= 2 but was of length " + coordinates.size());
    }

    @Override
    public String toWKT() {
        return getTypeName() + " " + WKTUtil.getCoordinateStringPointOrLineString(coordinates);
    }

    @Override
    public String toString() {
        return toWKT();
    }

}
