package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RouteTest {

	@Test
	public void minimalRoute() {
		Route pointRoute = Route.createFromLocation(TestUtil.FROM, ZonedDateTime.parse(TestUtil.START_TIME));
		pointRoute.validate(true);
	}

	@Test
	public void enforceStartEndOrder() {
		LinkedList<RouteSegment> segments = new LinkedList<>();
		segments.add(TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME));
		segments.forEach(s -> s.validate(true));

		Route route = Route.createFromSegments(segments);
		route.validate();

		try {
			route.setEndTime(TestUtil.START_TIME).setStartTime(TestUtil.END_TIME);
			route.validate(true);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void boundingBoxExtractionTest() {
		Location<?> giefinggasseAit = Location.createMinimal(GeoJSONCoordinate.createFromStrings("16.4265", "48.2686"));
		Location<?> heinrichVonBuolGasseBicycleParking = Location
				.createMinimal(GeoJSONCoordinate.createFromStrings("16.4279", "48.2668"));
		Location<?> floridsdorf = Location.createMinimal(GeoJSONCoordinate.createFromStrings("16.4007", "48.2562"));
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.createLineStringFeature(giefinggasseAit,
				heinrichVonBuolGasseBicycleParking, GeoJSONCoordinate.createFromStrings("16.4263", "48.2682"),
				GeoJSONCoordinate.createFromStrings("16.42824", "48.26719"));
		RouteSegment walkToHeinrichVonBuolGasse = new RouteSegment().setNr(1).setFrom(giefinggasseAit)
				.setTo(heinrichVonBuolGasseBicycleParking).setDistanceMeters(200).setDurationSeconds(60)
				.setStartTime("2016-01-01T15:00:00+01:00").setEndTime("2016-01-01T15:01:00+01:00")
				.setModeOfTransport(ModeOfTransport.STANDARD_FOOT).setGeometryGeoJson(geometryGeoJson);
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(heinrichVonBuolGasseBicycleParking, floridsdorf,
				GeoJSONCoordinate.createFromStrings("16.42354", "48.26306"),
				GeoJSONCoordinate.createFromStrings("16.4236", "48.2621"), GeoJSONCoordinate.createFromStrings("16.4044", "48.2576"),
				GeoJSONCoordinate.createFromStrings("16.40305", "48.25621"),
				GeoJSONCoordinate.createFromStrings("16.40127", "48.25698"));
		RouteSegment cycleToFloridsdorf = new RouteSegment().setNr(2).setFrom(heinrichVonBuolGasseBicycleParking)
				.setTo(floridsdorf).setDistanceMeters(2500).setDurationSeconds(60 * 9)
				.setStartTime("2016-01-01T15:01:00+01:00").setEndTime("2016-01-01T15:10:00+01:00")
				.setModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).setGeometryGeoJson(geometryGeoJson);

		Route route = Route.createFromSegments(Arrays.asList(walkToHeinrichVonBuolGasse, cycleToFloridsdorf));

		Assert.assertTrue(route.getBoundingBox().isPresent());
		List<List<GeoJSONCoordinate>> allCoordinates = route.getBoundingBox().get().getGeometry().getCoordinates();
		Assert.assertEquals("only one outer ring must be present in the polygon", 1, allCoordinates.size());
		List<GeoJSONCoordinate> polygon = allCoordinates.get(0);
		Assert.assertEquals(5, polygon.size());

		Assert.assertEquals("left lower longitude", "16.4007", polygon.get(0).getX().toString());
		Assert.assertEquals("left lower latitude", "48.2562", polygon.get(0).getY().toString());

		Assert.assertEquals("left upper longitude", "16.4007", polygon.get(1).getX().toString());
		Assert.assertEquals("left upper latitude", "48.2686", polygon.get(1).getY().toString());

		Assert.assertEquals("right upper longitude", "16.42824", polygon.get(2).getX().toString());
		Assert.assertEquals("right upper latitude", "48.2686", polygon.get(2).getY().toString());

		Assert.assertEquals("right lower longitude", "16.42824", polygon.get(3).getX().toString());
		Assert.assertEquals("right lower latitude", "48.2562", polygon.get(3).getY().toString());

		Assert.assertEquals("left lower longitude again", "16.4007", polygon.get(4).getX().toString());
		Assert.assertEquals("left lower latitude again", "48.2562", polygon.get(4).getY().toString());
	}

}
