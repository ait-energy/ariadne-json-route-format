package at.ac.ait.ariadne.routeformat.geojson;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONPointTest {

	@Test
	public void wktTest() {
		GeoJSONPoint p = new GeoJSONPoint(new CoordinatePoint("1.234", "2.3456789"));
		Assert.assertEquals("POINT (1.234 2.3456789)", p.toWKT());
	}

	@Test
	public void emptyWktTest() {
		GeoJSONPoint empty = new GeoJSONPoint();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("POINT EMPTY", empty.toWKT());
	}

}
