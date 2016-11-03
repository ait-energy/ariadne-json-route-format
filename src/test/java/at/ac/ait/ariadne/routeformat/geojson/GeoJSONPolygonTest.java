package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONPolygonTest {

	@Test
	public void emptyWktTest() {
		GeoJSONPolygon empty = GeoJSONPolygon.create(Collections.emptyList());
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("POLYGON EMPTY", empty.toWKT());
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooShortForLinearRingTest() {
		GeoJSONPolygon.create(Arrays.asList(Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"),
				Coordinate.createFromStrings("2.1", "2.2"), Coordinate.createFromStrings("3.1", "3.2"))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void unclosedLinearRingTest() {
		GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("4.1", "4.2"))));
	}

	@Test
	public void simplePolygonTest() {
		GeoJSONPolygon polygon = GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2"))));
		Assert.assertEquals("POLYGON ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2))", polygon.toWKT());
	}

	@Test
	public void complexPolygonTest() {
		GeoJSONPolygon polygon = GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2")),
				Arrays.asList(Coordinate.createFromStrings("1.11", "1.22"),
						Coordinate.createFromStrings("2.11", "2.22"), Coordinate.createFromStrings("3.11", "3.22"),
						Coordinate.createFromStrings("1.11", "1.22"))));
		Assert.assertEquals(
				"POLYGON ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22))",
				polygon.toWKT());
	}

}
