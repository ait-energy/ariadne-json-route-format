package at.ac.ait.ariadne.routeformat.util;

import org.junit.Assert;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.SprouteUtils;

/**
 * Helper methods for writing unit tests
 * 
 * @author AIT Austrian Institute of Technology GmbH
 *
 */
public class TestUtil {

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
