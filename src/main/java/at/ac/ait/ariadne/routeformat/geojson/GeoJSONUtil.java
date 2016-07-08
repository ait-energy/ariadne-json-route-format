package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.base.Preconditions;

public class GeoJSONUtil {

    /**
     * @param coordinates
     * @throws IllegalArgumentException
     *             if the coordinates are no valid LinearRing (at least 4
     *             coordinate pairs, where the first and the last coordinate are
     *             the same)
     */
    public static void assertLinearRing(List<List<BigDecimal>> coordinates) {
        for (List<BigDecimal> coordinate : coordinates)
            Preconditions.checkArgument(coordinate.size() >= 2,
                    "a coordinate must consist of at least two numbers. (was %s)", coordinate);
        int count = coordinates.size();
        Preconditions.checkArgument(count >= 4, "four coordinates required, got only %s", count);
        BigDecimal x1, x2, y1, y2;
        x1 = coordinates.get(0).get(0);
        x2 = coordinates.get(count - 1).get(0);
        y1 = coordinates.get(0).get(1);
        y2 = coordinates.get(count - 1).get(1);
        Preconditions.checkArgument(x1.equals(x2), "X value of first & last coordinate must be the same: %s != %s", x1,
                x2);
        Preconditions.checkArgument(y1.equals(y2), "Y value of first & last coordinate must be the same: %s != %s", y1,
                y2);
    }

}
