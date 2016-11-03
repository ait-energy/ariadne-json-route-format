package at.ac.ait.ariadne.routeformat.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A multi polygon, i.e. several polygons as defined in {@link GeoJSONPolygon}.
 * Note, that {@link #validate()} does not check if the interior rings actually
 * lie within the exterior ring.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.ALWAYS)
public class GeoJSONMultiPolygon implements GeoJSONGeometryObject {

	private List<List<List<Coordinate>>> coordinates = new ArrayList<>();

	// -- getters

	/**
	 * Coordinates of a multipolygon are an array of polygons, which are an
	 * array of LinearRing coordinate arrays (the first and the last coordinate
	 * must be the same, thereby closing the ring). The first element in the
	 * array represents the exterior ring. Any subsequent elements represent
	 * interior rings (or holes).
	 */
	@JsonProperty(required = true)
	public List<List<List<Coordinate>>> getCoordinates() {
		return coordinates;
	}

	// -- setters

	public GeoJSONMultiPolygon setCoordinates(List<List<List<Coordinate>>> coordinates) {
		this.coordinates = new ArrayList<>();
		for (List<List<Coordinate>> polygon : coordinates) {
			List<List<Coordinate>> polyCoordinates = new ArrayList<>();
			for (List<Coordinate> ring : polygon) {
				polyCoordinates.add(new ArrayList<>(ring));
			}
			this.coordinates.add(polyCoordinates);
		}
		return this;
	}

	// --

	public static GeoJSONMultiPolygon create(List<List<List<Coordinate>>> points) {
		return new GeoJSONMultiPolygon().setCoordinates(points);
	}

	@Override
	public boolean isEmpty() {
		return coordinates.isEmpty();
	}

	@Override
	public void validate() {
		for (List<List<Coordinate>> polygon : coordinates) {
			for (List<Coordinate> ring : polygon) {
				GeoJSONUtil.assertLinearRing(ring);
			}
		}
	}

	@Override
	public String toWKT() {
		return getTypeName() + " " + WKTUtil.getCoordinateStringMultiPolygon(coordinates);
	}

	@Override
	public String toString() {
		return toWKT();
	}

}