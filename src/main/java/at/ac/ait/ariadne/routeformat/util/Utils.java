package at.ac.ait.ariadne.routeformat.util;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Utils {
    
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * @return a ZonedDateTime with seconds accuracy
     */
    public static ZonedDateTime parseDateTime(String dateTimeString, String variableName) {
        if (dateTimeString == null)
            throw new IllegalArgumentException(variableName + " must not be null");

        try {
            return ZonedDateTime.parse(dateTimeString).truncatedTo(ChronoUnit.SECONDS);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(variableName + " could not be parsed: " + e.getMessage());
        }
    }
    
    public static String getDateTimeString(ZonedDateTime time) {
        return FORMATTER.format(time);
    }

    public static String getShortStringDateTime(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String getShortStringDate(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String getShortStringTime(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    /**
     * @return <code>true</code> if 'between' is really between (or equal) to
     *         start and end
     */
    public static boolean isBetween(ZonedDateTime start, Optional<ZonedDateTime> between, ZonedDateTime end) {
        if (between.isPresent()) {
            if (start.isAfter(between.get()) || end.isBefore(between.get()))
                return false;
        }
        return true;
    }

    /**
     * @param modesOfTransport
     *            a comma-separated list of MOTs
     */
    public static Set<GeneralizedModeOfTransportType> parseModesOfTransport(String modesOfTransport,
            String variableName) {
        if (modesOfTransport == null)
            throw new IllegalArgumentException(modesOfTransport + " must not be null");

        Set<GeneralizedModeOfTransportType> motSet = new HashSet<>();
        for (String modeOfTransport : modesOfTransport.split(",")) {
            try {
                motSet.add(GeneralizedModeOfTransportType.valueOf(modeOfTransport.trim().toUpperCase()));
            } catch (IllegalArgumentException exc) {
                throw new IllegalArgumentException("'" + modeOfTransport + "' is not a valid mode of transport");
            }
        }
        return motSet;
    }

    public static Location<?> createLocation(double latitude, double longitude) {
        return Location.createMinimal(GeoJSONCoordinate.create(longitude, latitude));
    }

    public static String getJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(object);
    }

    public static Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingBoxFromGeometryGeoJson(
            List<RouteSegment> segments) {
        BigDecimal minX = null, maxX = null, minY = null, maxY = null;
        for (RouteSegment segment : segments) {
            if (segment.getGeometryGeoJson().isPresent()) {
                for (GeoJSONCoordinate xy : segment.getGeometryGeoJson().get().getGeometry().getCoordinates()) {
                    if (minX == null || minX.compareTo(xy.getX()) > 0)
                        minX = xy.getX();
                    if (maxX == null || maxX.compareTo(xy.getX()) < 0)
                        maxX = xy.getX();
                    if (minY == null || minY.compareTo(xy.getY()) > 0)
                        minY = xy.getY();
                    if (maxY == null || maxY.compareTo(xy.getY()) < 0)
                        maxY = xy.getY();
                }
            }
        }

        if (minX == null || maxX == null || minY == null || maxY == null)
            return Optional.empty();

        List<GeoJSONCoordinate> outerRing = new ArrayList<>();
        outerRing.add(GeoJSONCoordinate.create(minX, minY));
        outerRing.add(GeoJSONCoordinate.create(minX, maxY));
        outerRing.add(GeoJSONCoordinate.create(maxX, maxY));
        outerRing.add(GeoJSONCoordinate.create(maxX, minY));
        outerRing.add(GeoJSONCoordinate.create(minX, minY));
        return Optional.of(GeoJSONFeature.createPolygonFeatureFromRings(outerRing, Collections.emptyList()));
    }

    /**
     * @throws IllegalArgumentException
     *             if value is present and negative
     */
    public static void checkPositiveIntegerOrEmpty(Optional<Integer> value, String variableName) {
        if (value.isPresent()) {
            Preconditions.checkArgument(value.get() >= 0, "only positive numbers for %s are allowed, was %s",
                    variableName, value);
        }
    }

    public static ModeOfTransport getStandardModeOfTransport(GeneralizedModeOfTransportType type) {
        switch (type) {
        case BICYCLE:
            return ModeOfTransport.STANDARD_BICYCLE;
        case CAR:
            return ModeOfTransport.STANDARD_CAR;
        case FOOT:
            return ModeOfTransport.STANDARD_FOOT;
        case MOTORCYCLE:
            return ModeOfTransport.STANDARD_MOTORCYCLE;
        case PUBLIC_TRANSPORT:
            return ModeOfTransport.STANDARD_PUBLIC_TRANSPORT;
        default:
            throw new IllegalArgumentException("unsupported type " + type);
        }
    }

}
