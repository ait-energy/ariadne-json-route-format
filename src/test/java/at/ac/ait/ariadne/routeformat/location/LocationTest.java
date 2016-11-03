package at.ac.ait.ariadne.routeformat.location;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.geojson.Coordinate;

public class LocationTest {

	private static Coordinate coordinate = Coordinate.createFromStrings("16.40073", "48.25625");

	@Test
	public void simpleLocationTest() {
		Location<?> l = Location.createMinimal(coordinate);
		l.validate();
		Assert.assertEquals(coordinate.getX(), l.getCoordinate().getGeometry().getCoordinates().get().getX());
		Assert.assertEquals(coordinate.getY(), l.getCoordinate().getGeometry().getCoordinates().get().getY());
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
