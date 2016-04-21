package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;

import org.junit.Assert;

import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * Helper snippets & methods for writing unit tests
 * 
 * @author AIT Austrian Institute of Technology GmbH
 *
 */
public class TestUtil {
	
	public static final String SERVICE_ID = "test_service";
	public static final String DEPARTURE_TIME = "2007-12-03T10:15:30+01:00";
	public static final String ARRIVAL_TIME = "2007-12-03T10:16:30+01:00";
	public static final Location FROM = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16, 48))).build();
	public static final Location TO = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.01, 48.01))).build();
	public static final ModeOfTransport MODE_OF_TRANSPORT = ModeOfTransport.builder()
			.withGeneralizedType(GeneralizedModeOfTransportType.BICYCLE).build();
	public static final GeoJSONFeature<GeoJSONLineString> GEOMETRY_GEOJSON = GeoJSONFeature.newLineStringFeature(Arrays
			.asList(CoordinatePoint.fromGeoJSONPointFeature(FROM.getCoordinate()),
					CoordinatePoint.fromGeoJSONPointFeature(TO.getCoordinate())));

	public static RouteSegment buildTestRouteSegment(String departureTime, String arrivalTime,
			IntermediateStop... intermediateStops) {
		return RouteSegment.builder().withNr(1).withFrom(FROM).withTo(TO).withModeOfTransport(MODE_OF_TRANSPORT)
				.withDepartureTime(departureTime).withArrivalTime(arrivalTime).withDurationSeconds(60)
				.withLengthMeters(100).withIntermediateStops(Arrays.asList(intermediateStops))
				.withGeometryGeoJson(GEOMETRY_GEOJSON).build(true);
	}

	public static void checkMot(RouteSegment segment, GeneralizedModeOfTransportType expectedGeneralMot,
			DetailedModeOfTransportType expectedDetailedMot) {
		ModeOfTransport modeOfTransport = segment.getModeOfTransport();
		Assert.assertEquals(expectedGeneralMot, modeOfTransport.getGeneralizedType());
		Assert.assertEquals(expectedDetailedMot, modeOfTransport.getDetailedType().get());
	}

	public static void checkLineName(RouteSegment segment, String expected) {
		ModeOfTransport modeOfTransport = segment.getModeOfTransport();
		Assert.assertEquals(expected, modeOfTransport.getService().get().getName());
	}

	/**
	 * @param expected
	 *            a String in the same format as from
	 *            {@link SprouteUtils#getShortStringDateTime(java.time.ZonedDateTime)}
	 */
	public static void checkDeparture(RouteSegment segment, String expected) {
		Assert.assertEquals(expected, SprouteUtils.getShortStringDateTime(segment.getDepartureTimeAsZonedDateTime()));
	}

	/**
	 * @param expected
	 *            a String in the same format as from
	 *            {@link SprouteUtils#getShortStringDateTime(java.time.ZonedDateTime)}
	 */
	public static void checkArrival(RouteSegment segment, String expected) {
		Assert.assertEquals(expected, SprouteUtils.getShortStringDateTime(segment.getArrivalTimeAsZonedDateTime()));
	}

	public static void checkDurationSeconds(RouteSegment segment, int expected) {
		Assert.assertEquals(expected, segment.getDurationSeconds());
	}

	public static void checkAlightingSeconds(RouteSegment segment, int expected) {
		if (expected == 0 && !segment.getAlightingSeconds().isPresent())
			return;
		Assert.assertEquals(expected, (int) segment.getAlightingSeconds().get());
	}

	public static void checkBoardingSeconds(RouteSegment segment, int expected) {
		if (expected == 0 && !segment.getBoardingSeconds().isPresent())
			return;
		Assert.assertEquals(expected, (int) segment.getBoardingSeconds().get());
	}

	public static void checkLengthMeters(RouteSegment segment, int expected) {
		Assert.assertEquals(expected, segment.getLengthMeters());
	}

}
