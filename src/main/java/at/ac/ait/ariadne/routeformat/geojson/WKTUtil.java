package at.ac.ait.ariadne.routeformat.geojson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

class WKTUtil {

    /**
     * @param coordinates
     *            list of coordinate pairs: X and Y (=longitude and latitude)
     * @return either 'EMPTY' or '(x1 y1, x2 y2,.. )'
     * 
     */
    public static String getCoordinateStringPointOrLineString(List<GeoJSONCoordinate> coordinates) {
        if (coordinates.isEmpty())
            return "EMPTY";

        StringBuilder sb = new StringBuilder();
        if (coordinates.isEmpty())
            sb.append("EMPTY");
        else {
            sb.append("(");
            List<String> xyzTuples = new ArrayList<>();
            for(GeoJSONCoordinate c : coordinates) {
                xyzTuples.add(getCoordinateString(c));
            }
            sb.append(Joiner.on(", ").join(xyzTuples));
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * @param coordinates
     *            list of list of coordinate pairs: X and Y (=longitude and
     *            latitude)
     * @return either 'EMPTY' or '((x1 y1), (x2 y2,..),.. )'
     * 
     */
    public static String getCoordinateStringPolygon(List<List<GeoJSONCoordinate>> coordinates) {
        if (coordinates.isEmpty())
            return "EMPTY";

        List<String> tokens = new ArrayList<>();
        for(List<GeoJSONCoordinate> c : coordinates) {
            tokens.add(getCoordinateStringPointOrLineString(c));
        }
        
        StringBuilder sb = new StringBuilder("(");
        sb.append(Joiner.on(", ").join(tokens));
        sb.append(")");
        return sb.toString();
    }

    /**
     * @param coordinates
     *            list of list of list of coordinate pairs: X and Y (=longitude
     *            and latitude)
     * @return either 'EMPTY' or '(((x1 y1), (x2 y2,..),.. ),..)'
     * 
     */
    public static String getCoordinateStringMultiPolygon(List<List<List<GeoJSONCoordinate>>> coordinates) {
        if (coordinates.isEmpty())
            return "EMPTY";

        List<String> tokens = new ArrayList<>();
        for(List<List<GeoJSONCoordinate>> c : coordinates) {
            tokens.add(getCoordinateStringPolygon(c));
        }
        
        StringBuilder sb = new StringBuilder("(");
        sb.append(Joiner.on(", ").join(tokens));
        sb.append(")");
        return sb.toString();
    }

    private static String getCoordinateString(GeoJSONCoordinate coordinate) {
        DecimalFormat df = new DecimalFormat("#.#######");
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(custom);

        List<String> subStrings = new ArrayList<>();
        subStrings.add(df.format(coordinate.getX()));
        subStrings.add(df.format(coordinate.getY()));
        // FIXME properly export altitude to WKT
        // if (coordinate.getZ().isPresent())
        // subStrings.add(df.format(coordinate.getZ().get()));
        return Joiner.on(' ').join(subStrings);
    }

}
