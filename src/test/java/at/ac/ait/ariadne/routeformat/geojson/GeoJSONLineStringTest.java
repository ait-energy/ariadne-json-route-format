package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.ac.ait.ariadne.routeformat.TestUtil;

public class GeoJSONLineStringTest {

    private static final String expectedJson = "{\"type\":\"LineString\",\"coordinates\":[[1.111,1.2],[2.1,2.2],[3.1,3.2],[4.1,4.2,4.3]]}";
    private static GeoJSONLineString lineString;

    @BeforeClass
    public static void setUp() {
        List<GeoJSONCoordinate> points = Arrays.asList(GeoJSONCoordinate.create("1.111", "1.2"),
                GeoJSONCoordinate.create("2.1", "2.2"), GeoJSONCoordinate.create("3.1", "3.2"),
                GeoJSONCoordinate.create("4.1", "4.2", "4.3"));
        lineString = GeoJSONLineString.create(points);
        lineString.validate();
    }

    @Test
    public void toJsonTest() throws JsonProcessingException {
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(lineString));
    }

    @Test
    public void fromJsonTest() throws IOException {
        GeoJSONLineString parsedLineString = TestUtil.MAPPER.readValue(expectedJson, GeoJSONLineString.class);
        parsedLineString.validate();
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedLineString));

        GeoJSONGeometryObject parsedObject = TestUtil.MAPPER.readValue(expectedJson, GeoJSONGeometryObject.class);
        parsedObject.validate();
        Assert.assertEquals(expectedJson, TestUtil.MAPPER.writeValueAsString(parsedObject));
    }

    @Test
    public void subLineStringTest() {
        GeoJSONLineString subLineString = lineString.subLineString(0, 4);
        subLineString.validate();
        Assert.assertEquals(4, subLineString.getCoordinates().size());

        subLineString = lineString.subLineString(1, 2);
        Assert.assertEquals(1, subLineString.getCoordinates().size());
        Assert.assertEquals(2.1, subLineString.getCoordinates().get(0).getX().doubleValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onlyOneCoordinaateLineStringTest() {
        GeoJSONLineString subLineString = lineString.subLineString(0, 1);
        subLineString.validate();
    }

    @Test
    public void wktTest() {
        Assert.assertEquals("LineString (1.111 1.2, 2.1 2.2, 3.1 3.2, 4.1 4.2)", lineString.toWKT());
    }

    @Test
    public void emptyWktTest() {
        GeoJSONLineString empty = new GeoJSONLineString();
        empty.validate();
        Assert.assertTrue(empty.isEmpty());
        Assert.assertEquals("LineString EMPTY", empty.toWKT());
    }

}
