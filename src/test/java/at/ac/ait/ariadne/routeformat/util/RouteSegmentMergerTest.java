package at.ac.ait.ariadne.routeformat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.SprouteUtils;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;

@RunWith(Parameterized.class)
public class RouteSegmentMergerTest {

	@Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays
				.asList(new Object[][] { { ModeOfTransport.STANDARD_FOOT }, { ModeOfTransport.STANDARD_BICYCLE }, });
	}

	private ModeOfTransport mot;

	public RouteSegmentMergerTest(ModeOfTransport mot) {
		this.mot = mot;
	}

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

		// prepare

		List<List<RouteSegment>> listOfSegmentList = new ArrayList<>();

		int segmentNr = 0;

		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(
				adalbertStifterStrasse15, treustrasse92, new CoordinatePoint(16.36515, 48.23729),
				new CoordinatePoint(16.3656, 48.23515), new CoordinatePoint(16.36288, 48.23509));
		RouteSegment bicycleFromAdalbertStifterStrasseToTreugasse = RouteSegment.builder().withNr(++segmentNr)
				.withFrom(adalbertStifterStrasse15).withTo(treustrasse92).withLengthMeters(597)
				// 106 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike
				.withDurationSeconds(106 + 60 * 2).withBoardingSeconds(60 * 1).withAlightingSeconds(60 * 1)
				.withDepartureTime("2016-01-01T15:34:10+01:00").withArrivalTime("2016-01-01T15:37:56+01:00")
				.withModeOfTransport(mot).withGeometryGeoJson(geometryGeoJson).build();
		listOfSegmentList.add(Arrays.asList(bicycleFromAdalbertStifterStrasseToTreugasse));

		geometryGeoJson = GeoJSONFeature.newLineStringFeature(treustrasse84, gaussplatz,
				new CoordinatePoint(16.3644, 48.2311), new CoordinatePoint(16.36638, 48.22886));
		RouteSegment bicycleFromTreugasseToGaussplatz = RouteSegment.builder().withNr(++segmentNr)
				.withFrom(treustrasse84).withTo(gaussplatz).withLengthMeters(941)
				// 180 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike = 5 minutes
				.withDurationSeconds(180 + 60 * 2).withBoardingSeconds(60 * 1).withAlightingSeconds(60 * 1)
				.withDepartureTime("2016-01-01T15:40:00+01:00").withArrivalTime("2016-01-01T15:45:00+01:00")
				.withModeOfTransport(mot).withGeometryGeoJson(geometryGeoJson).build();
		listOfSegmentList.add(Arrays.asList(bicycleFromTreugasseToGaussplatz));

		RouteSegmentMerger merger = new RouteSegmentMerger(listOfSegmentList);

		// test

		merger.setMergeSegmentsWithSameMot(false);
		Assert.assertEquals(2, merger.createMergedSegments().size());

		merger.setMergeSegmentsWithSameMot(true);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();
		Assert.assertEquals(1, mergedSegments.size());
		RouteSegment merged = mergedSegments.get(0);
		Assert.assertEquals(1, merged.getNr());
		Assert.assertEquals("2016-01-01T15:34:10",
				SprouteUtils.getShortString(merged.getDepartureTimeAsZonedDateTime()));
		Assert.assertEquals("2016-01-01T15:45:00", SprouteUtils.getShortString(merged.getArrivalTimeAsZonedDateTime()));
		Assert.assertEquals(10 * 60 + 50, merged.getDurationSeconds());
		Assert.assertEquals("sum of boarding of all segments + waiting inbetween", 120 + 124,
				(int) merged.getBoardingSeconds().get());
		Assert.assertEquals("sum of alighting of all segments", 120, (int) merged.getAlightingSeconds().get());
		Assert.assertEquals(597 + 941, merged.getLengthMeters());
	}
}
