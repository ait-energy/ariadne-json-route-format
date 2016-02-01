package at.ac.ait.ariadne.routeformat.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.Route;
import at.ac.ait.ariadne.routeformat.RouteFormatRoot;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.Service;
import at.ac.ait.ariadne.routeformat.ServiceProvider;
import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.Status;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.location.PointOfInterest;
import at.ac.ait.ariadne.routeformat.location.PublicTransportStop;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class is an example playground where we manually created an intermodal route to check if the route format
 * supports all required attributes / use cases.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class IntermodalRouteExample {

	private ServiceProvider wienerLinienProvider;
	private Service service28A, serviceU6;
	private Location giefinggasseAit, heinrichVonBuolGasseBusStop, floridsdorfBusStop, floridsdorfSubwayStop,
			handelskaiSubwayStop, handelskaiSubwayEntry, handelskaiCitybike;

	public IntermodalRouteExample() {
		initializeLocations();

		wienerLinienProvider = getWienerLinienServiceProvider();
		service28A = get28AService(wienerLinienProvider);
		serviceU6 = getU6Service(wienerLinienProvider);
	}

	private void initializeLocations() {
		Address giefinggasse = Address.builder().withCountry("Austria").withCity("Wien").withPostCode("1210")
				.withStreetName("Giefinggasse").withHouseNumber("2b").build();
		giefinggasseAit = PointOfInterest.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.4265263", "48.2686617")))
				.withAddress(giefinggasse).withName("AIT").withPoiType("company").build();

		heinrichVonBuolGasseBusStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.42791", "48.26680")))
				.withName("Heinrich-von-Buol-Gasse/Siemensstraße").build();

		floridsdorfBusStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.40073", "48.25625")))
				.withName("Floridsdorf Bus").withRelatedLines(Arrays.asList("29A")).build(); // TODO bahnsteig?

		floridsdorfSubwayStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.40050", "48.25618")))
				.withName("Floridsdorf U-Bahn").build();

		handelskaiSubwayStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.38541, 48.24173)))
				.withName("Handelskai U-Bahn (Bahnsteig 2)").build();
		
//		handelskaiSubwayEntry = PointOfInterest.builder()
//				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3848877, 48.2416471)))
//				.withName("Handelskai Eingang").build();
//		
//		handelskaiCitybike = PointOfInterest.builder()
//				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3847976, 48.2420356)))
//				.withName("Millennium Tower (2005)").build(); // TODO capacity 35, current bikes 10, current boxes 25

	}

	private ServiceProvider getWienerLinienServiceProvider() {
		return ServiceProvider
				.builder()
				.withName("Wiener Linien")
				.withWebsite("http://www.wienerlinien.at")
				.withAdditionalInfo(
						ImmutableMap.of("email", "post@wienerlinien.at", "email_ticketshop",
								"ticketshop@wienerlinien.at")).build();
	}

	private Service get28AService(ServiceProvider wienerLinienProvider) {
		return Service.builder().withName("28A").withDirection("Floridsdorf").withProvider(wienerLinienProvider)
				.build();
	}

	private Service getU6Service(ServiceProvider wienerLinienProvider) {
		return Service.builder().withName("U6").withDirection("Siebenhirten").withProvider(wienerLinienProvider)
				.build();
	}

	public RouteFormatRoot getRouteFormatRoot() throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> additionalInfoRouteRequest = new HashMap<>();
		additionalInfoRouteRequest.put("ait:additionalTestBigDecimal", new BigDecimal("12.34567"));
		additionalInfoRouteRequest.put("ait:additionalTestObject", wienerLinienProvider);
		additionalInfoRouteRequest.put("ait:additionalTestList", Lists.newArrayList(1, 2, 3, 4, 5, 6, 7));
		additionalInfoRouteRequest.put("ait:additionalTestString", "hello this is a String");

		Map<GeneralizedModeOfTransportType, List<Location>> privateVehicleLocations = new HashMap<>();
		// privateVehicleLocations.put(GeneralizedModeOfTransportType.BICYCLE,
		// Arrays.asList(richardneutragasseLocation));
		// privateVehicleLocations.put(GeneralizedModeOfTransportType.CAR,
		// Arrays.asList(richardneutragasseLocation, giefinggasseLocation));

		RoutingRequest request = RoutingRequest.builder().withServiceId("ariadne_webservice_vienna")
				.withFrom(giefinggasseAit).withTo(floridsdorfSubwayStop)
				.withModesOfTransport(Sets.newHashSet(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT))
				.withOptimizedFor("traveltime").withAdditionalInfo(additionalInfoRouteRequest)
				.withPrivateVehicleLocations(privateVehicleLocations).build();

		Route route = getRoute();

		return RouteFormatRoot.builder().withRouteFormatVersion("0.11-SNAPSHOT").withRequestId("999")
				.withProcessedTimeNow().withStatus(Status.OK).withDebugMessage("Route calculated in 0.002 seconds")
				.withCoordinateReferenceSystem("EPSG:4326").withRequest(request).withRoutes(Arrays.asList(route))
				.build();
	}

	private Route getRoute() {
		LinkedList<RouteSegment> segments = getRouteSegments();

		int lengthMeters = 0;
		int durationSeconds = 0;
		for (RouteSegment segment : segments) {
			lengthMeters += segment.getLengthMeters();
			durationSeconds += segment.getDurationSeconds();
		}

		return Route.builder().withFrom(segments.getFirst().getFrom()).withTo(segments.getLast().getTo())
				.withDepartureTime(segments.getFirst().getDepartureTimeAsZonedDateTime().orElse(null))
				.withArrivalTime(segments.getLast().getArrivalTimeAsZonedDateTime().orElse(null))
				.withLengthMeters(lengthMeters).withDurationSeconds(durationSeconds).withSegments(segments).build();
	}

	private LinkedList<RouteSegment> getRouteSegments() {
		LinkedList<RouteSegment> segments = new LinkedList<>();
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson;

		int segmentNr = 0;

		// ### walk to bus ###
		geometryGeoJson = getGeoJSONLineStringFeature(giefinggasseAit, heinrichVonBuolGasseBusStop,
				new CoordinatePoint(16.4263, 48.2682), new CoordinatePoint(16.42824, 48.26719));
		// NOTE: example on how to add geometry for single edges
		GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = getGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(geometryGeoJson);
		RouteSegment walkToBusStopHeinrichVonBuolGasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(giefinggasseAit)
				.withTo(heinrichVonBuolGasseBusStop)
				.withLengthMeters(200)
				.withDurationSeconds(60)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
				.withGeometryGeoJson(geometryGeoJson).withGeometryGeoJsonEdges(geometryGeoJsonEdges).build();
		segments.add(walkToBusStopHeinrichVonBuolGasse);

		// ### wait for bus ###
		// 5 minutes waiting (=boarding) time
		geometryGeoJson = getGeoJSONLineStringFeature(heinrichVonBuolGasseBusStop, heinrichVonBuolGasseBusStop);
		RouteSegment transferToBusHeinrichVonBuolGasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(heinrichVonBuolGasseBusStop)
				.withTo(heinrichVonBuolGasseBusStop)
				.withLengthMeters(0)
				.withDurationSeconds(60 * 5)
				.withBoardingSeconds(60 * 5)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.TRANSFER).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(transferToBusHeinrichVonBuolGasse);

		// ### ride bus ###
		geometryGeoJson = getGeoJSONLineStringFeature(heinrichVonBuolGasseBusStop, floridsdorfBusStop,
				new CoordinatePoint(16.4236, 48.2621), new CoordinatePoint(16.4044, 48.2576), new CoordinatePoint(
						16.40305, 48.25621), new CoordinatePoint(16.40127, 48.25698));
		RouteSegment busFromHeinrichVonBuolGgasseToFloridsdorf = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(heinrichVonBuolGasseBusStop)
				.withTo(floridsdorfBusStop)
				.withLengthMeters(2500)
				.withDurationSeconds(60 * 10)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BUS)
								.withService(service28A).build()).withGeometryGeoJson(geometryGeoJson).build();
		segments.add(busFromHeinrichVonBuolGgasseToFloridsdorf);

		// ### transfer from bus to subway and wait for subway ###
		// 1 minute walking time + 3 minutes waiting (=boarding) time
		geometryGeoJson = getGeoJSONLineStringFeature(floridsdorfBusStop, floridsdorfSubwayStop);
		RouteSegment transferFloridsdorfFromBusToSubway = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(floridsdorfBusStop)
				.withTo(floridsdorfSubwayStop)
				.withLengthMeters(40)
				.withDurationSeconds(60 * 4)
				.withBoardingSeconds(60 * 3)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.TRANSFER).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(transferFloridsdorfFromBusToSubway);

		// ### ride subway ###
		geometryGeoJson = getGeoJSONLineStringFeature(floridsdorfSubwayStop, handelskaiSubwayStop, new CoordinatePoint(
				16.39468, 48.24630));
		// NOTE: example on how to add additional properties to the geometry be used e.g. in Leaflet
		// let's paint U6 in the color that is used on official maps (and a bit fatter & opaque)
		geometryGeoJson.properties.put("color", "#bf7700");
		geometryGeoJson.properties.put("weight", "10");
		geometryGeoJson.properties.put("opacity", "0.9");
		RouteSegment subwayFromFloridsdorfToHandelskai = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(floridsdorfSubwayStop)
				.withTo(handelskaiSubwayStop)
				.withLengthMeters(2000)
				.withDurationSeconds(60 * 4)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.SUBWAY)
								.withService(serviceU6).build()).withGeometryGeoJson(geometryGeoJson).build();
		segments.add(subwayFromFloridsdorfToHandelskai);

		// ### transfer from subway ###

		// ### walk from subway to station-based sharing (bike-sharing) ###

		// ### ride station-based sharing (bike-sharing) - including rent & return times ###

		// ### walk to free-floating sharing (car-sharing) ###

		// ### ride free-floating sharing (car-sharing) ###

		return segments;
	}

	private GeoJSONFeature<GeoJSONLineString> getGeoJSONLineStringFeature(Location from, Location to,
			CoordinatePoint... geometryInbetween) {
		List<CoordinatePoint> coordinatePoints = new ArrayList<>();
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPoint(from.getCoordinate().geometry));
		coordinatePoints.addAll(Arrays.asList(geometryInbetween));
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPoint(to.getCoordinate().geometry));
		return GeoJSONFeature.newLineStringFeature(coordinatePoints);
	}

	private GeoJSONFeatureCollection<GeoJSONLineString> getGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(
			GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
		GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = new GeoJSONFeatureCollection<>();
		GeoJSONFeature<GeoJSONLineString> lineStringFeature;

		lineStringFeature = GeoJSONFeature.newLineStringFeature(geometryGeoJson.geometry.subLineString(0, 2));
		lineStringFeature.properties.put("name", "Giefinggasse");
		lineStringFeature.properties.put("frc", "6");
		lineStringFeature.properties.put("edgeWeight", "54.1");
		geometryGeoJsonEdges.features.add(lineStringFeature);

		lineStringFeature = GeoJSONFeature.newLineStringFeature(geometryGeoJson.geometry.subLineString(1, 3));
		lineStringFeature.properties.put("name", "Siemensstraße");
		lineStringFeature.properties.put("frc", "2");
		lineStringFeature.properties.put("edgeWeight", "182.5");
		geometryGeoJsonEdges.features.add(lineStringFeature);

		lineStringFeature = GeoJSONFeature.newLineStringFeature(geometryGeoJson.geometry.subLineString(2, 4));
		lineStringFeature.properties.put("name", "Heinrich-von-Buol-Gasse");
		lineStringFeature.properties.put("frc", "3");
		lineStringFeature.properties.put("edgeWeight", "49.8");
		geometryGeoJsonEdges.features.add(lineStringFeature);

		return geometryGeoJsonEdges;
	}

	// ZonedDateTime departureTime = ZonedDateTime.parse("2015-01-01T10:15:30+01:00");
	// ZonedDateTime arrivalTime = departureTime.plus(durationSeconds, ChronoUnit.SECONDS);
	//
	// List<Instruction> navigationInstructions = new ArrayList<>();
	// navigationInstructions.add(BasicRoadInstruction
	// .builder()
	// .forRouteEnd(routeGeometry.getFirst(), Optional.of("Hügelweg"), Optional.of(FormOfWay.PEDESTRIAN_ZONE),
	// Optional.empty()).build());
	// navigationInstructions.add(RoundaboutInstruction.enterBuilder(routeGeometry.getFirst())
	// .withOntoStreetName("Bergstraße").withOntoFormOfWay(FormOfWay.CYCLEPATH).withExitNr(3).build());
}
