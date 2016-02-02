package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;

import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Location;

public class TestUtil {

	public static final String departureTime = "2007-12-03T10:15:30+01:00";
	public static final String arrivalTime = "2007-12-03T10:16:30+01:00";
	public static final Location from = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16, 48))).build();
	public static final Location to = Location.builder()
			.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.01, 48.01))).build();
	public static final ModeOfTransport modeOfTransport = ModeOfTransport.builder()
			.withGeneralizedType(GeneralizedModeOfTransportType.BICYCLE).build();
	public static final GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(Arrays
			.asList(CoordinatePoint.fromGeoJSONPointFeature(from.getCoordinate()),
					CoordinatePoint.fromGeoJSONPointFeature(to.getCoordinate())));

	public static RouteSegment buildRouteSegment(String departureTime, String arrivalTime,
			IntermediateStop... intermediateStops) {
		return RouteSegment.builder().withNr(1).withFrom(from).withTo(to).withModeOfTransport(modeOfTransport)
				.withDepartureTime(departureTime).withArrivalTime(arrivalTime).withDurationSeconds(60)
				.withLengthMeters(100).withIntermediateStops(Arrays.asList(intermediateStops))
				.withGeometryGeoJson(geometryGeoJson).build();
	}

}
