package at.ac.ait.ariadne.routeformat.geojson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Validatable;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class GeoJSONFeature<T extends GeoJSONGeometryObject> implements Validatable {

    @JsonProperty(required = true)
    public final String type = GeoJSONUtil.getTypeName(this.getClass());
    private T geometry;
    private Map<String, Object> properties = new TreeMap<>();

    // -- getters

    @JsonProperty(required = true)
    public T getGeometry() {
        return geometry;
    }

    /**
     * Unrestricted possibility to store additional information, e.g. properties
     * to be used in visualizations
     */
    @JsonInclude(Include.ALWAYS)
    @JsonProperty(required = true)
    public Map<String, Object> getProperties() {
        return properties;
    }

    // -- setters

    public GeoJSONFeature<T> setGeometry(T geometry) {
        this.geometry = geometry;
        return this;
    }

    public GeoJSONFeature<T> setProperties(Map<String, Object> properties) {
        this.properties = new TreeMap<>(properties);
        return this;
    }

    // --

    public static GeoJSONFeature<GeoJSONPoint> createPointFeature(GeoJSONCoordinate point) {
        return createPointFeature(GeoJSONPoint.create(point));
    }

    public static GeoJSONFeature<GeoJSONPoint> createPointFeature(GeoJSONPoint point) {
        return new GeoJSONFeature<GeoJSONPoint>().setGeometry(point);
    }

    public static GeoJSONFeature<GeoJSONLineString> createLineStringFeature(Location<?> from, Location<?> to,
            GeoJSONCoordinate... geometryInbetween) {
        List<GeoJSONCoordinate> coordinatePoints = new ArrayList<>();
        coordinatePoints.add(from.getCoordinate().geometry.getCoordinates().get());
        coordinatePoints.addAll(Arrays.asList(geometryInbetween));
        coordinatePoints.add(to.getCoordinate().geometry.getCoordinates().get());
        return createLineStringFeature(coordinatePoints);
    }

    public static GeoJSONFeature<GeoJSONLineString> createLineStringFeature(List<GeoJSONCoordinate> points) {
        return createLineStringFeature(GeoJSONLineString.create(points));
    }

    public static GeoJSONFeature<GeoJSONLineString> createLineStringFeature(GeoJSONLineString lineString) {
        return new GeoJSONFeature<GeoJSONLineString>().setGeometry(lineString);
    }

    /**
     * @see GeoJSONPolygon#coordinates
     * @param outerRing
     *            mandatory
     * @param innerRings
     *            can be empty
     */
    public static GeoJSONFeature<GeoJSONPolygon> createPolygonFeatureFromRings(List<GeoJSONCoordinate> outerRing,
            List<List<GeoJSONCoordinate>> innerRings) {
        List<List<GeoJSONCoordinate>> rings = new ArrayList<>();
        rings.add(outerRing);
        rings.addAll(innerRings);
        return createPolygonFeatureFromCoordinatePoints(rings);
    }

    /**
     * @see GeoJSONPolygon#coordinates
     */
    public static GeoJSONFeature<GeoJSONPolygon> createPolygonFeatureFromCoordinatePoints(
            List<List<GeoJSONCoordinate>> points) {
        return createPolygonFeature(GeoJSONPolygon.create(points));
    }

    public static GeoJSONFeature<GeoJSONPolygon> createPolygonFeature(GeoJSONPolygon polygon) {
        return new GeoJSONFeature<GeoJSONPolygon>().setGeometry(polygon);
    }

    public static GeoJSONFeature<GeoJSONMultiPolygon> createMultiPolygonFeatureFromPolygons(
            List<GeoJSONFeature<GeoJSONPolygon>> polygons) {
        List<List<List<GeoJSONCoordinate>>> polygonGeometries = new ArrayList<>();
        for (GeoJSONFeature<GeoJSONPolygon> polygon : polygons) {
            polygonGeometries.add(polygon.geometry.getCoordinates());
        }
        return new GeoJSONFeature<GeoJSONMultiPolygon>().setGeometry(GeoJSONMultiPolygon.create(polygonGeometries));
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(geometry != null, "geometry is mandatory but missing");
        geometry.validate();
    }

    public String toWKT() {
        return geometry.toWKT();
    }

    @Override
    public String toString() {
        return "GeoJSONFeature [geometryWKT=" + toWKT() + ", properties=" + properties + "]";
    }

}
