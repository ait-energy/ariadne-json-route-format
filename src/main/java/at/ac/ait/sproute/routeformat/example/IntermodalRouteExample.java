package at.ac.ait.sproute.routeformat.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.ac.ait.sproute.routeformat.ModeOfTransport;
import at.ac.ait.sproute.routeformat.Route;
import at.ac.ait.sproute.routeformat.RouteFormatRoot;
import at.ac.ait.sproute.routeformat.RouteSegment;
import at.ac.ait.sproute.routeformat.RoutingRequest;
import at.ac.ait.sproute.routeformat.Service;
import at.ac.ait.sproute.routeformat.ServiceProvider;
import at.ac.ait.sproute.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.sproute.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.sproute.routeformat.Sproute.Status;
import at.ac.ait.sproute.routeformat.geojson.CoordinatePoint;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.sproute.routeformat.location.Address;
import at.ac.ait.sproute.routeformat.location.Location;
import at.ac.ait.sproute.routeformat.location.PointOfInterest;
import at.ac.ait.sproute.routeformat.location.PublicTransportStop;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class IntermodalRouteExample {

	private ServiceProvider wienerLinienProvider;
	private Service service28A, serviceU6;
	private Location giefinggasseAit, heinrichVonBuolGasseBusStop, floridsdorfBusStop, floridsdorfSubwayStop;

	public IntermodalRouteExample() {
		giefinggasseAit = getLocationGiefinggasseAit();
		heinrichVonBuolGasseBusStop = getLocationHeinrichVonBuolGasseBusStop();
		floridsdorfBusStop = getLocationFloridsdorfBusStop();
		floridsdorfSubwayStop = getLocationFloridsdorfSubwayStop();

		wienerLinienProvider = getWienerLinienServiceProvider();
		service28A = get28AService(wienerLinienProvider);
		serviceU6 = getU6Service(wienerLinienProvider);
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

	private Location getLocationGiefinggasseAit() {
		GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature.newPointFeature(new CoordinatePoint("16.4265263",
				"48.2686617"));
		Address giefinggasse = Address.builder().withCountry("Austria").withCity("Wien").withPostCode("1210")
				.withStreetName("Giefinggasse").withHouseNumber("2b").build();
		return PointOfInterest.builder().withCoordinate(coordinate).withAddress(giefinggasse).withName("AIT")
				.withPoiType("company").build();
	}

	private Location getLocationHeinrichVonBuolGasseBusStop() {
		GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature.newPointFeature(new CoordinatePoint("16.42791",
				"48.26680"));
		return PublicTransportStop.builder().withCoordinate(coordinate)
				.withName("Heinrich-von-Buol-Gasse/Siemensstraße").build();
	}

	private Location getLocationFloridsdorfBusStop() {
		GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature.newPointFeature(new CoordinatePoint("16.40073",
				"48.25625"));
		return PublicTransportStop.builder().withCoordinate(coordinate).withName("Floridsdorf Bus")
				.withRelatedLines(Arrays.asList("29A")).build(); // TODO bahnsteig?
	}

	private Location getLocationFloridsdorfSubwayStop() {
		GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature.newPointFeature(new CoordinatePoint("16.40050",
				"48.25618"));
		return PublicTransportStop.builder().withCoordinate(coordinate).withName("Floridsdorf U-Bahn").build();
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

		List<CoordinatePoint> coordinatePoints = Arrays.asList(CoordinatePoint.fromGeoJSONPoint(giefinggasseAit
				.getCoordinate().geometry), new CoordinatePoint(16.4263, 48.2682), new CoordinatePoint(16.42824,
				48.26719), CoordinatePoint.fromGeoJSONPoint(heinrichVonBuolGasseBusStop.getCoordinate().geometry));
		geometryGeoJson = GeoJSONFeature.newLineStringFeature(coordinatePoints);

		geometryGeoJson = getGeoJSONLineStringFeature(giefinggasseAit, heinrichVonBuolGasseBusStop,
				new CoordinatePoint(16.4263, 48.2682), new CoordinatePoint(16.42824, 48.26719));
		// example on how to add additional properties to the geometry be used e.g. in Leaflet
		geometryGeoJson.properties.put("color", "#FF11BB");
		geometryGeoJson.properties.put("weight", 2);
		geometryGeoJson.properties.put("opacity", "0.5");
		RouteSegment walkToBusStopHeinrichVonBuolGasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(giefinggasseAit)
				.withTo(heinrichVonBuolGasseBusStop)
				.withLengthMeters(200)
				.withDurationSeconds(60)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(walkToBusStopHeinrichVonBuolGasse);

		// 5 minutes waiting time (=boarding time)
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

		// 1 minute walking + 3 minutes waiting time (=boarding time)
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

	// public void oid() {
	// ZonedDateTime departureTime = ZonedDateTime.parse("2015-01-01T10:15:30+01:00");
	// ZonedDateTime arrivalTime = departureTime.plus(durationSeconds, ChronoUnit.SECONDS);
	//
	// GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(routeGeometry);
	// geometryGeoJson = GeoJSONFeature.newLineStringFeature(routeGeometry);
	// geometryGeoJson.properties.put("color", "#FF11BB");
	// geometryGeoJson.properties.put("weight", 2);
	// geometryGeoJson.properties.put("opacity", "0.5");
	//
	// GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = new GeoJSONFeatureCollection<>();
	// GeoJSONFeature<GeoJSONLineString> giefinggasseFeature = GeoJSONFeature.newLineStringFeature(routeGeometry
	// .subList(0, 2));
	// giefinggasseFeature.properties.put("name", "Giefinggasse");
	// giefinggasseFeature.properties.put("frc", "6");
	// giefinggasseFeature.properties.put("edgeWeight", "123");
	// geometryGeoJsonEdges.features.add(giefinggasseFeature);
	// GeoJSONFeature<GeoJSONLineString> paukerwerkgasseJson = GeoJSONFeature.newLineStringFeature(routeGeometry
	// .subList(1, 3));
	// paukerwerkgasseJson.properties.put("name", "Paukerwerkgasse");
	// paukerwerkgasseJson.properties.put("frc", "6");
	// paukerwerkgasseJson.properties.put("edgeWeight", "187");
	// geometryGeoJsonEdges.features.add(paukerwerkgasseJson);
	//
	// List<Instruction> navigationInstructions = new ArrayList<>();
	// navigationInstructions.add(BasicRoadInstruction
	// .builder()
	// .forRouteEnd(routeGeometry.getFirst(), Optional.of("Hügelweg"), Optional.of(FormOfWay.PEDESTRIAN_ZONE),
	// Optional.empty()).build());
	// navigationInstructions.add(RoundaboutInstruction.enterBuilder(routeGeometry.getFirst())
	// .withOntoStreetName("Bergstraße").withOntoFormOfWay(FormOfWay.CYCLEPATH).withExitNr(3).build());
	// }
}
