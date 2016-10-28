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
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Utils {

    /**
     * @return a ZonedDateTime with seconds accuracy
     */
    public static ZonedDateTime parseZonedDateTime(String zonedDateTimeString, String variableName) {
        if (zonedDateTimeString == null)
            throw new IllegalArgumentException(variableName + " must not be null");

        try {
            return ZonedDateTime.parse(zonedDateTimeString).truncatedTo(ChronoUnit.SECONDS);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(variableName + " could not be parsed: " + e.getMessage());
        }
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
        return Location.createMinimal(new CoordinatePoint(longitude, latitude));
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
                for (List<BigDecimal> xy : segment.getGeometryGeoJson().get().geometry.coordinates) {
                    if (minX == null || minX.compareTo(xy.get(0)) > 0)
                        minX = xy.get(0);
                    if (maxX == null || maxX.compareTo(xy.get(0)) < 0)
                        maxX = xy.get(0);
                    if (minY == null || minY.compareTo(xy.get(1)) > 0)
                        minY = xy.get(1);
                    if (maxY == null || maxY.compareTo(xy.get(1)) < 0)
                        maxY = xy.get(1);
                }
            }
        }

        if (minX == null || maxX == null || minY == null || maxY == null)
            return Optional.empty();

        List<List<BigDecimal>> outerRing = new ArrayList<>();
        outerRing.add(Lists.newArrayList(minX, minY));
        outerRing.add(Lists.newArrayList(minX, maxY));
        outerRing.add(Lists.newArrayList(maxX, maxY));
        outerRing.add(Lists.newArrayList(maxX, minY));
        outerRing.add(Lists.newArrayList(minX, minY));
        return Optional.of(GeoJSONFeature.newPolygonFeatureFromBigDecimals(outerRing, Collections.emptyList()));
    }

    /**
     * @throws IllegalArgumentException
     *             if value is present and negative
     */
    public static Optional<Integer> enforcePositiveInteger(Optional<Integer> value, String variableName) {
        if (value.isPresent()) {
            Preconditions.checkArgument(value.get() >= 0, "only positive numbers for %s are allowed, was %s",
                    variableName, value);
        }
        return value;
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
