package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.ac.ait.ariadne.routeformat.TestUtil;

public class GeoJSONPointTest {

	private static final String expectedJson = "{\"type\":\"Point\",\"coordinates\":[1.234,2.3456789]}";
	private GeoJSONPoint p;

	@Before
	public void createPoint() {
		p = GeoJSONPoint.create(GeoJSONCoordinate.create("1.234", "2.3456789"));
		p.validate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalPointTest() {
		GeoJSONCoordinate.create(Collections.emptyList());
	}

	@Test
	public void point2D() {
		p.validate();

		Assert.assertEquals(new BigDecimal("1.234"), p.getCoordinates().get().getX());
		Assert.assertEquals(new BigDecimal("2.3456789"), p.getCoordinates().get().getY());
		Assert.assertFalse(p.getCoordinates().get().getZ().isPresent());
	}

	@Test
	public void point3D() {
		p = GeoJSONPoint.create(GeoJSONCoordinate.create("1.234", "2.3456789", "100"));
		p.validate();

		Assert.assertEquals(new BigDecimal("1.234"), p.getCoordinates().get().getX());
		Assert.assertEquals(new BigDecimal("2.3456789"), p.getCoordinates().get().getY());
		Assert.assertEquals(new BigDecimal("100"), p.getCoordinates().get().getZ().get());
	}

	@Test
	public void toJsonTest() throws JsonProcessingException {
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(p));
	}

	@Test
	public void fromJsonTest() throws IOException {
		GeoJSONPoint parsedPoint = TestUtil.MAPPER.readValue(expectedJson, GeoJSONPoint.class);
		parsedPoint.validate();
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedPoint));

		GeoJSONGeometryObject parsedObject = TestUtil.MAPPER.readValue(expectedJson, GeoJSONGeometryObject.class);
		parsedObject.validate();
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedObject));
	}

	@Test
	public void wktTest() {
		Assert.assertEquals("Point (1.234 2.3456789)", p.toWKT());
	}

	@Test
	public void emptyWktTest() {
		GeoJSONPoint empty = new GeoJSONPoint();
		empty.validate();
		Assert.assertTrue(empty.isEmpty());
		Assert.assertEquals("Point EMPTY", empty.toWKT());
	}

}
