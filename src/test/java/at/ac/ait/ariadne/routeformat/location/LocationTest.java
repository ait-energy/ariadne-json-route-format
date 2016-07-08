package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;

public class LocationTest {

    private static GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature
            .newPointFeature(new CoordinatePoint("16.40073", "48.25625"));

    @Test
    public void simpleLocationTest() {
        Location l = Location.builder().withCoordinate(coordinate).build();
        Assert.assertEquals(coordinate, l.getCoordinate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleLocationValidationTest() {
        Location.builder().withAdditionalInfo(Collections.emptyMap()).build();
    }

    @Test
    public void poiTest() {
        PointOfInterest.builder().withCoordinate(coordinate).withPoiType("museum").withName("Naturhistorisches Museum")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void poiLocationValidationTest() {
        PointOfInterest.builder().withPoiType("museum").withName("Naturhistorisches Museum").build();
    }

}
