package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.util.Collections;

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
	private GeoJSONPoint p;

	@BeforeClass
	public static void setUp() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
	}

	@Before
	public void createPoints() {
		p = GeoJSONPoint.create(Coordinate.createFromStrings("1.234", "2.3456789"));
		p.validate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalPointTest() {
		Coordinate.create(Collections.emptyList());
	}

	@Test
	public void point3D() {
		p = GeoJSONPoint.create(Coordinate.createFromStrings("1.234", "2.3456789", "100"));
		p.validate();
	}

	@Test
	public void toJsonTest() throws JsonProcessingException {
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(p));
	}

	@Test
	public void fromJsonTest() throws IOException {
		GeoJSONPoint parsedPoint = mapper.readValue(expectedJson, GeoJSONPoint.class);
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(parsedPoint));

		GeoJSONGeometryObject parsedObject = mapper.readValue(expectedJson, GeoJSONGeometryObject.class);
		Assert.assertEquals(expectedJson, mapper.writeValueAsString(parsedObject));
	}

	@Test
	public void wktTest() {
		Assert.assertEquals("Point (1.234 2.3456789)", p.toWKT());
	}

	@Test
	public void emptyWktTest() {
		GeoJSONPoint empty = new GeoJSONPoint();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("Point EMPTY", empty.toWKT());
	}

}
