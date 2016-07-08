package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONPolygonTest {

    @Test
    public void emptyWktTest() {
        GeoJSONPolygon empty = new GeoJSONPolygon();
        Assert.assertTrue(empty.isEmpty());
        Assert.assertEquals("POLYGON EMPTY", empty.toWKT());
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooShortForLinearRingTest() {
        new GeoJSONPolygon(Arrays.asList(Arrays.asList(new CoordinatePoint("1.1", "1.2"),
                new CoordinatePoint("2.1", "2.2"), new CoordinatePoint("3.1", "3.2"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unclosedLinearRingTest() {
        new GeoJSONPolygon(
                Arrays.asList(Arrays.asList(new CoordinatePoint("1.1", "1.2"), new CoordinatePoint("2.1", "2.2"),
                        new CoordinatePoint("3.1", "3.2"), new CoordinatePoint("4.1", "4.2"))));
    }

    @Test
    public void simplePolygonTest() {
        GeoJSONPolygon polygon = new GeoJSONPolygon(
                Arrays.asList(Arrays.asList(new CoordinatePoint("1.1", "1.2"), new CoordinatePoint("2.1", "2.2"),
                        new CoordinatePoint("3.1", "3.2"), new CoordinatePoint("1.1", "1.2"))));
        Assert.assertEquals("POLYGON ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2))", polygon.toWKT());
    }

    @Test
    public void complexPolygonTest() {
        GeoJSONPolygon polygon = new GeoJSONPolygon(Arrays.asList(
                Arrays.asList(new CoordinatePoint("1.1", "1.2"), new CoordinatePoint("2.1", "2.2"),
                        new CoordinatePoint("3.1", "3.2"), new CoordinatePoint("1.1", "1.2")),
                Arrays.asList(new CoordinatePoint("1.11", "1.22"), new CoordinatePoint("2.11", "2.22"),
                        new CoordinatePoint("3.11", "3.22"), new CoordinatePoint("1.11", "1.22"))));
        Assert.assertEquals(
                "POLYGON ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22))",
                polygon.toWKT());
    }

}
