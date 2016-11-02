package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GeoJSONPointTest {

	private static final String expectedJson = "{\"type\":\"Point\",\"coordinates\":[1.234,2.3456789]}";
	private static ObjectMapper mapper;
	private Point p;

	@BeforeClass
	public static void setUp() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
	}

	@Before
	public void createPoint() {
		p = Point.create(new CoordinatePoint("1.234", "2.3456789"));
	}

	@Test
	public void toJsonTest() throws JsonProcessingException {
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(p));
	}

	@Test
	public void fromJsonTest() throws IOException {
		Point parsedPoint = mapper.readValue(expectedJson, Point.class);
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(parsedPoint));

		GeoJSONGeometryObject parsedObject = mapper.readValue(expectedJson, GeoJSONGeometryObject.class);
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(parsedObject));
	}

	@Test
	public void wktTest() {
		Assert.assertEquals("POINT (1.234 2.3456789)", p.toWKT());
	}

	@Test
	public void emptyWktTest() {
		Point empty = new Point();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("POINT EMPTY", empty.toWKT());
	}

}
