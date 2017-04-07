package at.ac.ait.ariadne.routeformat.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A polygon consisting of a mandatory exterior ring and optional interior rings
 * defining holes. Note, that {@link #validate()} does not check if the interior
 * rings actually lie within the exterior ring.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONPolygon implements GeoJSONGeometryObject {

    private List<List<GeoJSONCoordinate>> coordinates = new ArrayList<>();

    // -- getters

    /**
     * Coordinates of a polygon are an array of LinearRing coordinate arrays
     * (the first and the last coordinate must be the same, thereby closing the
     * ring). The first element in the array represents the exterior ring. Any
     * subsequent elements represent interior rings (or holes).
     */
    @JsonProperty(required = true)
    public List<List<GeoJSONCoordinate>> getCoordinates() {
        return coordinates;
    }

    // -- setters

    public GeoJSONPolygon setCoordinates(List<List<GeoJSONCoordinate>> coordinates) {
        this.coordinates = new ArrayList<>();
        for (List<GeoJSONCoordinate> ring : coordinates) {
            this.coordinates.add(new ArrayList<>(ring));
        }
        return this;
    }

    // --

    public static GeoJSONPolygon create(List<List<GeoJSONCoordinate>> points) {
        return new GeoJSONPolygon().setCoordinates(points);
    }

    @Override
    public boolean isEmpty() {
        return coordinates.isEmpty();
    }

    @Override
    public void validate() {
        for (List<GeoJSONCoordinate> ring : coordinates)
            GeoJSONUtil.assertLinearRing(ring);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeoJSONPolygon other = (GeoJSONPolygon) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        return true;
    }

    @Override
    public String toWKT() {
        return getTypeName() + " " + WKTUtil.getCoordinateStringPolygon(coordinates);
    }

    @Override
    public String toString() {
        return toWKT();
    }

}