package at.ac.ait.ariadne.routeformat.geojson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * A coordinate independent of coordinate reference systems.
 * <p>
 * The GeoJSON specification (RFC 7946) says:
 * <p>
 * <i> A position is an array of numbers. There MUST be two or more elements.
 * The first two elements are longitude and latitude, or easting and northing,
 * precisely in that order and using decimal numbers. Altitude or elevation MAY
 * be included as an optional third element.</i>
 * <p>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(using = GeoJSONCoordinateDeserializer.class)
@JsonSerialize(using = GeoJSONCoordinateSerializer.class)
public class GeoJSONCoordinate implements Validatable {

    private BigDecimal x, y;
    private Optional<BigDecimal> z = Optional.empty();

    // -- getters

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public Optional<BigDecimal> getZ() {
        return z;
    }

    // -- setters

    public GeoJSONCoordinate setX(BigDecimal x) {
        this.x = x;
        return this;
    }

    public GeoJSONCoordinate setY(BigDecimal y) {
        this.y = y;
        return this;
    }

    public GeoJSONCoordinate setZ(BigDecimal z) {
        this.z = Optional.ofNullable(z);
        return this;
    }

    // --

    public static GeoJSONCoordinate create(double x, double y) {
        return GeoJSONCoordinate.create(String.format(Locale.US, "%.7f", x), String.format(Locale.US, "%.7f", y));
    }

    public static GeoJSONCoordinate create(double x, double y, double z) {
        return GeoJSONCoordinate.create(String.format(Locale.US, "%.7f", x), String.format(Locale.US, "%.7f", y),
                String.format(Locale.US, "%.7f", z));
    }

    public static GeoJSONCoordinate create(String x, String y) {
        return GeoJSONCoordinate.create(new BigDecimal(x), new BigDecimal(y));
    }

    public static GeoJSONCoordinate create(String x, String y, String z) {
        return GeoJSONCoordinate.create(new BigDecimal(x), new BigDecimal(y), new BigDecimal(z));
    }

    public static GeoJSONCoordinate create(List<BigDecimal> coordinates) {
        switch (coordinates.size()) {
        case 2:
            return GeoJSONCoordinate.create(coordinates.get(0), coordinates.get(1));
        case 3:
            return GeoJSONCoordinate.create(coordinates.get(0), coordinates.get(1), coordinates.get(2));
        default:
            throw new IllegalArgumentException("exactly 2 or 3 coordinates allowed, but were " + coordinates.size());
        }
    }

    public static GeoJSONCoordinate create(BigDecimal x, BigDecimal y) {
        return new GeoJSONCoordinate().setX(x).setY(y);
    }

    public static GeoJSONCoordinate create(BigDecimal x, BigDecimal y, BigDecimal z) {
        return new GeoJSONCoordinate().setX(x).setY(y).setZ(z);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(x != null, "x is mandatory but missing");
        Preconditions.checkArgument(y != null, "y is mandatory but missing");
    }

    @JsonIgnore
    public List<BigDecimal> asNewList() {
        List<BigDecimal> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        return list;
    }

    @Override
    public String toString() {
        return "Coordinate [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}
