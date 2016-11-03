package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.ac.ait.ariadne.routeformat.TestUtil;

public class GeoJSONFeatureTest {

	private static final String expectedJson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.234,2.3456789]},\"properties\":{\"aProperty\":\"aValue\"}}";
	private GeoJSONFeature<GeoJSONPoint> p;

	@Before
	public void createPointFeature() {
		p = GeoJSONFeature.createPointFeature(GeoJSONPoint.create(GeoJSONCoordinate.create("1.234", "2.3456789")));
		p.getProperties().put("aProperty", "aValue");
		p.validate();
	}

	@Test
	public void toJsonTest() throws JsonProcessingException {
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(p));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void fromJsonTest() throws IOException {
		GeoJSONFeature<GeoJSONPoint> parsedPoint = TestUtil.MAPPER.readValue(expectedJson, GeoJSONFeature.class);
		parsedPoint.validate();
		Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedPoint));
	}

	@Test
	public void wktTest() {
		Assert.assertEquals("Point (1.234 2.3456789)", p.toWKT());
	}

}
