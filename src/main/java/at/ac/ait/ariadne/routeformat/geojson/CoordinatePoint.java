package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A coordinate independent of the coordinate reference system.
 * <p>
 * Note for using WGS84: longitude=x, latitude=y
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class CoordinatePoint {

	public CoordinatePoint(double x, double y) {
		this.x = new BigDecimal(String.format(Locale.US, "%.7f", x));
		this.y = new BigDecimal(String.format(Locale.US, "%.7f", y));
	}

	public CoordinatePoint(BigDecimal x, BigDecimal y) {
		this.x = x;
		this.y = y;
	}

	public CoordinatePoint(String x, String y) {
		this.x = new BigDecimal(x);
		this.y = new BigDecimal(y);
	}

	public BigDecimal x;
	public BigDecimal y;

	public List<BigDecimal> asNewList() {
		List<BigDecimal> list = new ArrayList<>();
		list.add(x);
		list.add(y);
		return list;
	}

	public static CoordinatePoint fromGeoJSONPointFeature(GeoJSONFeature<Point> pointFeature) {
		return fromGeoJSONPoint(pointFeature.geometry);
	}

	public static CoordinatePoint fromGeoJSONPoint(Point point) {
		BigDecimal x = point.coordinates.get(0);
		BigDecimal y = point.coordinates.get(1);
		return new CoordinatePoint(x, y);
	}

	@Override
	public String toString() {
		return "CoordinatePoint [x=" + x + ", y=" + y + "]";
	}

}
