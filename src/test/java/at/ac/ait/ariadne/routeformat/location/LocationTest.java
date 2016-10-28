package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;

public class LocationTest {

	private static CoordinatePoint coordinate = new CoordinatePoint("16.40073", "48.25625");

	@Test
	public void simpleLocationTest() {
		Location<?> l = Location.createMinimal(coordinate);
		l.validate();
		Assert.assertEquals(coordinate.x, l.getCoordinate().geometry.coordinates.get(0));
		Assert.assertEquals(coordinate.y, l.getCoordinate().geometry.coordinates.get(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void simpleLocationValidationTest() {
		Location<?> l = new Location<>();
		l.validate();
	}

	@Test
	public void poiTest() {
		String museumName = "Naturhistorisches Museum";
		PointOfInterest poi = PointOfInterest.createMinimal(coordinate).setPoiType("museum").setName(museumName);
		poi.validate();
		Assert.assertEquals(museumName, poi.getName().get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void poiLocationValidationTest() {
		PointOfInterest poi = new PointOfInterest().setPoiType("museum");
		poi.validate();
	}

}
