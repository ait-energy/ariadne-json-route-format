package at.ac.ait.ariadne.routeformat.util;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RouteSegmentMergerTest {

	private static Location<?> adalbertStifterStrasse15, treustrasse92, treustrasse84, gaussplatz;

	@BeforeClass
	public static void initialise() {
		adalbertStifterStrasse15 = Location.createMinimal(Coordinate.createFromStrings("16.3655", "48.23752"))
				.setAddress(new Address().setStreetName("Adalbert-Stifter-Straße").setHouseNumber("15"));

		treustrasse92 = Location.createMinimal(Coordinate.createFromStrings("16.36329", "48.234077"))
				.setAddress(new Address().setStreetName("Treustraße").setHouseNumber("92").setPostCode("1200"));

		treustrasse84 = Location.createMinimal(Coordinate.createFromStrings("16.36369", "48.23348"))
				.setAddress(new Address().setStreetName("Treustraße").setHouseNumber("84").setPostCode("1200"));

		gaussplatz = Location.createMinimal(Coordinate.createFromStrings("16.369045", "48.2267"))
				.setAddress(new Address().setStreetName("Gaußplatz"));
	}

	@Test
	public void testMergingSegmentsWithSameMot() {
		List<List<RouteSegment>> listOfSegmentList = Arrays.asList(
				Arrays.asList(getFirstSegment(ModeOfTransport.STANDARD_FOOT)),
				Arrays.asList(getSecondSegment(ModeOfTransport.STANDARD_FOOT)));
		RouteSegmentMerger merger = new RouteSegmentMerger(listOfSegmentList);

		merger.setMergeSegmentsWithSameMot(false);
		Assert.assertEquals(2, merger.createMergedSegments().size());

		merger.setMergeSegmentsWithSameMot(true);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();
		Assert.assertEquals(1, mergedSegments.size());
		RouteSegment merged = mergedSegments.get(0);
		Assert.assertEquals(1, merged.getNr());
		Assert.assertEquals("2016-01-01T15:34:10", Utils.getShortStringDateTime(merged.getStartTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:45:00", Utils.getShortStringDateTime(merged.getEndTimeAsZonedDateTime()));
		Assert.assertEquals(10 * 60 + 50, merged.getDurationSeconds());
		Assert.assertEquals("sum of boarding of all segments + waiting inbetween", 120 + 124,
				(int) merged.getBoardingSeconds().get());
		Assert.assertEquals("sum of alighting of all segments", 120, (int) merged.getAlightingSeconds().get());
		Assert.assertEquals(597 + 941, merged.getDistanceMeters());
	}

	@Test
	public void testAdditionalAlightingSeconds() {
		List<List<RouteSegment>> listOfSegmentList = Arrays.asList(
				Arrays.asList(getFirstSegment(ModeOfTransport.STANDARD_FOOT)),
				Arrays.asList(getSecondSegment(ModeOfTransport.STANDARD_FOOT)));
		RouteSegmentMerger merger = new RouteSegmentMerger(listOfSegmentList);

		merger.setAdditionalAlightingSecondsBetweenRoutes(Arrays.asList(60));
		merger.setMergeSegmentsWithSameMot(false);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();
		Assert.assertEquals(2, mergedSegments.size());

		// first segment must be prolonged
		RouteSegment first = mergedSegments.get(0);
		Assert.assertEquals(1, first.getNr());
		Assert.assertEquals("2016-01-01T15:34:10", Utils.getShortStringDateTime(first.getStartTimeAsZonedDateTime()));
		Assert.assertEquals("one additional minute added", "2016-01-01T15:38:56",
				Utils.getShortStringDateTime(first.getEndTimeAsZonedDateTime()));
		Assert.assertEquals(4 * 60 + 46, first.getDurationSeconds());
		Assert.assertEquals(60, (int) first.getBoardingSeconds().get());
		Assert.assertEquals("one additional minute added", 120, (int) first.getAlightingSeconds().get());

		// second segment must contain the rest of the waiting time
		// left after prolonging the first segment
		RouteSegment second = mergedSegments.get(1);
		Assert.assertEquals(2, second.getNr());
		Assert.assertEquals("2016-01-01T15:38:56", Utils.getShortStringDateTime(second.getStartTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:45:00", Utils.getShortStringDateTime(second.getEndTimeAsZonedDateTime()));
		Assert.assertEquals(64 + (180 + 60 * 2), second.getDurationSeconds());
		Assert.assertEquals(64 + 60, (int) second.getBoardingSeconds().get());
		Assert.assertEquals(60, (int) second.getAlightingSeconds().get());
	}

	@Test
	public void testMergingWithOverlap() {
		RouteSegment secondSegment = getSecondSegment(ModeOfTransport.STANDARD_BICYCLE);
		secondSegment = RouteSegment.createShallowCopy(secondSegment).shiftInTime(-5, ChronoUnit.MINUTES);

		List<List<RouteSegment>> listOfSegmentList = Arrays
				.asList(Arrays.asList(getFirstSegment(ModeOfTransport.STANDARD_FOOT)), Arrays.asList(secondSegment));
		RouteSegmentMerger merger = new RouteSegmentMerger(listOfSegmentList);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();

		RouteSegment first = mergedSegments.get(0);
		Assert.assertEquals("2016-01-01T15:34:10", Utils.getShortStringDateTime(first.getStartTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:37:56", Utils.getShortStringDateTime(first.getEndTimeAsZonedDateTime()));
		RouteSegment second = mergedSegments.get(1);
		Assert.assertEquals("there must be zero gap to the first segment", "2016-01-01T15:37:56",
				Utils.getShortStringDateTime(second.getStartTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:42:56", Utils.getShortStringDateTime(second.getEndTimeAsZonedDateTime()));
	}

	private RouteSegment getFirstSegment(ModeOfTransport mot) {
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.createLineStringFeature(
				adalbertStifterStrasse15, treustrasse92, Coordinate.createFromStrings("16.36515", "48.23729"),
				Coordinate.createFromStrings("16.3656", "48.23515"),
				Coordinate.createFromStrings("16.36288", "48.23509"));
		RouteSegment bicycleFromAdalbertStifterStrasseToTreugasse = new RouteSegment().setNr(1)
				.setFrom(adalbertStifterStrasse15).setTo(treustrasse92).setDistanceMeters(597)
				// 106 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike
				.setDurationSeconds(106 + 60 * 2).setBoardingSeconds(60 * 1).setAlightingSeconds(60 * 1)
				.setStartTime("2016-01-01T15:34:10+01:00").setEndTime("2016-01-01T15:37:56+01:00")
				.setModeOfTransport(mot).setGeometryGeoJson(geometryGeoJson);
		return bicycleFromAdalbertStifterStrasseToTreugasse;
	}

	private RouteSegment getSecondSegment(ModeOfTransport mot) {
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.createLineStringFeature(treustrasse84,
				gaussplatz, Coordinate.createFromStrings("16.3644", "48.2311"),
				Coordinate.createFromStrings("16.36638", "48.22886"));
		RouteSegment bicycleFromTreugasseToGaussplatz = new RouteSegment().setNr(2).setFrom(treustrasse84)
				.setTo(gaussplatz).setDistanceMeters(941)
				// 180 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike = 5 minutes
				.setDurationSeconds(180 + 60 * 2).setBoardingSeconds(60 * 1).setAlightingSeconds(60 * 1)
				.setStartTime("2016-01-01T15:40:00+01:00").setEndTime("2016-01-01T15:45:00+01:00")
				.setModeOfTransport(mot).setGeometryGeoJson(geometryGeoJson);
		return bicycleFromTreugasseToGaussplatz;
	}
}
