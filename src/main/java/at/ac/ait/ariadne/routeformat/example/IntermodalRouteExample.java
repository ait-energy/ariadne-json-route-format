package at.ac.ait.ariadne.routeformat.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.ac.ait.ariadne.routeformat.IntermediateStop;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.Route;
import at.ac.ait.ariadne.routeformat.RouteFormatRoot;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.Service;
import at.ac.ait.ariadne.routeformat.Sproute.Accessibility;
import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.Sharing;
import at.ac.ait.ariadne.routeformat.Sproute.Status;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.location.PointOfInterest;
import at.ac.ait.ariadne.routeformat.location.PublicTransportStop;
import at.ac.ait.ariadne.routeformat.location.SharingStation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class is an example playground where we manually created an intermodal route to check if the route format
 * supports all required attributes / use cases.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class IntermodalRouteExample {

	private Operator wienerLinienOperator, citybikeOperator, car2goOperator, flincOperator;
	private Location giefinggasseAit, heinrichVonBuolGasseBusStop, floridsdorfBusStop, floridsdorfSubwayStop,
			neueDonauSubwayStop, handelskaiSubwayStop, handelskaiSubwayEntry, handelskaiCitybike,
			friedrichEngelsPlatzCitybike, car2goPickup, adalbertStifterStrasse15, privateBicycleHopsagasse,
			antonKummererPark, treustrasse92, gaussplatz;
	private Service service28A, serviceU6;

	public IntermodalRouteExample() {
		initializeOperators();
		initializeLocations();
		initializePublicTransportServices();
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
				.withName("Floridsdorf").withPlatform("C").build();

		floridsdorfSubwayStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.40050", "48.25618")))
				.withName("Floridsdorf").withPlatform("2 (U-Bahn)").build();

		neueDonauSubwayStop = PublicTransportStop
				.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.39468, 48.24630)))
				.withName("Neue Donau")
				.withPlatform("2")
				.withRelatedLines(
						ImmutableMap.of("20A", DetailedModeOfTransportType.BUS, "20B", DetailedModeOfTransportType.BUS))
				.build();

		handelskaiSubwayStop = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.38541, 48.24173)))
				.withName("Handelskai").withPlatform("2").build();

		handelskaiSubwayEntry = PublicTransportStop.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3848877, 48.2416471)))
				.withName("Handelskai (Stationseingang)").build();
		// TODO separately model station entries?

		handelskaiCitybike = SharingStation
				.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3847976, 48.2420356)))
				.withName("Millennium Tower")
				.withId("2005")
				.withModesOfTransport(ImmutableSet.of(GeneralizedModeOfTransportType.BICYCLE))
				.withOperator(citybikeOperator)
				.withAdditionalInfo(ImmutableMap.of("capacity", "35", "bikes_available", "10", "boxes_available", "25"))
				.build();

		friedrichEngelsPlatzCitybike = SharingStation.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3792033, 48.2441354)))
				.withName("Friedrich Engels Platz").withId("2006")
				.withModesOfTransport(ImmutableSet.of(GeneralizedModeOfTransportType.BICYCLE))
				.withOperator(citybikeOperator)
				.withAdditionalInfo(ImmutableMap.of("capacity", "27", "bikes_available", "27", "boxes_available", "0"))
				.build();

		car2goPickup = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.377454, 48.24386)))
				.withAddress(Address.builder().withStreetName("Adalbert-Stifter-Straße").withHouseNumber("71").build())
				.build();

		adalbertStifterStrasse15 = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3655, 48.23752)))
				.withAddress(Address.builder().withStreetName("Adalbert-Stifter-Straße").withHouseNumber("15").build())
				.build();

		privateBicycleHopsagasse = Location
				.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.3715916, 48.246609)))
				.withAddress(
						Address.builder().withStreetName("Hopsagasse").withHouseNumber("5").withPostCode("1200")
								.build()).build();

		antonKummererPark = PointOfInterest.builder().withPoiType("park").withName("Anton-Kummerer-Park")
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.364074, 48.2350109))).build();

		treustrasse92 = Location
				.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.36329, 48.234077)))
				.withAddress(
						Address.builder().withStreetName("Treustraße").withHouseNumber("92").withPostCode("1200")
								.build()).build();

		gaussplatz = Location.builder()
				.withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint(16.369045, 48.2267)))
				.withAddress(Address.builder().withStreetName("Gaußplatz").build()).build();
	}

	private void initializeOperators() {
		wienerLinienOperator = Operator.builder().withName("Wiener Linien").withWebsite("http://www.wienerlinien.at")
				.withCustomerServiceEmail("post@wienerlinien.at")
				.withAdditionalInfo(ImmutableMap.of("email_ticketshop", "ticketshop@wienerlinien.at")).build();

		citybikeOperator = Operator
				.builder()
				.withName("Citybike Wien")
				.withWebsite("http://citybikewien.at")
				.withCustomerServiceEmail("kontakt@citybikewien.at")
				.withCustomerServicePhone("0810 500 500")
				.withAddress(
						Address.builder().withStreetName("Litfaßstraße").withHouseNumber("6").withPostCode("1030")
								.withCity("Wien").build()).build();

		car2goOperator = Operator.builder().withName("Car2Go").withWebsite("https://www.car2go.com/de/wien")
				.withCustomerServiceEmail("wien@car2go.com").build();

		flincOperator = Operator.builder().withName("Flinc").withWebsite("https://flinc.org").build();
	}

	private void initializePublicTransportServices() {
		service28A = Service.builder().withName("28A").withTowards("Floridsdorf").build();
		serviceU6 = Service.builder().withName("U6").withTowards("Siebenhirten").withColor("#bf7700").build();
	}

	public RouteFormatRoot getRouteFormatRoot() throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, Object> additionalInfoRouteRequest = new HashMap<>();
		additionalInfoRouteRequest.put("ait:additionalTestBigDecimal", new BigDecimal("12.34567"));
		additionalInfoRouteRequest.put("ait:additionalTestObject", wienerLinienOperator);
		additionalInfoRouteRequest.put("ait:additionalTestList", Lists.newArrayList(1, 2, 3, 4, 5, 6, 7));
		additionalInfoRouteRequest.put("ait:additionalTestString", "hello this is a String");

		Map<GeneralizedModeOfTransportType, List<Location>> privateVehicleLocations = new HashMap<>();
		privateVehicleLocations.put(GeneralizedModeOfTransportType.BICYCLE,
				Arrays.asList(adalbertStifterStrasse15, privateBicycleHopsagasse));
		privateVehicleLocations.put(GeneralizedModeOfTransportType.CAR, Arrays.asList(treustrasse92));

		RoutingRequest request = RoutingRequest.builder().withServiceId("ariadne_webservice_vienna")
				.withFrom(giefinggasseAit).withTo(gaussplatz).withDepartureTime("2016-01-01T15:00:00+01:00")
				.withModesOfTransport(Sets.newHashSet(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT))
				// TODO more finegrained support for MOTs!
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
				.withDepartureTime("2016-01-01T15:00:00+01:00")
				.withArrivalTime("2016-01-01T15:01:00+01:00")
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
				.withGeometryGeoJson(geometryGeoJson).withGeometryGeoJsonEdges(geometryGeoJsonEdges).build();
		segments.add(walkToBusStopHeinrichVonBuolGasse);

		// ### wait for bus ###
		geometryGeoJson = getGeoJSONLineStringFeature(heinrichVonBuolGasseBusStop, heinrichVonBuolGasseBusStop);
		RouteSegment transferToBusHeinrichVonBuolGasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(heinrichVonBuolGasseBusStop)
				.withTo(heinrichVonBuolGasseBusStop)
				.withLengthMeters(0)
				// 5 minutes waiting (=boarding) time
				.withDurationSeconds(60 * 5)
				.withBoardingSeconds(60 * 5)
				.withDepartureTime("2016-01-01T15:01:00+01:00")
				.withArrivalTime("2016-01-01T15:06:00+01:00")
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.TRANSFER).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(transferToBusHeinrichVonBuolGasse);

		// ### ride bus (bus is not wheelchair accessible!) ###
		geometryGeoJson = getGeoJSONLineStringFeature(heinrichVonBuolGasseBusStop, floridsdorfBusStop,
				new CoordinatePoint(16.42354, 48.26306), new CoordinatePoint(16.4236, 48.2621), new CoordinatePoint(
						16.4044, 48.2576), new CoordinatePoint(16.40305, 48.25621), new CoordinatePoint(16.40127,
						48.25698));
		RouteSegment busFromHeinrichVonBuolGgasseToFloridsdorf = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(heinrichVonBuolGasseBusStop)
				.withTo(floridsdorfBusStop)
				.withLengthMeters(2500)
				.withDurationSeconds(60 * 10 + 30)
				.withDepartureTime("2016-01-01T15:06:00+01:00")
				.withArrivalTime("2016-01-01T15:16:30+01:00")
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BUS)
								.withAccessibility(Arrays.asList(Accessibility.NOT_WHEELHAIR_ACCESSIBLE))
								.withService(service28A).withOperator(wienerLinienOperator).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(busFromHeinrichVonBuolGgasseToFloridsdorf);

		// ### transfer from bus to subway and wait for subway
		// (first use escalator, then stairs, then escalator again) ###
		geometryGeoJson = getGeoJSONLineStringFeature(floridsdorfBusStop, floridsdorfSubwayStop);
		RouteSegment transferFloridsdorfFromBusToSubway = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(floridsdorfBusStop)
				.withTo(floridsdorfSubwayStop)
				.withLengthMeters(40)
				// 1 minute walking time + 3 minutes waiting (=boarding) time
				.withDurationSeconds(60 * 4)
				.withBoardingSeconds(60 * 3)
				.withDepartureTime("2016-01-01T15:16:30+01:00")
				.withArrivalTime("2016-01-01T15:20:30+01:00")
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.TRANSFER).build())
				.withAccessibility(
						Arrays.asList(Accessibility.ESCALATOR, Accessibility.STAIRS, Accessibility.ESCALATOR))
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(transferFloridsdorfFromBusToSubway);

		// ### ride subway (wheelchair accessible!) ###
		geometryGeoJson = getGeoJSONLineStringFeature(floridsdorfSubwayStop, handelskaiSubwayStop, new CoordinatePoint(
				16.39468, 48.24630));
		RouteSegment subwayFromFloridsdorfToHandelskai = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(floridsdorfSubwayStop)
				.withTo(handelskaiSubwayStop)
				.withLengthMeters(2000)
				.withDurationSeconds(60 * 4)
				.withDepartureTime("2016-01-01T15:20:30+01:00")
				.withArrivalTime("2016-01-01T15:24:30+01:00")
				.withIntermediateStops(Arrays.asList(getIntermediateStopNeueDonau()))
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.SUBWAY)
								.withAccessibility(Arrays.asList(Accessibility.WHEELCHAIR_ACCESSIBLE))
								.withService(serviceU6).withOperator(wienerLinienOperator).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(subwayFromFloridsdorfToHandelskai);

		// ### transfer from subway ###
		geometryGeoJson = getGeoJSONLineStringFeature(handelskaiSubwayStop, handelskaiSubwayEntry);
		RouteSegment transferHandelskaiFromSubwayToExit = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(handelskaiSubwayStop)
				.withTo(handelskaiSubwayEntry)
				.withLengthMeters(40)
				// 3 minutes walking time
				.withDurationSeconds(60 * 3)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.TRANSFER).build())
				.withAccessibility(Arrays.asList(Accessibility.ESCALATOR)).withGeometryGeoJson(geometryGeoJson).build();
		segments.add(transferHandelskaiFromSubwayToExit);

		// ### walk from subway to station-based sharing (bike-sharing) ###
		geometryGeoJson = getGeoJSONLineStringFeature(handelskaiSubwayEntry, handelskaiCitybike, new CoordinatePoint(
				16.3845846, 48.2418792));
		RouteSegment walkToCitybikeHandelskai = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(handelskaiSubwayEntry)
				.withTo(handelskaiCitybike)
				.withLengthMeters(57)
				.withDurationSeconds(40)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(walkToCitybikeHandelskai);

		// ### ride station-based sharing (bike-sharing) - including rent & return times ###
		geometryGeoJson = getGeoJSONLineStringFeature(handelskaiCitybike, friedrichEngelsPlatzCitybike,
				new CoordinatePoint(16.3838145, 48.2413853), new CoordinatePoint(16.3807639, 48.2442201),
				new CoordinatePoint(16.3793906, 48.2438237));
		// NOTE: example on how to add additional properties to the geometry be used e.g. in Leaflet
		geometryGeoJson.properties.put("color", "#FFBBCC");
		geometryGeoJson.properties.put("weight", "7");
		geometryGeoJson.properties.put("opacity", "0.9");
		RouteSegment citybikeFromHandelskaiToFriedrichEngelsPlatz = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(handelskaiCitybike)
				.withTo(friedrichEngelsPlatzCitybike)
				.withLengthMeters(692)
				// 3 minutes ride, 2 minutes renting the bike, 1 minute returning the bike
				.withDurationSeconds(60 * 6)
				.withBoardingSeconds(60 * 2)
				.withAlightingSeconds(60 * 1)
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BICYCLE)
								.withSharingType(Sharing.STATION_BOUND_VEHICLE_SHARING).withOperator(citybikeOperator)
								.build()).withGeometryGeoJson(geometryGeoJson).build();
		segments.add(citybikeFromHandelskaiToFriedrichEngelsPlatz);

		// ### walk to free-floating sharing (car-sharing) - over stairs (general info that there are stairs) ###
		geometryGeoJson = getGeoJSONLineStringFeature(friedrichEngelsPlatzCitybike, car2goPickup, new CoordinatePoint(
				16.37763018715632, 48.243692289787816));
		RouteSegment walkToCar2go = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(friedrichEngelsPlatzCitybike)
				.withTo(car2goPickup)
				.withLengthMeters(150)
				.withDurationSeconds(115)
				.withAccessibility(Arrays.asList(Accessibility.STAIRS))
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(walkToCar2go);

		// ### ride free-floating sharing (electric car-sharing) ###
		geometryGeoJson = getGeoJSONLineStringFeature(car2goPickup, adalbertStifterStrasse15, new CoordinatePoint(
				16.373601, 48.24218));
		RouteSegment car2goAlongAdalbertStifterStrasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(car2goPickup)
				.withTo(adalbertStifterStrasse15)
				.withLengthMeters(692)
				// 4 minutes ride, 2 minutes renting the car, 1 minute parking the car
				.withDurationSeconds(60 * 7)
				.withBoardingSeconds(60 * 2)
				.withAlightingSeconds(60 * 1)
				.withModeOfTransport(
						ModeOfTransport.builder()
								.withDetailedType(DetailedModeOfTransportType.CAR)
								.withSharingType(Sharing.FREE_FLOATING_VEHICLE_SHARING)
								.withElectric(true)
								.withOperator(car2goOperator)
								// for now specific information goes as additional info
								.withAdditionalInfo(
										ImmutableMap.of("licensePlate", "W-123456", "fuelPercentage", "80",
												"interiorState", "good", "exteriorState", "unacceptable")).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(car2goAlongAdalbertStifterStrasse);

		// ### ride private vehicle (bicycle) - and there is a park as potential stop on the way ###
		geometryGeoJson = getGeoJSONLineStringFeature(adalbertStifterStrasse15, treustrasse92, new CoordinatePoint(
				16.36515, 48.23729), new CoordinatePoint(16.3656, 48.23515), new CoordinatePoint(16.36288, 48.23509));
		RouteSegment bicycleFromAdalbertStifterStrasseToTreugasse = RouteSegment
				.builder()
				.withNr(++segmentNr)
				.withFrom(adalbertStifterStrasse15)
				.withTo(treustrasse92)
				.withLengthMeters(597)
				// 106 seconds ride, 1 minutes unlocking bike, 1 minute parking & locking biker
				.withDurationSeconds(106 + 60 * 2)
				.withBoardingSeconds(60 * 1)
				.withAlightingSeconds(60 * 1)
				.withIntermediateStops(Arrays.asList(IntermediateStop.builder().withStop(antonKummererPark).build()))
				// additional info about vehicles provided in the request can be added here (such as in project SMILE)
				.withAdditionalInfo(ImmutableMap.of("name", "Univega Mountainbike"))
				.withModeOfTransport(
						ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BICYCLE).build())
				.withGeometryGeoJson(geometryGeoJson).build();
		segments.add(bicycleFromAdalbertStifterStrasseToTreugasse);

		// ### ride-sharing (via car) ###
		geometryGeoJson = getGeoJSONLineStringFeature(treustrasse92, gaussplatz, new CoordinatePoint(16.3644, 48.2311),
				new CoordinatePoint(16.36638, 48.22886));
		RouteSegment rideSharingFromTreugasseToGaussplatz = RouteSegment.builder()
				.withNr(++segmentNr)
				.withFrom(treustrasse92)
				.withTo(gaussplatz)
				.withLengthMeters(941)
				// 112 seconds ride, 3 minutes waiting for ride-sharing & hopping on board
				.withDurationSeconds(112 + 60 * 3)
				.withBoardingSeconds(60 * 3)
				.withModeOfTransport(
						ModeOfTransport
								.builder()
								.withDetailedType(DetailedModeOfTransportType.CAR)
								.withSharingType(Sharing.RIDE_SHARING)
								.withOperator(flincOperator)
								// for now specific information goes as additional info
								.withAdditionalInfo(
										ImmutableMap.of("userName", "herbertWien78", "phoneNumber", "+43 650 7734343"))
								.build()).withGeometryGeoJson(geometryGeoJson).build();
		segments.add(rideSharingFromTreugasseToGaussplatz);

		return segments;
	}

	private GeoJSONFeature<GeoJSONLineString> getGeoJSONLineStringFeature(Location from, Location to,
			CoordinatePoint... geometryInbetween) {
		List<CoordinatePoint> coordinatePoints = new ArrayList<>();
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPointFeature(from.getCoordinate()));
		coordinatePoints.addAll(Arrays.asList(geometryInbetween));
		coordinatePoints.add(CoordinatePoint.fromGeoJSONPointFeature(to.getCoordinate()));
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

	private IntermediateStop getIntermediateStopNeueDonau() {
		ZonedDateTime arrivalTime = ZonedDateTime.parse("2016-01-01T15:22:30+01:00");
		ZonedDateTime departureTime = arrivalTime.plus(60, ChronoUnit.SECONDS);
		return IntermediateStop.builder().withStop(neueDonauSubwayStop).withPlannedArrivalTime(arrivalTime)
				.withPlannedDepartureTime(departureTime).withEstimatedArrivalTime(arrivalTime)
				.withEstimatedDepartureTime(departureTime).build();
	}

	// TODO add exemplary navigation instructions
	// List<Instruction> navigationInstructions = new ArrayList<>();
	// navigationInstructions.add(BasicRoadInstruction
	// .builder()
	// .forRouteEnd(routeGeometry.getFirst(), Optional.of("Hügelweg"), Optional.of(FormOfWay.PEDESTRIAN_ZONE),
	// Optional.empty()).build());
	// navigationInstructions.add(RoundaboutInstruction.enterBuilder(routeGeometry.getFirst())
	// .withOntoStreetName("Bergstraße").withOntoFormOfWay(FormOfWay.CYCLEPATH).withExitNr(3).build());
}
