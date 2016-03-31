package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONFeature<T extends GeoJSONGeometryObject> {

	@JsonProperty(required = true)
	public final GeoJSONType type = GeoJSONType.Feature;

	@JsonProperty(required = false)
	public CRS crs = CRS.WGS84;

	@JsonProperty(required = true)
	public T geometry;

	/**
	 * Unrestricted possibility to store additional information, e.g. properties
	 * to be used in visualizations
	 */
	@JsonProperty(required = true)
	public Map<String, Object> properties = new HashMap<>();

	public String toWKT() {
		return geometry.toWKT();
	}

	@Override
	public String toString() {
		return "GeoJSONFeature [type=" + type + ", geometry=" + geometry + ", properties=" + properties + "]";
	}

	public static GeoJSONFeature<GeoJSONPoint> newPointFeature(CoordinatePoint point) {
		GeoJSONFeature<GeoJSONPoint> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONPoint(point);
		return feature;
	}

	public static GeoJSONFeature<GeoJSONPoint> newPointFeature(GeoJSONPoint point) {
		GeoJSONFeature<GeoJSONPoint> feature = new GeoJSONFeature<>();
		feature.geometry = point;
		return feature;
	}

	public static GeoJSONFeature<GeoJSONLineString> newLineStringFeature(List<CoordinatePoint> points) {
		GeoJSONFeature<GeoJSONLineString> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONLineString(points);
		return feature;
	}

	public static GeoJSONFeature<GeoJSONLineString> newLineStringFeature(Location from, Location to,
			CoordinatePoint... geometryInbetween) {
		List<CoordinatePoint> coordinatePoints = new ArrayList<>();
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPointFeature(from.getCoordinate()));
		coordinatePoints.addAll(Arrays.asList(geometryInbetween));
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPointFeature(to.getCoordinate()));
		return GeoJSONFeature.newLineStringFeature(coordinatePoints);
	}

	public static GeoJSONFeature<GeoJSONLineString> newLineStringFeature(GeoJSONLineString lineString) {
		GeoJSONFeature<GeoJSONLineString> feature = new GeoJSONFeature<>();
		feature.geometry = lineString;
		return feature;
	}

	/**
	 * @see GeoJSONPolygon#coordinates
	 */
	public static GeoJSONFeature<GeoJSONPolygon> newPolygonFeatureFromCoordinatePoints(
			List<List<CoordinatePoint>> points) {
		GeoJSONFeature<GeoJSONPolygon> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONPolygon(points);
		return feature;
	}

	/**
	 * @see GeoJSONPolygon#coordinates
	 * @param outerRing
	 *            mandatory
	 * @param innerRings
	 *            can be empty
	 */
	public static GeoJSONFeature<GeoJSONPolygon> newPolygonFeatureFromBigDecimals(List<List<BigDecimal>> outerRing,
			List<List<List<BigDecimal>>> innerRings) {
		GeoJSONFeature<GeoJSONPolygon> feature = new GeoJSONFeature<>();
		feature.geometry = new GeoJSONPolygon();
		List<List<List<BigDecimal>>> rings = new ArrayList<>();
		rings.add(outerRing);
		rings.addAll(innerRings);
		feature.geometry.coordinates = rings;
		return feature;
	}

}
