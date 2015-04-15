package at.ac.ait.sproute.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A coordinate independent of the coordinate reference system. (longitude=x,
 * latitude=y)
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class CoordinatePoint {

	public CoordinatePoint(double x, double y) {
		this.x = new BigDecimal(String.format("%.7f", x));
		this.y = new BigDecimal(String.format("%.7f", y));
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

}
