package at.ac.ait.ariadne.routeformat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.Route.Builder;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RouteTest {

	@Test
	public void enforceDepartureArrivalOrder() {
		LinkedList<RouteSegment> segments = new LinkedList<>();
		segments.add(TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME));
		Builder routeBuilder = Route.builder().withSegmentsAndAutomaticallyInferredFields(segments);

		routeBuilder.build();

		try {
			routeBuilder.withArrivalTime(TestUtil.DEPARTURE_TIME).withDepartureTime(TestUtil.ARRIVAL_TIME).build(true);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void boundingBoxExtractionTest() {
		Location giefinggasseAit = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.4265", "48.2686"))).build();
		Location heinrichVonBuolGasseBicycleParking = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.4279", "48.2668"))).build();
		Location floridsdorf = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.4007", "48.2562"))).build();
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(giefinggasseAit,
				heinrichVonBuolGasseBicycleParking, new CoordinatePoint("16.4263", "48.2682"),
				new CoordinatePoint("16.42824", "48.26719"));
		RouteSegment walkToHeinrichVonBuolGasse = RouteSegment.builder().withNr(1).withFrom(giefinggasseAit)
				.withTo(heinrichVonBuolGasseBicycleParking).withLengthMeters(200).withDurationSeconds(60)
				.withDepartureTime("2016-01-01T15:00:00+01:00").withArrivalTime("2016-01-01T15:01:00+01:00")
				.withModeOfTransport(ModeOfTransport.STANDARD_FOOT).withGeometryGeoJson(geometryGeoJson).build();
		geometryGeoJson = GeoJSONFeature.newLineStringFeature(heinrichVonBuolGasseBicycleParking, floridsdorf,
				new CoordinatePoint("16.42354", "48.26306"), new CoordinatePoint("16.4236", "48.2621"),
				new CoordinatePoint("16.4044", "48.2576"), new CoordinatePoint("16.40305", "48.25621"),
				new CoordinatePoint("16.40127", "48.25698"));
		RouteSegment cycleToFloridsdorf = RouteSegment.builder().withNr(2).withFrom(heinrichVonBuolGasseBicycleParking)
				.withTo(floridsdorf).withLengthMeters(2500).withDurationSeconds(60 * 9)
				.withDepartureTime("2016-01-01T15:01:00+01:00").withArrivalTime("2016-01-01T15:10:00+01:00")
				.withModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).withGeometryGeoJson(geometryGeoJson).build();

		Route route = Route.builder().withSegmentsAndAutomaticallyInferredFields(
				Arrays.asList(walkToHeinrichVonBuolGasse, cycleToFloridsdorf)).build();

		Assert.assertTrue(route.getBoundingBox().isPresent());
		List<List<List<BigDecimal>>> allCoordinates = route.getBoundingBox().get().geometry.coordinates;
		Assert.assertEquals("only one outer ring must be present in the polygon", 1, allCoordinates.size());
		List<List<BigDecimal>> polygon = allCoordinates.get(0);
		Assert.assertEquals(5, polygon.size());

		Assert.assertEquals("left lower longitude", "16.4007", polygon.get(0).get(0).toString());
		Assert.assertEquals("left lower latitude", "48.2562", polygon.get(0).get(1).toString());

		Assert.assertEquals("left upper longitude", "16.4007", polygon.get(1).get(0).toString());
		Assert.assertEquals("left upper latitude", "48.2686", polygon.get(1).get(1).toString());

		Assert.assertEquals("right upper longitude", "16.42824", polygon.get(2).get(0).toString());
		Assert.assertEquals("right upper latitude", "48.2686", polygon.get(2).get(1).toString());

		Assert.assertEquals("right lower longitude", "16.42824", polygon.get(3).get(0).toString());
		Assert.assertEquals("right lower latitude", "48.2562", polygon.get(3).get(1).toString());

		Assert.assertEquals("left lower longitude again", "16.4007", polygon.get(4).get(0).toString());
		Assert.assertEquals("left lower latitude again", "48.2562", polygon.get(4).get(1).toString());
	}

}
