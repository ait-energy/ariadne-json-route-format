package at.ac.ait.sproute.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class CoordinatePoint {
	
	public CoordinatePoint(double longitude, double latitude) {
		this.x = new BigDecimal(String.format("%.7f", longitude));
		this.y = new BigDecimal(String.format("%.7f", latitude));
	}
	
	public CoordinatePoint(BigDecimal longitude, BigDecimal latitude) {
		this.x = longitude;
		this.y = latitude;
	}

	public CoordinatePoint(String longitude, String latitude) {
		this.x = new BigDecimal(longitude);
		this.y = new BigDecimal(latitude);
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
