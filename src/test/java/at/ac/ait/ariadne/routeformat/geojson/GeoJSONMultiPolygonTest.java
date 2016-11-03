package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONMultiPolygonTest {

	@Test
	public void emptyWktTest() {
		GeoJSONMultiPolygon empty = GeoJSONMultiPolygon.create(Collections.emptyList());
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("MultiPolygon EMPTY", empty.toWKT());
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooShortForLinearRingTest() {
		GeoJSONMultiPolygon.create(Arrays.asList(Arrays.asList(Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"),
				Coordinate.createFromStrings("2.1", "2.2"), Coordinate.createFromStrings("3.1", "3.2")))));
	}

	@Test
	public void simpleMultiPolygonTest() {
		List<List<Coordinate>> polygonPoints = Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2")));
		GeoJSONMultiPolygon multiPolygon = GeoJSONMultiPolygon.create(Arrays.asList(polygonPoints, polygonPoints));
		Assert.assertEquals(
				"MultiPolygon (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)))",
				multiPolygon.toWKT());
	}

	@Test
	public void complexMultiPolygonTest() {
		List<List<Coordinate>> polygonPoints = Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2")),
				Arrays.asList(Coordinate.createFromStrings("1.11", "1.22"),
						Coordinate.createFromStrings("2.11", "2.22"), Coordinate.createFromStrings("3.11", "3.22"),
						Coordinate.createFromStrings("1.11", "1.22")));
		GeoJSONMultiPolygon multiPolygon = GeoJSONMultiPolygon.create(Arrays.asList(polygonPoints, polygonPoints));
		Assert.assertEquals(
				"MultiPolygon (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)))",
				multiPolygon.toWKT());
	}

}
