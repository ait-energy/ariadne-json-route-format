package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GeoJSONLineStringTest {

	private static GeoJSONLineString lineString;

	@BeforeClass
	public static void setUp() {
		List<Coordinate> points = Arrays.asList(Coordinate.createFromStrings("1.111", "1.2"),
				Coordinate.createFromStrings("2.1", "2.2"), Coordinate.createFromStrings("3.1", "3.2"),
				Coordinate.createFromStrings("4.1", "4.2"));
		lineString = GeoJSONLineString.create(points);
		lineString.validate();
	}

	@Test
	public void subLineStringTest() {
		GeoJSONLineString subLineString = lineString.subLineString(0, 4);
		Assert.assertEquals(4, subLineString.getCoordinates().size());

		subLineString = lineString.subLineString(1, 2);
		Assert.assertEquals(1, subLineString.getCoordinates().size());
		Assert.assertEquals(2.1, subLineString.getCoordinates().get(0).getX().doubleValue(), 0.001);
	}

	@Test
	public void wktTest() {
		Assert.assertEquals("LineString (1.111 1.2, 2.1 2.2, 3.1 3.2, 4.1 4.2)", lineString.toWKT());
	}

	@Test
	public void emptyWktTest() {
		GeoJSONLineString empty = new GeoJSONLineString();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("LineString EMPTY", empty.toWKT());
	}

}
