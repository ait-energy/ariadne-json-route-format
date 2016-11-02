package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONMultiPolygon implements GeoJSONGeometryObject {

    @JsonProperty(required = true)
    public final GeoJSONType type = GeoJSONType.MultiPolygon;

    /**
     * Coordinates of a multipolygon are an array of polygons, which are an
     * array of LinearRing coordinate arrays (the first and the last coordinate
     * must be the same, thereby closing the ring). The first element in the
     * array represents the exterior ring. Any subsequent elements represent
     * interior rings (or holes).
     * <p>
     * The inner list of {@link BigDecimal} is always a pair of coordinates: X
     * and Y (=longitude and latitude)
     */
    @JsonProperty(required = true)
    public List<List<List<List<BigDecimal>>>> coordinates = new ArrayList<>();

    public GeoJSONMultiPolygon() {
    }

    @Override
    public String toString() {
        return "GeoJSONPolygon [coordinates=" + coordinates + "]";
    }

    /**
     * Note: no checks if inner rings are actually within the outer ring are
     * performed
     * 
     * @throws IllegalArgumentException
     *             if invalid LinearRings are contained
     */
    public GeoJSONMultiPolygon(List<List<List<Coordinate>>> points) {
        for (List<List<Coordinate>> polygon : points) {
            List<List<List<BigDecimal>>> polyCoordinates = new ArrayList<>();
            for (List<Coordinate> linearRing : polygon) {
                List<List<BigDecimal>> ring = new ArrayList<>();
                for (Coordinate point : linearRing)
                    ring.add(point.asNewList());
                GeoJSONUtil.assertLinearRing(ring);
                polyCoordinates.add(ring);
            }
            coordinates.add(polyCoordinates);
        }
    }

    @Override
    public String toWKT() {
        return type.name().toUpperCase() + " " + WKTUtil.getCoordinateStringMultiPolygon(coordinates);
    }

    @Override
    public boolean isEmpty() {
        return coordinates.isEmpty();
    }

}