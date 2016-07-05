package at.ac.ait.ariadne.routeformat.util;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RouteSegmentMergerTest {

	private static Location adalbertStifterStrasse15, treustrasse92, treustrasse84, gaussplatz;

	@BeforeClass
	public static void initialise() {
		adalbertStifterStrasse15 = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3655, 48.23752)))
				.withAddress(Address.builder().withStreetName("Adalbert-Stifter-Straße").withHouseNumber("15").build())
				.build();

		treustrasse92 = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.36329, 48.234077)))
				.withAddress(Address.builder().withStreetName("Treustraße").withHouseNumber("92").withPostCode("1200")
						.build())
				.build();

		treustrasse84 = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.36369, 48.23348)))
				.withAddress(Address.builder().withStreetName("Treustraße").withHouseNumber("84").withPostCode("1200")
						.build())
				.build();

		gaussplatz = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.369045, 48.2267)))
				.withAddress(Address.builder().withStreetName("Gaußplatz").build()).build();
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
		Assert.assertEquals("2016-01-01T15:34:10",
				Utils.getShortStringDateTime(merged.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:45:00", Utils.getShortStringDateTime(merged.getArrivalTimeAsZonedDateTime()));
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
		Assert.assertEquals("2016-01-01T15:34:10",
				Utils.getShortStringDateTime(first.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("one additional minute added", "2016-01-01T15:38:56",
				Utils.getShortStringDateTime(first.getArrivalTimeAsZonedDateTime()));
		Assert.assertEquals(4 * 60 + 46, first.getDurationSeconds());
		Assert.assertEquals(60, (int) first.getBoardingSeconds().get());
		Assert.assertEquals("one additional minute added", 120, (int) first.getAlightingSeconds().get());

		// second segment must contain the rest of the waiting time
		// left after prolonging the first segment
		RouteSegment second = mergedSegments.get(1);
		Assert.assertEquals(2, second.getNr());
		Assert.assertEquals("2016-01-01T15:38:56",
				Utils.getShortStringDateTime(second.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:45:00", Utils.getShortStringDateTime(second.getArrivalTimeAsZonedDateTime()));
		Assert.assertEquals(64 + (180 + 60 * 2), second.getDurationSeconds());
		Assert.assertEquals(64 + 60, (int) second.getBoardingSeconds().get());
		Assert.assertEquals(60, (int) second.getAlightingSeconds().get());
	}

	@Test
	public void testMergingWithOverlap() {
		RouteSegment secondSegment = getSecondSegment(ModeOfTransport.STANDARD_BICYCLE);
		secondSegment = RouteSegment.builder(secondSegment).shiftInTime(-5, ChronoUnit.MINUTES).build();

		List<List<RouteSegment>> listOfSegmentList = Arrays
				.asList(Arrays.asList(getFirstSegment(ModeOfTransport.STANDARD_FOOT)), Arrays.asList(secondSegment));
		RouteSegmentMerger merger = new RouteSegmentMerger(listOfSegmentList);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();

		RouteSegment first = mergedSegments.get(0);
		Assert.assertEquals("2016-01-01T15:34:10",
				Utils.getShortStringDateTime(first.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:37:56", Utils.getShortStringDateTime(first.getArrivalTimeAsZonedDateTime()));
		RouteSegment second = mergedSegments.get(1);
		Assert.assertEquals("there must be zero gap to the first segment", "2016-01-01T15:37:56",
				Utils.getShortStringDateTime(second.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:42:56", Utils.getShortStringDateTime(second.getArrivalTimeAsZonedDateTime()));
	}

	private RouteSegment getFirstSegment(ModeOfTransport mot) {
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(
				adalbertStifterStrasse15, treustrasse92, new CoordinatePoint(16.36515, 48.23729),
				new CoordinatePoint(16.3656, 48.23515), new CoordinatePoint(16.36288, 48.23509));
		RouteSegment bicycleFromAdalbertStifterStrasseToTreugasse = RouteSegment.builder().withNr(1)
				.withFrom(adalbertStifterStrasse15).withTo(treustrasse92).withDistanceMeters(597)
				// 106 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike
				.withDurationSeconds(106 + 60 * 2).withBoardingSeconds(60 * 1).withAlightingSeconds(60 * 1)
				.withDepartureTime("2016-01-01T15:34:10+01:00").withArrivalTime("2016-01-01T15:37:56+01:00")
				.withModeOfTransport(mot).withGeometryGeoJson(geometryGeoJson).build();
		return bicycleFromAdalbertStifterStrasseToTreugasse;
	}

	private RouteSegment getSecondSegment(ModeOfTransport mot) {
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(treustrasse84,
				gaussplatz, new CoordinatePoint(16.3644, 48.2311), new CoordinatePoint(16.36638, 48.22886));
		RouteSegment bicycleFromTreugasseToGaussplatz = RouteSegment.builder().withNr(2).withFrom(treustrasse84)
				.withTo(gaussplatz).withDistanceMeters(941)
				// 180 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike = 5 minutes
				.withDurationSeconds(180 + 60 * 2).withBoardingSeconds(60 * 1).withAlightingSeconds(60 * 1)
				.withDepartureTime("2016-01-01T15:40:00+01:00").withArrivalTime("2016-01-01T15:45:00+01:00")
				.withModeOfTransport(mot).withGeometryGeoJson(geometryGeoJson).build();
		return bicycleFromTreugasseToGaussplatz;
	}
}
