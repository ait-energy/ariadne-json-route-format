package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RouteSegmentTest {

	private static String departureTime = "2007-12-03T10:15:30+01:00";
	private static String arrivalTime = "2007-12-03T10:16:30+01:00";
	private static Location from = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16, 48))).build();
	private static Location to = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.01, 48.01))).build();
	private static ModeOfTransport modeOfTransport = ModeOfTransport.builder()
			.withGeneralizedType(GeneralizedModeOfTransportType.BICYCLE).build();
	private static GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(Arrays
			.asList(CoordinatePoint.fromGeoJSONPointFeature(from.getCoordinate()),
					CoordinatePoint.fromGeoJSONPointFeature(to.getCoordinate())));

	@Test
	public void enforceDepartureArrivalOrder() {
		try {
			buildRouteSegment(arrivalTime, departureTime);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		buildRouteSegment(departureTime, arrivalTime);
	}

	@Test
	public void enforceCorrectDurationTest() {
		try {
			buildRouteSegment(departureTime, "2007-12-03T10:16:31+01:00");
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		buildRouteSegment(departureTime, arrivalTime);
	}

	@Test
	public void enforceCorrectTimestampsForIntermediateStopsTest() {
		IntermediateStop start = IntermediateStop.builder().withStop(from)
				.withPlannedArrivalTime("2007-12-03T10:15:30+01:00").build();
		IntermediateStop middle = IntermediateStop.builder().withStop(from)
				.withPlannedArrivalTime("2007-12-03T10:16:00+01:00").build();
		IntermediateStop end = IntermediateStop.builder().withStop(from)
				.withPlannedArrivalTime("2007-12-03T10:16:30+01:00").build();
		IntermediateStop afterEnd = IntermediateStop.builder().withStop(from)
				.withPlannedArrivalTime("2007-12-03T10:17:00+01:00").build();

		try {
			buildRouteSegment(departureTime, arrivalTime, afterEnd);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		buildRouteSegment(departureTime, arrivalTime, start);
		buildRouteSegment(departureTime, arrivalTime, middle);
		buildRouteSegment(departureTime, arrivalTime, end);
	}

	private void buildRouteSegment(String departureTime, String arrivalTime, IntermediateStop... intermediateStops) {
		RouteSegment.builder().withNr(1).withFrom(from).withTo(to).withModeOfTransport(modeOfTransport)
				.withDepartureTime(departureTime).withArrivalTime(arrivalTime).withDurationSeconds(60)
				.withLengthMeters(100).withIntermediateStops(Arrays.asList(intermediateStops))
				.withGeometryGeoJson(geometryGeoJson).build();
	}
}
