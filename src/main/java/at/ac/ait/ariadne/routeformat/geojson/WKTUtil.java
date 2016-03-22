package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;

class WKTUtil {

	/**
	 * @param coordinates
	 *            pairs of coordinates: X and Y (=longitude and latitude)
	 * @return either 'EMPTY' or '(x1 y1, x2 y2,.. )'
	 * 
	 */
	public static String getCoordinateStringPointOrLineString(List<List<BigDecimal>> coordinates) {
		DecimalFormat df = new DecimalFormat("#.#######");
		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);

		StringBuilder sb = new StringBuilder();
		if (coordinates.isEmpty())
			sb.append("EMPTY");
		else {
			sb.append("(");
			List<String> xyPairs = coordinates.stream().map(xy -> df.format(xy.get(0)) + " " + df.format(xy.get(1)))
					.collect(Collectors.toList());
			sb.append(Joiner.on(", ").join(xyPairs));
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * @param coordinates
	 *            list of pairs of coordinates: X and Y (=longitude and
	 *            latitude)
	 * @return either 'EMPTY' or '((x1 y1), (x2 y2,..),.. )'
	 * 
	 */
	public static String getCoordinateStringPolygon(List<List<List<BigDecimal>>> coordinates) {
		if (coordinates.isEmpty())
			return "EMPTY";

		StringBuilder sb = new StringBuilder("(");
		sb.append(Joiner.on(", ").join(
				coordinates.stream().map(c -> getCoordinateStringPointOrLineString(c)).collect(Collectors.toList())));
		sb.append(")");
		return sb.toString();
	}

}
