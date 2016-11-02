package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;

import org.junit.Assert;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * Helper snippets & methods for writing unit tests
 * 
 * @author AIT Austrian Institute of Technology GmbH
 *
 */
public class TestUtil {

	public static final String SERVICE_ID = "test_service";
	public static final String START_TIME = "2007-12-03T10:15:30+01:00";
	public static final String END_TIME = "2007-12-03T10:16:30+01:00";
	public static final Location<?> FROM = Location.createMinimal(new Coordinate(16, 48));
	public static final Location<?> TO = Location.createMinimal(new Coordinate(16.01, 48.01));
	public static final ModeOfTransport MODE_OF_TRANSPORT = ModeOfTransport.STANDARD_BICYCLE;
	public static final GeoJSONFeature<GeoJSONLineString> GEOMETRY_GEOJSON = GeoJSONFeature
			.newLineStringFeature(Arrays.asList(Coordinate.fromGeoJSONPointFeature(FROM.getCoordinate()),
					Coordinate.fromGeoJSONPointFeature(TO.getCoordinate())));

	public static RouteSegment createTestRouteSegment(String startTime, String endTime,
			IntermediateStop... intermediateStops) {
		return new RouteSegment().setNr(1).setFrom(FROM).setTo(TO).setModeOfTransport(MODE_OF_TRANSPORT)
				.setStartTime(startTime).setEndTime(endTime).setDurationSeconds(60).setDistanceMeters(100)
				.setIntermediateStops(Arrays.asList(intermediateStops)).setGeometryGeoJson(GEOMETRY_GEOJSON);
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
	 *            {@link Utils#getShortStringDateTime(java.time.ZonedDateTime)}
	 */
	public static void checkStart(RouteSegment segment, String expected) {
		Assert.assertEquals(expected, Utils.getShortStringDateTime(segment.getStartTimeAsZonedDateTime()));
	}

	/**
	 * @param expected
	 *            a String in the same format as from
	 *            {@link Utils#getShortStringDateTime(java.time.ZonedDateTime)}
	 */
	public static void checkEnd(RouteSegment segment, String expected) {
		Assert.assertEquals(expected, Utils.getShortStringDateTime(segment.getEndTimeAsZonedDateTime()));
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
		Assert.assertEquals(expected, segment.getDistanceMeters());
	}

}
