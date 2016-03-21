package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONPolygonTest {

	@Test
	public void emptyWktTest() {
		GeoJSONPolygon polygon = new GeoJSONPolygon();
		Assert.assertEquals("POLYGON EMPTY", polygon.toWKT());
	}

	@Test
	public void polygonToWktTest() {
		GeoJSONPolygon polygon = new GeoJSONPolygon(
				Arrays.asList(Arrays.asList(new CoordinatePoint("1.1", "1.2"), new CoordinatePoint("2.1", "2.2"))));
		Assert.assertEquals("POLYGON ((1.1 1.2, 2.1 2.2))", polygon.toWKT());

	}

	@Test
	public void multiPolygonToWktTest() {
		GeoJSONPolygon polygon = new GeoJSONPolygon(Arrays.asList(
				Arrays.asList(new CoordinatePoint("1.1", "1.2"), new CoordinatePoint("2.1", "2.2"),
						new CoordinatePoint("3.1", "3.2")),
				Arrays.asList(new CoordinatePoint("1.11", "1.22"), new CoordinatePoint("2.11", "2.22"),
						new CoordinatePoint("3.11", "3.22"))));
		Assert.assertEquals("POLYGON ((1.1 1.2, 2.1 2.2, 3.1 3.2), (1.11 1.22, 2.11 2.22, 3.11 3.22))",
				polygon.toWKT());

	}

}
