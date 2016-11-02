package at.ac.ait.ariadne.routeformat.geojson;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GeoJSONMultiPolygonTest {

    @Test
    public void emptyWktTest() {
        GeoJSONMultiPolygon empty = new GeoJSONMultiPolygon();
        Assert.assertTrue(empty.isEmpty());
        Assert.assertEquals("MULTIPOLYGON EMPTY", empty.toWKT());
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooShortForLinearRingTest() {
        new GeoJSONMultiPolygon(Arrays.asList(Arrays.asList(Arrays.asList(new Coordinate("1.1", "1.2"),
                new Coordinate("2.1", "2.2"), new Coordinate("3.1", "3.2")))));
    }

    @Test
    public void simpleMultiPolygonTest() {
        List<List<Coordinate>> polygonPoints = Arrays
                .asList(Arrays.asList(new Coordinate("1.1", "1.2"), new Coordinate("2.1", "2.2"),
                        new Coordinate("3.1", "3.2"), new Coordinate("1.1", "1.2")));
        GeoJSONMultiPolygon multiPolygon = new GeoJSONMultiPolygon(Arrays.asList(polygonPoints, polygonPoints));
        Assert.assertEquals(
                "MULTIPOLYGON (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)))",
                multiPolygon.toWKT());
    }

    @Test
    public void complexMultiPolygonTest() {
        List<List<Coordinate>> polygonPoints = Arrays.asList(
                Arrays.asList(new Coordinate("1.1", "1.2"), new Coordinate("2.1", "2.2"),
                        new Coordinate("3.1", "3.2"), new Coordinate("1.1", "1.2")),
                Arrays.asList(new Coordinate("1.11", "1.22"), new Coordinate("2.11", "2.22"),
                        new Coordinate("3.11", "3.22"), new Coordinate("1.11", "1.22")));
        GeoJSONMultiPolygon multiPolygon = new GeoJSONMultiPolygon(Arrays.asList(polygonPoints, polygonPoints));
        Assert.assertEquals(
                "MULTIPOLYGON (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)))",
                multiPolygon.toWKT());
    }

}
