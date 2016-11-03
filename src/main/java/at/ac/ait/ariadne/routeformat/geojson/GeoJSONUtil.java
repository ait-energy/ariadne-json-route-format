package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.Preconditions;

public class GeoJSONUtil {

	/**
	 * @return the geometry type of a GeoJSON* class, e.g. Point for
	 *         {@link GeoJSONPoint}
	 */
	public static String getTypeName(Class<?> clazz) {
		return clazz.getSimpleName().replaceAll("GeoJSON", "");
	}

	/**
	 * @param coordinates
	 * @throws IllegalArgumentException
	 *             if the coordinates are no valid LinearRing (at least 4
	 *             coordinate pairs, where the first and the last coordinate are
	 *             the same)
	 */
	public static void assertLinearRing(List<Coordinate> coordinates) {
		int count = coordinates.size();
		Preconditions.checkArgument(count >= 4, "four coordinates required, got only %s", count);
		BigDecimal x1, x2, y1, y2;
		x1 = coordinates.get(0).getX();
		x2 = coordinates.get(count - 1).getX();
		y1 = coordinates.get(0).getY();
		y2 = coordinates.get(count - 1).getY();
		Preconditions.checkArgument(x1.equals(x2), "X value of first & last coordinate must be the same: %s != %s", x1,
				x2);
		Preconditions.checkArgument(y1.equals(y2), "Y value of first & last coordinate must be the same: %s != %s", y1,
				y2);
	}

}
