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
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import at.ac.ait.ariadne.routeformat.Constants.Accessibility;
import at.ac.ait.ariadne.routeformat.Constants.AccessibilityRestriction;
import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.ParkingType;
import at.ac.ait.ariadne.routeformat.Constants.Preposition;
import at.ac.ait.ariadne.routeformat.Constants.Sharing;
import at.ac.ait.ariadne.routeformat.Constants.Speed;
import at.ac.ait.ariadne.routeformat.Constants.Status;
import at.ac.ait.ariadne.routeformat.Constants.TurnDirection;
import at.ac.ait.ariadne.routeformat.Constants.VehicleAccessibility;
import at.ac.ait.ariadne.routeformat.IntermediateStop;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.RequestModeOfTransport;
import at.ac.ait.ariadne.routeformat.RequestPTModeOfTransport;
import at.ac.ait.ariadne.routeformat.Route;
import at.ac.ait.ariadne.routeformat.RouteFormatRoot;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.Service;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONMultiPolygon;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.instruction.BasicRoadInstruction;
import at.ac.ait.ariadne.routeformat.instruction.Instruction;
import at.ac.ait.ariadne.routeformat.instruction.Landmark;
import at.ac.ait.ariadne.routeformat.instruction.RoundaboutInstruction;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.location.Parking;
import at.ac.ait.ariadne.routeformat.location.PointOfInterest;
import at.ac.ait.ariadne.routeformat.location.PublicTransportStop;
import at.ac.ait.ariadne.routeformat.location.SharingStation;

/**
 * This class is an example playground where we manually created an intermodal
 * route to check if the route format supports all required attributes / use
 * cases.
 * <p>
 * It may also be useful for writing unit tests with a predefined route and
 * route segments (which we try to change as little as possible in future
 * versions).
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class IntermodalRouteExample {

	private Operator wienerLinienOperator, citybikeOperator, car2goOperator, flincOperator;
	private ModeOfTransport wienerLinienMot, citybikeMot, car2goMot, flincMot;
	private Location<?> giefinggasseAit, heinrichVonBuolGasseBusStop, floridsdorfBusStop, floridsdorfSubwayStop,
			neueDonauSubwayStop, handelskaiSubwayStop, handelskaiSubwayEntry, handelskaiCitybike,
			friedrichEngelsPlatzCitybike, car2goPickup, adalbertStifterStrasse15, privateBicycleHopsagasse,
			antonKummererPark, treustrasse92, gaussplatz, scholzgasse1Parking;
	private Service service28A, serviceU6;
	private RouteSegment walkToBusStopHeinrichVonBuolGasse, transferToBusHeinrichVonBuolGasse,
			busFromHeinrichVonBuolGgasseToFloridsdorf, citybikeFromHandelskaiToFriedrichEngelsPlatz,
			car2goAlongAdalbertStifterStrasse, bicycleFromAdalbertStifterStrasseToTreugasse,
			rideSharingFromTreugasseToGaussplatz, carFromGaussplatzToScholzgasse;
	private RouteFormatRoot root;

	public IntermodalRouteExample() throws JsonGenerationException, JsonMappingException, IOException {
		initializeOperators();
		initializeComplexModesOfTransport();
		initializeLocations();
		initializePublicTransportServices();
		this.root = createRouteFormatRoot();
	}

	private void initializeOperators() {
		wienerLinienOperator = Operator.createMinimal("Wiener Linien").setWebsite("http://www.wienerlinien.at")
				.setCustomerServiceEmail("post@wienerlinien.at")
				.setAdditionalInfo(ImmutableMap.of("email_ticketshop", "ticketshop@wienerlinien.at"));

		citybikeOperator = Operator.createMinimal("Citybike Wien").setWebsite("http://citybikewien.at")
				.setCustomerServiceEmail("kontakt@citybikewien.at").setCustomerServicePhone("0810 500 500")
				.setAddress(new Address().setStreetName("Litfaßstraße").setHouseNumber("6").setPostCode("1030")
						.setCity("Wien"));

		car2goOperator = Operator.createMinimal("Car2Go").setWebsite("https://www.car2go.com/de/wien")
				.setCustomerServiceEmail("wien@car2go.com");

		flincOperator = Operator.createMinimal("Flinc").setWebsite("https://flinc.org");
	}

	private void initializeComplexModesOfTransport() {
		wienerLinienMot = ModeOfTransport.createMinimal(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT)
				.setOperator(wienerLinienOperator);

		citybikeMot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.BICYCLE)
				.setSharingType(Sharing.STATION_BOUND_VEHICLE_SHARING).setOperator(citybikeOperator);

		car2goMot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.CAR)
				.setSharingType(Sharing.FREE_FLOATING_VEHICLE_SHARING).setElectric(true).setOperator(car2goOperator);

		flincMot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.CAR).setSharingType(Sharing.RIDE_SHARING)
				.setOperator(flincOperator);
	}

	private void initializeLocations() {
		Address giefinggasse = new Address().setCountry("Austria").setCity("Wien").setPostCode("1210")
				.setStreetName("Giefinggasse").setHouseNumber("2b")
				.setAdditionalInfo(ImmutableMap.of("floor", "3", "room", "S313"));

		giefinggasseAit = PointOfInterest.createMinimal(GeoJSONCoordinate.create("16.4265263", "48.2686617"))
				.setAddress(giefinggasse).setName("AIT").setPoiType("company");

		heinrichVonBuolGasseBusStop = PublicTransportStop
				.createMinimal(GeoJSONCoordinate.create("16.42791", "48.26680"))
				.setName("Heinrich-von-Buol-Gasse/Siemensstraße");

		floridsdorfBusStop = PublicTransportStop.createMinimal(GeoJSONCoordinate.create("16.40073", "48.25625"))
				.setName("Floridsdorf").setPlatform("C");

		floridsdorfSubwayStop = PublicTransportStop.createMinimal(GeoJSONCoordinate.create("16.40050", "48.25618"))
				.setName("Floridsdorf").setPlatform("2 (U-Bahn)");

		neueDonauSubwayStop = PublicTransportStop.createMinimal(GeoJSONCoordinate.create("16.39468", "48.24630"))
				.setName("Neue Donau").setPlatform("2").setRelatedLines(ImmutableMap.of("20A",
						DetailedModeOfTransportType.BUS, "20B", DetailedModeOfTransportType.BUS));

		handelskaiSubwayStop = PublicTransportStop.createMinimal(GeoJSONCoordinate.create("16.38541", "48.24173"))
				.setName("Handelskai").setPlatform("2");

		handelskaiSubwayEntry = PublicTransportStop.createMinimal(GeoJSONCoordinate.create("16.3848877", "48.2416471"))
				.setName("Handelskai (Stationseingang)");

		handelskaiCitybike = SharingStation.createMinimal(GeoJSONCoordinate.create("16.3847976", "48.2420356"))
				.setName("Millennium Tower").setId("2005")
				.setModesOfTransport(Arrays.asList(GeneralizedModeOfTransportType.BICYCLE))
				.setOperator(citybikeOperator)
				.setAdditionalInfo(ImmutableMap.of("capacity", "35", "bikes_available", "10", "boxes_available", "25"));

		friedrichEngelsPlatzCitybike = SharingStation
				.createMinimal(GeoJSONCoordinate.create("16.3792033", "48.2441354")).setName("Friedrich Engels Platz")
				.setId("2006").setModesOfTransport(Arrays.asList(GeneralizedModeOfTransportType.BICYCLE))
				.setOperator(citybikeOperator)
				.setAdditionalInfo(ImmutableMap.of("capacity", "27", "bikes_available", "27", "boxes_available", "0"));

		car2goPickup = Location.createMinimal(GeoJSONCoordinate.create("16.377454", "48.24386"))
				.setAddress(new Address().setStreetName("Adalbert-Stifter-Straße").setHouseNumber("71"));

		adalbertStifterStrasse15 = Location.createMinimal(GeoJSONCoordinate.create("16.3655", "48.23752"))
				.setAddress(new Address().setStreetName("Adalbert-Stifter-Straße").setHouseNumber("15"));

		privateBicycleHopsagasse = Location.createMinimal(GeoJSONCoordinate.create("16.3715916", "48.246609"))
				.setAddress(new Address().setStreetName("Hopsagasse").setHouseNumber("5").setPostCode("1200"));

		antonKummererPark = PointOfInterest.createMinimal(GeoJSONCoordinate.create("16.364074", "48.2350109"))
				.setPoiType("park").setName("Anton-Kummerer-Park");

		treustrasse92 = Location.createMinimal(GeoJSONCoordinate.create("16.36329", "48.234077"))
				.setAddress(new Address().setStreetName("Treustraße").setHouseNumber("92").setPostCode("1200"));

		gaussplatz = Location.createMinimal(GeoJSONCoordinate.create("16.369045", "48.2267"))
				.setAddress(new Address().setStreetName("Gaußplatz"));

		scholzgasse1Parking = Parking.createMinimal(GeoJSONCoordinate.create("16.3695", "48.2243"))
				.setAddress(new Address().setStreetName("Scholzgasse").setHouseNumber("1").setPostCode("1020"))
				.setFee(true).setName("Virtuelle Parkgarage Scholzgasse")
				.setModesOfTransport(
						ImmutableSet.of(GeneralizedModeOfTransportType.CAR, GeneralizedModeOfTransportType.BICYCLE))
				.setParkingType(ParkingType.UNDERGROUND).setParkAndRide(false);
	}

	private void initializePublicTransportServices() {
		service28A = Service.createMinimal("28A").setTowards("Floridsdorf");
		serviceU6 = Service.createMinimal("U6").setTowards("Siebenhirten").setColor("#bf7700");
	}

	/**
	 * @return a cached instance of an intermodal showcase-route
	 */
	public RouteFormatRoot getRouteFormatRoot() {
		return root;
	}

	private RouteFormatRoot createRouteFormatRoot() throws JsonGenerationException, JsonMappingException, IOException {
		RouteFormatRoot root = new RouteFormatRoot().setRouteFormatVersion("X.Y-UNKNOWN").setRequestId("999")
				.setProcessedTime("1970-01-01T00:00:00Z").setStatus(Status.OK)
				.setDebugMessage("Route calculated in 0.002 seconds").setCoordinateReferenceSystem("EPSG:4326")
				.setRequest(createRoutingRequest()).setRoutes(Arrays.asList(
						Route.createFromSegments(createRouteSegments()).setOptimizedFor("travel time and low costs")));
		root.validate();
		return root;
	}

	private RoutingRequest createRoutingRequest() {
		Map<String, Object> additionalInfoRouteRequest = new HashMap<>();
		additionalInfoRouteRequest.put("ait:additionalTestBigDecimal", new BigDecimal("12.34567"));
		additionalInfoRouteRequest.put("ait:additionalTestObject", wienerLinienOperator);
		additionalInfoRouteRequest.put("ait:additionalTestList", Lists.newArrayList(1, 2, 3, 4, 5, 6, 7));
		additionalInfoRouteRequest.put("ait:additionalTestString", "hello this is a String");

		GeoJSONFeature<GeoJSONPolygon> forbiddenPolygon = GeoJSONFeature.createPolygonFeatureFromCoordinatePoints(
				Arrays.asList(Arrays.asList(GeoJSONCoordinate.create("16", "48"),
						GeoJSONCoordinate.create("16.1", "48"), GeoJSONCoordinate.create("16.1", "48.1"),
						GeoJSONCoordinate.create("16.1", "48"), GeoJSONCoordinate.create("16", "48"))));
		GeoJSONFeature<GeoJSONMultiPolygon> forbiddenAreas = GeoJSONFeature
				.createMultiPolygonFeatureFromPolygons(Arrays.asList(forbiddenPolygon));

		List<RequestModeOfTransport<?>> requestModes = new ArrayList<>();
		requestModes.add(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_FOOT)
				.setMaximumDistanceMeters(2000).setSpeed(Speed.FAST.name()).setForbiddenAreas(forbiddenAreas));
		requestModes.add(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_BICYCLE)
				.setMaximumTravelTimeSeconds(45 * 60).setSpeed(Speed.SLOW.name())
				.setLocations(Arrays.asList(adalbertStifterStrasse15, privateBicycleHopsagasse)));
		requestModes.add(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_MOTORCYCLE));
		RequestModeOfTransport<?> carMot = RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_CAR)
				.setLocations(Arrays.asList(treustrasse92));
		requestModes.add(carMot);
		requestModes.add(RequestPTModeOfTransport.createMinimal(wienerLinienMot)
				.setExcludedPublicTransportModes(ImmutableSet.of(DetailedModeOfTransportType.AERIALWAY,
						DetailedModeOfTransportType.AIRPLANE, DetailedModeOfTransportType.SHIP)));
		requestModes.add(RequestModeOfTransport.createMinimal(citybikeMot));
		requestModes.add(RequestModeOfTransport.createMinimal(car2goMot));
		requestModes.add(RequestModeOfTransport.createMinimal(flincMot));

		String serviceId = "ariadne_webservice_vienna";
		Location<?> from = giefinggasseAit;
		Location<?> to = scholzgasse1Parking;
		return RoutingRequest.createMinimal(serviceId, from, to, requestModes)
				.setDepartureTime("2016-01-01T15:00:00+01:00").setLanguage("DE")
				.setAccessibilityRestrictions(ImmutableSet.of(AccessibilityRestriction.NO_ELEVATOR))
				.setOptimizedFor("ENERGY").setMaximumTransfers(10).setEndModeOfTransport(carMot)
				.setAdditionalInfo(additionalInfoRouteRequest);
	}

	private LinkedList<RouteSegment> createRouteSegments() {
		LinkedList<RouteSegment> segments = new LinkedList<>();
		GeoJSONFeature<GeoJSONLineString> geometryGeoJson;

		int segmentNr = 0;

		// ### walk to bus ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(giefinggasseAit, heinrichVonBuolGasseBusStop,
				GeoJSONCoordinate.create("16.4263", "48.2682"), GeoJSONCoordinate.create("16.42824", "48.26719"));
		// NOTE: example on how to add geometry for single edges
		GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = createGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(
				geometryGeoJson);
		walkToBusStopHeinrichVonBuolGasse = new RouteSegment().setNr(++segmentNr).setFrom(giefinggasseAit)
				.setTo(heinrichVonBuolGasseBusStop).setDistanceMeters(200).setDurationSeconds(60)
				.setStartTime("2016-01-01T15:00:00+01:00").setEndTime("2016-01-01T15:01:00+01:00")
				.setModeOfTransport(ModeOfTransport.STANDARD_FOOT).setGeometryGeoJson(geometryGeoJson)
				.setGeometryGeoJsonEdges(geometryGeoJsonEdges);
		segments.add(walkToBusStopHeinrichVonBuolGasse);

		// ### wait for bus ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(heinrichVonBuolGasseBusStop,
				heinrichVonBuolGasseBusStop);
		transferToBusHeinrichVonBuolGasse = new RouteSegment().setNr(++segmentNr).setFrom(heinrichVonBuolGasseBusStop)
				.setTo(heinrichVonBuolGasseBusStop).setDistanceMeters(0)
				// 5 minutes waiting (=boarding) time
				.setDurationSeconds(60 * 5).setBoardingSeconds(60 * 5).setStartTime("2016-01-01T15:01:00+01:00")
				.setEndTime("2016-01-01T15:06:00+01:00").setModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(transferToBusHeinrichVonBuolGasse);

		// ### ride bus (bus is not wheelchair accessible!) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(heinrichVonBuolGasseBusStop, floridsdorfBusStop,
				GeoJSONCoordinate.create("16.42354", "48.26306"), GeoJSONCoordinate.create("16.4236", "48.2621"),
				GeoJSONCoordinate.create("16.4044", "48.2576"), GeoJSONCoordinate.create("16.40305", "48.25621"),
				GeoJSONCoordinate.create("16.40127", "48.25698"));
		busFromHeinrichVonBuolGgasseToFloridsdorf = new RouteSegment().setNr(++segmentNr)
				.setFrom(heinrichVonBuolGasseBusStop).setTo(floridsdorfBusStop).setDistanceMeters(2500)
				.setDurationSeconds(60 * 10 + 30).setStartTime("2016-01-01T15:06:00+01:00")
				.setEndTime("2016-01-01T15:16:30+01:00")
				.setModeOfTransport(ModeOfTransport.createMinimal(DetailedModeOfTransportType.BUS)
						.setAccessibility(Sets.newHashSet(VehicleAccessibility.HIGH_FLOOR_VEHICLE))
						.setService(service28A).setOperator(wienerLinienOperator))
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(busFromHeinrichVonBuolGgasseToFloridsdorf);

		// ### transfer from bus to subway and wait for subway (walk down
		// stairs) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(floridsdorfBusStop, floridsdorfSubwayStop);
		RouteSegment transferFloridsdorfFromBusToSubway = new RouteSegment().setNr(++segmentNr)
				.setFrom(floridsdorfBusStop).setTo(floridsdorfSubwayStop).setDistanceMeters(40)
				// 1 minute walking time + 3 minutes waiting (=boarding) time
				.setDurationSeconds(60 * 4).setBoardingSeconds(60 * 3).setStartTime("2016-01-01T15:16:30+01:00")
				.setEndTime("2016-01-01T15:20:30+01:00").setModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
				.setAccessibility(Arrays.asList(Accessibility.STAIRS_DOWN)).setGeometryGeoJson(geometryGeoJson);
		segments.add(transferFloridsdorfFromBusToSubway);

		// ### ride subway (wheelchair accessible!) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(floridsdorfSubwayStop, handelskaiSubwayStop,
				GeoJSONCoordinate.create("16.39468", "48.24630"));
		RouteSegment subwayFromFloridsdorfToHandelskai = new RouteSegment().setNr(++segmentNr)
				.setFrom(floridsdorfSubwayStop).setTo(handelskaiSubwayStop).setDistanceMeters(2000)
				.setDurationSeconds(60 * 4).setStartTime("2016-01-01T15:20:30+01:00")
				.setEndTime("2016-01-01T15:24:30+01:00")
				.setIntermediateStops(Arrays.asList(createIntermediateStopNeueDonau()))
				.setModeOfTransport(ModeOfTransport.createMinimal(DetailedModeOfTransportType.SUBWAY)
						.setAccessibility(Sets.newHashSet(VehicleAccessibility.LOW_FLOOR_VEHICLE)).setService(serviceU6)
						.setOperator(wienerLinienOperator))
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(subwayFromFloridsdorfToHandelskai);

		// ### transfer from subway ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(handelskaiSubwayStop, handelskaiSubwayEntry);
		RouteSegment transferHandelskaiFromSubwayToExit = new RouteSegment().setNr(++segmentNr)
				.setFrom(handelskaiSubwayStop).setTo(handelskaiSubwayEntry).setDistanceMeters(40)
				// 3 minutes walking time
				.setDurationSeconds(60 * 3).setStartTime("2016-01-01T15:24:30+01:00")
				.setEndTime("2016-01-01T15:27:30+01:00").setModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
				.setAccessibility(Arrays.asList(Accessibility.ESCALATOR_DOWN, Accessibility.STAIRS_DOWN))
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(transferHandelskaiFromSubwayToExit);

		// ### walk from subway to station-based sharing (bike-sharing) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(handelskaiSubwayEntry, handelskaiCitybike,
				GeoJSONCoordinate.create("16.3845846", "48.2418792"));
		RouteSegment walkToCitybikeHandelskai = new RouteSegment().setNr(++segmentNr).setFrom(handelskaiSubwayEntry)
				.setTo(handelskaiCitybike).setDistanceMeters(57).setDurationSeconds(40)
				.setStartTime("2016-01-01T15:27:30+01:00").setEndTime("2016-01-01T15:28:10+01:00")
				.setModeOfTransport(ModeOfTransport.STANDARD_FOOT).setGeometryGeoJson(geometryGeoJson);
		segments.add(walkToCitybikeHandelskai);

		// ### ride station-based sharing (bike-sharing) - including rent &
		// return times ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(handelskaiCitybike, friedrichEngelsPlatzCitybike,
				GeoJSONCoordinate.create("16.3838145", "48.2413853"),
				GeoJSONCoordinate.create("16.3807639", "48.2442201"),
				GeoJSONCoordinate.create("16.3793906", "48.2438237"));
		// NOTE: example on how to add additional properties to the geometry be
		// used e.g. in Leaflet
		geometryGeoJson.getProperties().put("color", "#FFBBCC");
		geometryGeoJson.getProperties().put("weight", "7");
		geometryGeoJson.getProperties().put("opacity", "0.9");
		citybikeFromHandelskaiToFriedrichEngelsPlatz = new RouteSegment().setNr(++segmentNr).setFrom(handelskaiCitybike)
				.setTo(friedrichEngelsPlatzCitybike).setDistanceMeters(692)
				// 3 minutes ride, 2 minutes renting the bike, 1 minute
				// returning the bike
				.setDurationSeconds(60 * 6).setBoardingSeconds(60 * 2).setAlightingSeconds(60 * 1)
				.setStartTime("2016-01-01T15:28:10+01:00").setEndTime("2016-01-01T15:34:10+01:00")
				.setModeOfTransport(citybikeMot).setGeometryGeoJson(geometryGeoJson);
		segments.add(citybikeFromHandelskaiToFriedrichEngelsPlatz);

		// ### walk to free-floating sharing (car-sharing) - over stairs
		// (general info that there are stairs) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(friedrichEngelsPlatzCitybike, car2goPickup,
				GeoJSONCoordinate.create("16.37763", "48.24369"));
		RouteSegment walkToCar2go = new RouteSegment().setNr(++segmentNr).setFrom(friedrichEngelsPlatzCitybike)
				.setTo(car2goPickup).setDistanceMeters(150).setDurationSeconds(115)
				.setStartTime("2016-01-01T15:34:10+01:00").setEndTime("2016-01-01T15:36:05+01:00")
				.setAccessibility(Arrays.asList(Accessibility.STAIRS_UP))
				.setModeOfTransport(ModeOfTransport.STANDARD_FOOT).setGeometryGeoJson(geometryGeoJson);
		segments.add(walkToCar2go);

		// ### ride free-floating sharing (electric car-sharing) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(car2goPickup, adalbertStifterStrasse15,
				GeoJSONCoordinate.create("16.373601", "48.24218"));
		car2goAlongAdalbertStifterStrasse = new RouteSegment().setNr(++segmentNr).setFrom(car2goPickup)
				.setTo(adalbertStifterStrasse15).setDistanceMeters(692)
				// 4 minutes ride, 2 minutes renting the car, 1 minute parking
				// the car
				.setDurationSeconds(60 * 7).setBoardingSeconds(60 * 2).setAlightingSeconds(60 * 1)
				.setStartTime("2016-01-01T15:36:05+01:00").setEndTime("2016-01-01T15:43:05+01:00")
				.setModeOfTransport(ModeOfTransport.createMinimal(DetailedModeOfTransportType.CAR)
						.setSharingType(Sharing.FREE_FLOATING_VEHICLE_SHARING).setElectric(true)
						.setOperator(car2goOperator)
						// for now specific information goes as additional info
						.setAdditionalInfo(ImmutableMap.of("licensePlate", "W-123456", "fuelPercentage", "80",
								"interiorState", "good", "exteriorState", "unacceptable")))
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(car2goAlongAdalbertStifterStrasse);

		// ### ride private vehicle (bicycle) - and there is a park as potential
		// stop on the way ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(adalbertStifterStrasse15, treustrasse92,
				GeoJSONCoordinate.create("16.36515", "48.23729"), GeoJSONCoordinate.create("16.3656", "48.23515"),
				GeoJSONCoordinate.create("16.36288", "48.23509"));

		// add navigation instructions
		List<Instruction<?>> navigationInstructions = new ArrayList<>();
		navigationInstructions
				.add(BasicRoadInstruction.createMinimalRouteStart(adalbertStifterStrasse15.getSimpleCoordinate(),
						Optional.of("Adalbert-Stifter-Straße"), Optional.of(FormOfWay.ROAD)));
		navigationInstructions.add(BasicRoadInstruction
				.createMinimalOnRoute(GeoJSONCoordinate.create("16.3651564", "48.2372703"), TurnDirection.SLIGHT_LEFT,
						Optional.of("Klosterneuburger Straße"), Optional.of(FormOfWay.ROAD))
				.setLandmark(Landmark.createMinimal(Preposition.AFTER,
						PointOfInterest.createMinimal(GeoJSONCoordinate.create("16.3653027", "48.2374996"))
								.setName("Wiener Backstube").setPoiType("amenity=bakery"))));
		// there is no roundabout, but let's demonstrate this anyways
		navigationInstructions
				.add(RoundaboutInstruction.createMinimalEnter(GeoJSONCoordinate.create("16.36560", "48.23511"))
						.setExitNr(2).setRoundaboutStreetName("Rondeauplatz").setOntoStreetName("Leiziger Straße"));
		navigationInstructions
				.add(RoundaboutInstruction.createMinimalExit(GeoJSONCoordinate.create("16.36560", "48.23511"))
						.setOntoStreetName("Leipziger Straße").setOntoFormOfWay(FormOfWay.FOOT_AND_CYCLEPATH));
		navigationInstructions.add(BasicRoadInstruction
				.createMinimalOnRoute(GeoJSONCoordinate.create("16.36292", "48.23504"), TurnDirection.LEFT,
						Optional.of("Treustraße"), Optional.of(FormOfWay.ROAD))
				.setContinueMeters(110));
		navigationInstructions.add(BasicRoadInstruction.createMinimalRouteEnd(treustrasse92.getSimpleCoordinate(),
				Optional.of("Treustraße"), Optional.of(FormOfWay.ROAD)));

		bicycleFromAdalbertStifterStrasseToTreugasse = new RouteSegment().setNr(++segmentNr)
				.setFrom(adalbertStifterStrasse15).setTo(treustrasse92).setDistanceMeters(597)
				// 106 seconds ride, 1 minutes unlocking bike, 1 minute parking
				// & locking bike
				.setDurationSeconds(106 + 60 * 2).setBoardingSeconds(60 * 1).setAlightingSeconds(60 * 1)
				.setStartTime("2016-01-01T15:43:05+01:00").setEndTime("2016-01-01T15:46:51+01:00")
				.setIntermediateStops(Arrays.asList(IntermediateStop.createMinimal(antonKummererPark)))
				// additional info about vehicles provided in the request can be
				// added here (such as in project SMILE)
				.setAdditionalInfo(ImmutableMap.of("name", "Univega Mountainbike"))
				.setModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).setGeometryGeoJson(geometryGeoJson)
				.setNavigationInstructions(navigationInstructions);
		segments.add(bicycleFromAdalbertStifterStrasseToTreugasse);

		// ### ride-sharing (via car) ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(treustrasse92, gaussplatz,
				GeoJSONCoordinate.create("16.3644", "48.2311"), GeoJSONCoordinate.create("16.36638", "48.22886"));
		rideSharingFromTreugasseToGaussplatz = new RouteSegment().setNr(++segmentNr).setFrom(treustrasse92)
				.setTo(gaussplatz).setDistanceMeters(941)
				// 112 seconds ride, 3 minutes waiting for ride-sharing &
				// hopping on board
				.setDurationSeconds(112 + 60 * 3).setBoardingSeconds(60 * 3).setStartTime("2016-01-01T15:46:51+01:00")
				.setEndTime("2016-01-01T15:51:43+01:00")
				.setModeOfTransport(ModeOfTransport.createMinimal(DetailedModeOfTransportType.CAR)
						.setSharingType(Sharing.RIDE_SHARING).setOperator(flincOperator)
						// for now specific information goes as additional info
						.setAdditionalInfo(
								ImmutableMap.of("userName", "herbertWien78", "phoneNumber", "+43 650 7734343")))
				.setGeometryGeoJson(geometryGeoJson);
		segments.add(rideSharingFromTreugasseToGaussplatz);

		// ### private car ###
		geometryGeoJson = GeoJSONFeature.createLineStringFeature(gaussplatz, scholzgasse1Parking,
				GeoJSONCoordinate.create("16.3688", "48.2263"), GeoJSONCoordinate.create("16.3693", "48.2257"),
				GeoJSONCoordinate.create("16.3697", "48.2256"));
		carFromGaussplatzToScholzgasse = new RouteSegment().setNr(++segmentNr).setFrom(gaussplatz)
				.setTo(scholzgasse1Parking).setDistanceMeters(299)
				// 57 seconds ride
				// 1 minute unlocking/entering time
				// 5 minutes searching for a parking space
				.setDurationSeconds(57 + 60 * 6).setBoardingSeconds(60).setAlightingSeconds(60 * 5)
				.setStartTime("2016-01-01T15:51:43+01:00").setEndTime("2016-01-01T15:58:40+01:00")
				.setModeOfTransport(ModeOfTransport.STANDARD_CAR).setGeometryGeoJson(geometryGeoJson);
		segments.add(carFromGaussplatzToScholzgasse);

		return segments;
	}

	private GeoJSONFeatureCollection<GeoJSONLineString> createGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(
			GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
		GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = new GeoJSONFeatureCollection<>();
		GeoJSONFeature<GeoJSONLineString> lineStringFeature;

		lineStringFeature = GeoJSONFeature.createLineStringFeature(geometryGeoJson.getGeometry().subLineString(0, 2));
		lineStringFeature.getProperties().put("name", "Giefinggasse");
		lineStringFeature.getProperties().put("frc", "6");
		lineStringFeature.getProperties().put("edgeWeight", "54.1");
		geometryGeoJsonEdges.getFeatures().add(lineStringFeature);

		lineStringFeature = GeoJSONFeature.createLineStringFeature(geometryGeoJson.getGeometry().subLineString(1, 3));
		lineStringFeature.getProperties().put("name", "Siemensstraße");
		lineStringFeature.getProperties().put("frc", "2");
		lineStringFeature.getProperties().put("edgeWeight", "182.5");
		geometryGeoJsonEdges.getFeatures().add(lineStringFeature);

		lineStringFeature = GeoJSONFeature.createLineStringFeature(geometryGeoJson.getGeometry().subLineString(2, 4));
		lineStringFeature.getProperties().put("name", "Heinrich-von-Buol-Gasse");
		lineStringFeature.getProperties().put("frc", "3");
		lineStringFeature.getProperties().put("edgeWeight", "49.8");
		geometryGeoJsonEdges.getFeatures().add(lineStringFeature);

		return geometryGeoJsonEdges;
	}

	private IntermediateStop createIntermediateStopNeueDonau() {
		ZonedDateTime arrivalTime = ZonedDateTime.parse("2016-01-01T15:22:30+01:00");
		ZonedDateTime departureTime = arrivalTime.plus(60, ChronoUnit.SECONDS);
		return IntermediateStop.createMinimal(neueDonauSubwayStop).setPlannedArrivalTime(arrivalTime)
				.setPlannedDepartureTime(departureTime).setEstimatedArrivalTime(arrivalTime)
				.setEstimatedDepartureTime(departureTime);
	}

	public RouteSegment getFootSegment() {
		return walkToBusStopHeinrichVonBuolGasse;
	}

	public RouteSegment getPrivateBicycleSegmentWithNavigationInstructions() {
		return bicycleFromAdalbertStifterStrasseToTreugasse;
	}

	public RouteSegment getPrivateCarSegment() {
		return carFromGaussplatzToScholzgasse;
	}

	public RouteSegment getTransferToPublicTransportSegment() {
		return transferToBusHeinrichVonBuolGasse;
	}

	public RouteSegment getBusSegment() {
		return busFromHeinrichVonBuolGgasseToFloridsdorf;
	}

	public RouteSegment getBikeSharingSegment() {
		return citybikeFromHandelskaiToFriedrichEngelsPlatz;
	}

	public RouteSegment getCarSharingSegment() {
		return car2goAlongAdalbertStifterStrasse;
	}

	public RouteSegment getRideSharingSegment() {
		return rideSharingFromTreugasseToGaussplatz;
	}

}
