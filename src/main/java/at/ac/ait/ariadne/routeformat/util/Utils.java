package at.ac.ait.ariadne.routeformat.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
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
    
    public static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String LOCAL_TIME_FORMAT = "HH:mm:ss";
    public static final String PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    public static final String PARSE_FORMAT_FALLBACK = "yyyy-MM-dd'T'HH:mmXXX";

    /**
     * @return a Date with seconds accuracy
     */
    public static Date parseZonedDateTime(String zonedDateTimeString, String variableName) {
        if (zonedDateTimeString == null)
            throw new IllegalArgumentException(variableName + " must not be null");

        try {
            return truncateToSeconds(new SimpleDateFormat(PARSE_FORMAT).parse(zonedDateTimeString));
        } catch (ParseException e) {
            try {
                return truncateToSeconds(new SimpleDateFormat(PARSE_FORMAT_FALLBACK).parse(zonedDateTimeString));
            } catch (ParseException e1) {
                throw new IllegalArgumentException(variableName + " could not be parsed: " + e.getMessage());
            }
        }
    }
    
    public static Date addSeconds(Date date, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, seconds);
        return c.getTime();
    }
    
    public static Date subtractSeconds(Date date, int seconds) {
        return addSeconds(date, seconds * -1);
    }
    
    public static int getSecondsBetween(Date a, Date b) {
        long millisBetween = b.getTime() - a.getTime();
        return (int) (millisBetween / 1000);
    }
    
    public static Date truncateToSeconds(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    public static String getAsZonedDateTimeString(Date time) {
        return new SimpleDateFormat(PARSE_FORMAT).format(time);
    }
    
    public static String getShortStringDateTime(Date time) {
        return new SimpleDateFormat(LOCAL_DATE_TIME_FORMAT).format(time);
    }

    public static String getShortStringDate(Date time) {
        return new SimpleDateFormat(LOCAL_DATE_FORMAT).format(time);
    }

    public static String getShortStringTime(Date time) {
        return new SimpleDateFormat(LOCAL_TIME_FORMAT).format(time);
    }

    /**
     * @return <code>true</code> if 'between' is really between (or equal) to
     *         start and end
     */
    public static boolean isBetween(Date start, Optional<Date> between, Date end) {
        if (between.isPresent()) {
            long startTime = start.getTime();
            long betweenTime = between.get().getTime();
            long endTime = end.getTime();
            if (startTime > betweenTime || endTime < betweenTime)
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
            return Optional.absent();

        List<GeoJSONCoordinate> outerRing = new ArrayList<>();
        outerRing.add(GeoJSONCoordinate.create(minX, minY));
        outerRing.add(GeoJSONCoordinate.create(minX, maxY));
        outerRing.add(GeoJSONCoordinate.create(maxX, maxY));
        outerRing.add(GeoJSONCoordinate.create(maxX, minY));
        outerRing.add(GeoJSONCoordinate.create(minX, minY));
        List<List<GeoJSONCoordinate>> emptyInnerRing = new ArrayList<>();
        return Optional.of(GeoJSONFeature.createPolygonFeatureFromRings(outerRing, emptyInnerRing));
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
