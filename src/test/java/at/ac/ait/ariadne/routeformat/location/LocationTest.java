package at.ac.ait.ariadne.routeformat.location;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

public class LocationTest {

    private static GeoJSONCoordinate coordinateA = GeoJSONCoordinate.create("16.40902", "48.25277");
    private static GeoJSONCoordinate coordinateB = GeoJSONCoordinate.create("16.40073", "48.25625");

    @Test
    public void simpleLocationTest() {
        Location<?> l = Location.createMinimal(coordinateA);
        l.validate();
        Assert.assertEquals(coordinateA.getX(), l.getCoordinate().getGeometry().getCoordinates().get().getX());
        Assert.assertEquals(coordinateA.getY(), l.getCoordinate().getGeometry().getCoordinates().get().getY());
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleLocationValidationTest() {
        Location<?> l = new Location<>();
        l.validate();
    }

    @Test
    public void poiTest() {
        String churchName = "Donaufelder Kirche";
        PointOfInterest poi = PointOfInterest.createMinimal(coordinateA).setPoiType("church").setName(churchName);
        poi.validate();
        Assert.assertEquals(churchName, poi.getName().get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void poiLocationValidationTest() {
        PointOfInterest poi = new PointOfInterest().setPoiType("museum");
        poi.validate();
    }

    @Test
    public void notEqualsForSameSubclassTest() {
        PointOfInterest church = PointOfInterest.createMinimal(coordinateA).setPoiType("church")
                .setName("Donaufelder Kirche");
        PointOfInterest churchWrongName = PointOfInterest.createMinimal(coordinateA).setPoiType("church")
                .setName("Eine Kirche");
        PointOfInterest churchWrongLocation = PointOfInterest.createMinimal(coordinateB).setPoiType("church")
                .setName("Donaufelder Kirche");

        Assert.assertEquals(church, church);
        Assert.assertNotEquals(church, churchWrongName);
        Assert.assertNotEquals(church, churchWrongLocation);
    }
    
    @Test
    public void notEqualsDifferentSubclassesTest() {
        Location<?> a = Location.createMinimal(coordinateA);
        PointOfInterest b = PointOfInterest.createMinimal(coordinateA);
        
        Assert.assertNotEquals(a, b);
    }

}
