package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.ac.ait.ariadne.routeformat.TestUtil;

public class GeoJSONMultiPolygonTest {

    private static final String expectedJson = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.1,1.2],[2.1,2.2],[3.1,3.2],[1.1,1.2]]],[[[1.1,1.2],[2.1,2.2],[3.1,3.2],[1.1,1.2]]]]}";
    private static GeoJSONMultiPolygon multiPolygon;

    @BeforeClass
    public static void setUp() {
        List<List<GeoJSONCoordinate>> polygonPoints = Arrays
                .asList(Arrays.asList(GeoJSONCoordinate.create("1.1", "1.2"), GeoJSONCoordinate.create("2.1", "2.2"),
                        GeoJSONCoordinate.create("3.1", "3.2"), GeoJSONCoordinate.create("1.1", "1.2")));
        multiPolygon = GeoJSONMultiPolygon.create(Arrays.asList(polygonPoints, polygonPoints));
        multiPolygon.validate();
    }

    @Test
    public void emptyWktTest() {
        GeoJSONMultiPolygon empty = GeoJSONMultiPolygon.create(Collections.emptyList());
        empty.validate();
        Assert.assertTrue(empty.isEmpty());
        Assert.assertEquals("MultiPolygon EMPTY", empty.toWKT());
    }

    @Test
    public void toJsonTest() throws JsonProcessingException {
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(multiPolygon));
    }

    @Test
    public void fromJsonTest() throws IOException {
        GeoJSONMultiPolygon parsedMultiPolygon = TestUtil.MAPPER.readValue(expectedJson, GeoJSONMultiPolygon.class);
        parsedMultiPolygon.validate();
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedMultiPolygon));

        GeoJSONGeometryObject parsedObject = TestUtil.MAPPER.readValue(expectedJson, GeoJSONGeometryObject.class);
        parsedObject.validate();
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooShortForLinearRingTest() {
        GeoJSONMultiPolygon.create(Arrays.asList(Arrays.asList(Arrays.asList(GeoJSONCoordinate.create("1.1", "1.2"),
                GeoJSONCoordinate.create("2.1", "2.2"), GeoJSONCoordinate.create("3.1", "3.2"))))).validate();
    }

    @Test
    public void simpleMultiPolygonTest() {
        Assert.assertEquals(
                "MultiPolygon (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2)))",
                multiPolygon.toWKT());
    }

    @Test
    public void complexMultiPolygonTest() {
        List<List<GeoJSONCoordinate>> polygonPoints = Arrays.asList(
                Arrays.asList(GeoJSONCoordinate.create("1.1", "1.2"), GeoJSONCoordinate.create("2.1", "2.2"),
                        GeoJSONCoordinate.create("3.1", "3.2"), GeoJSONCoordinate.create("1.1", "1.2")),
                Arrays.asList(GeoJSONCoordinate.create("1.11", "1.22"), GeoJSONCoordinate.create("2.11", "2.22"),
                        GeoJSONCoordinate.create("3.11", "3.22"), GeoJSONCoordinate.create("1.11", "1.22")));
        GeoJSONMultiPolygon multiPolygon = GeoJSONMultiPolygon.create(Arrays.asList(polygonPoints, polygonPoints));
        multiPolygon.validate();
        Assert.assertEquals(
                "MultiPolygon (((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)), ((1.1 1.2, 2.1 2.2, 3.1 3.2, 1.1 1.2), (1.11 1.22, 2.11 2.22, 3.11 3.22, 1.11 1.22)))",
                multiPolygon.toWKT());
    }

}
