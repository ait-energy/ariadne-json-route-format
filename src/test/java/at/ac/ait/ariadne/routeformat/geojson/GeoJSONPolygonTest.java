package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.ac.ait.ariadne.routeformat.TestUtil;

public class GeoJSONPolygonTest {

	private static final String expectedJson = "{\"type\":\"Polygon\",\"coordinates\":[[[1.1,1.2],[2.1,2.2],[3.1,3.2],[1.1,1.2]]]}";
	private static GeoJSONPolygon polygon;

	@BeforeClass
	public static void setUp() {
		polygon = GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2"))));
		polygon.validate();
	}

	@Test
	public void emptyWktTest() {
		GeoJSONPolygon empty = GeoJSONPolygon.create(Collections.emptyList());
		empty.validate();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("Polygon EMPTY", empty.toWKT());
	}

	@Test
	public void toJsonTest() throws JsonProcessingException {
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(polygon));
	}

	@Test
	public void fromJsonTest() throws IOException {
		GeoJSONPolygon parsedPolygon = TestUtil.MAPPER.readValue(expectedJson, GeoJSONPolygon.class);
		parsedPolygon.validate();
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedPolygon));

		GeoJSONGeometryObject parsedObject = TestUtil.MAPPER.readValue(expectedJson, GeoJSONGeometryObject.class);
		parsedObject.validate();
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedObject));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooShortForLinearRingTest() {
		GeoJSONPolygon
				.create(Arrays.asList(Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"),
						Coordinate.createFromStrings("2.1", "2.2"), Coordinate.createFromStrings("3.1", "3.2"))))
				.validate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void unclosedLinearRingTest() {
		GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("4.1", "4.2"))))
				.validate();
	}

	@Test
	public void simplePolygonTest() {
		Assert.assertEquals("Polygon ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2))", polygon.toWKT());
	}

	@Test
	public void complexPolygonTest() {
		GeoJSONPolygon polygon = GeoJSONPolygon.create(Arrays.asList(
				Arrays.asList(Coordinate.createFromStrings("1.1", "1.2"), Coordinate.createFromStrings("2.1", "2.2"),
						Coordinate.createFromStrings("3.1", "3.2"), Coordinate.createFromStrings("1.1", "1.2")),
				Arrays.asList(Coordinate.createFromStrings("1.11", "1.22"),
						Coordinate.createFromStrings("2.11", "2.22"), Coordinate.createFromStrings("3.11", "3.22"),
						Coordinate.createFromStrings("1.11", "1.22"))));
		polygon.validate();
		Assert.assertEquals(
				"Polygon ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22))",
				polygon.toWKT());
	}

}
