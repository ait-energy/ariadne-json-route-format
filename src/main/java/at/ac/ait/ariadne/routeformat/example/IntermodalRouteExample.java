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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.Constants.Accessibility;
import at.ac.ait.ariadne.routeformat.Constants.AccessibilityRestriction;
import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.Sharing;
import at.ac.ait.ariadne.routeformat.Constants.Speed;
import at.ac.ait.ariadne.routeformat.Constants.Status;
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
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONMultiPolygon;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Address;
import at.ac.ait.ariadne.routeformat.location.Location;
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
    private Location giefinggasseAit, heinrichVonBuolGasseBusStop, floridsdorfBusStop, floridsdorfSubwayStop,
            neueDonauSubwayStop, handelskaiSubwayStop, handelskaiSubwayEntry, handelskaiCitybike,
            friedrichEngelsPlatzCitybike, car2goPickup, adalbertStifterStrasse15, privateBicycleHopsagasse,
            antonKummererPark, treustrasse92, gaussplatz, scholzgasse1;
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
        wienerLinienOperator = Operator.builder().withName("Wiener Linien").withWebsite("http://www.wienerlinien.at")
                .withCustomerServiceEmail("post@wienerlinien.at")
                .withAdditionalInfo(ImmutableMap.of("email_ticketshop", "ticketshop@wienerlinien.at")).build();

        citybikeOperator = Operator.builder().withName("Citybike Wien").withWebsite("http://citybikewien.at")
                .withCustomerServiceEmail("kontakt@citybikewien.at").withCustomerServicePhone("0810 500 500")
                .withAddress(Address.builder().withStreetName("Litfaßstraße").withHouseNumber("6").withPostCode("1030")
                        .withCity("Wien").build())
                .build();

        car2goOperator = Operator.builder().withName("Car2Go").withWebsite("https://www.car2go.com/de/wien")
                .withCustomerServiceEmail("wien@car2go.com").build();

        flincOperator = Operator.builder().withName("Flinc").withWebsite("https://flinc.org").build();
    }

    private void initializeComplexModesOfTransport() {
        wienerLinienMot = ModeOfTransport.builder().withGeneralizedType(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT)
                .withOperator(wienerLinienOperator).build();

        citybikeMot = ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BICYCLE)
                .withSharingType(Sharing.STATION_BOUND_VEHICLE_SHARING).withOperator(citybikeOperator).build();

        car2goMot = ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.CAR)
                .withSharingType(Sharing.FREE_FLOATING_VEHICLE_SHARING).withElectric(true).withOperator(car2goOperator)
                .build();

        flincMot = ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.CAR)
                .withSharingType(Sharing.RIDE_SHARING).withOperator(flincOperator).build();
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

        neueDonauSubwayStop = PublicTransportStop.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.39468", "48.24630")))
                .withName("Neue Donau").withPlatform("2")
                .withRelatedLines(
                        ImmutableMap.of("20A", DetailedModeOfTransportType.BUS, "20B", DetailedModeOfTransportType.BUS))
                .build();

        handelskaiSubwayStop = PublicTransportStop.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.38541", "48.24173")))
                .withName("Handelskai").withPlatform("2").build();

        handelskaiSubwayEntry = PublicTransportStop.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3848877", "48.2416471")))
                .withName("Handelskai (Stationseingang)").build();

        handelskaiCitybike = SharingStation.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3847976", "48.2420356")))
                .withName("Millennium Tower").withId("2005")
                .withModesOfTransport(Arrays.asList(GeneralizedModeOfTransportType.BICYCLE))
                .withOperator(citybikeOperator)
                .withAdditionalInfo(ImmutableMap.of("capacity", "35", "bikes_available", "10", "boxes_available", "25"))
                .build();

        friedrichEngelsPlatzCitybike = SharingStation.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3792033", "48.2441354")))
                .withName("Friedrich Engels Platz").withId("2006")
                .withModesOfTransport(Arrays.asList(GeneralizedModeOfTransportType.BICYCLE))
                .withOperator(citybikeOperator)
                .withAdditionalInfo(ImmutableMap.of("capacity", "27", "bikes_available", "27", "boxes_available", "0"))
                .build();

        car2goPickup = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.377454", "48.24386")))
                .withAddress(Address.builder().withStreetName("Adalbert-Stifter-Straße").withHouseNumber("71").build())
                .build();

        adalbertStifterStrasse15 = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3655", "48.23752")))
                .withAddress(Address.builder().withStreetName("Adalbert-Stifter-Straße").withHouseNumber("15").build())
                .build();

        privateBicycleHopsagasse = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3715916", "48.246609")))
                .withAddress(Address.builder().withStreetName("Hopsagasse").withHouseNumber("5").withPostCode("1200")
                        .build())
                .build();

        antonKummererPark = PointOfInterest.builder().withPoiType("park").withName("Anton-Kummerer-Park")
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.364074", "48.2350109"))).build();

        treustrasse92 = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.36329", "48.234077")))
                .withAddress(Address.builder().withStreetName("Treustraße").withHouseNumber("92").withPostCode("1200")
                        .build())
                .build();

        gaussplatz = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.369045", "48.2267")))
                .withAddress(Address.builder().withStreetName("Gaußplatz").build()).build();

        scholzgasse1 = Location.builder()
                .withCoordinate(GeoJSONFeature.newPointFeature(new CoordinatePoint("16.3695", "48.2243")))
                .withAddress(Address.builder().withStreetName("Scholzgasse").withHouseNumber("1").withPostCode("1020")
                        .build())
                .build();
    }

    private void initializePublicTransportServices() {
        service28A = Service.builder().withName("28A").withTowards("Floridsdorf").build();
        serviceU6 = Service.builder().withName("U6").withTowards("Siebenhirten").withColor("#bf7700").build();
    }

    /**
     * @return a cached instance of an intermodal showcase-route
     */
    public RouteFormatRoot getRouteFormatRoot() {
        return root;
    }

    private RouteFormatRoot createRouteFormatRoot() throws JsonGenerationException, JsonMappingException, IOException {
        return RouteFormatRoot.builder().withRouteFormatVersion("0.14-SNAPSHOT").withRequestId("999")
                .withProcessedTimeNow().withStatus(Status.OK).withDebugMessage("Route calculated in 0.002 seconds")
                .withCoordinateReferenceSystem("EPSG:4326").withRequest(createRoutingRequest())
                .withRoutes(
                        Arrays.asList(Route.builder().withSegmentsAndAutomaticallyInferredFields(createRouteSegments())
                                .withOptimizedFor("travel time and low costs").build()))
                .build();
    }

    private RoutingRequest createRoutingRequest() {
        Map<String, Object> additionalInfoRouteRequest = new HashMap<>();
        additionalInfoRouteRequest.put("ait:additionalTestBigDecimal", new BigDecimal("12.34567"));
        additionalInfoRouteRequest.put("ait:additionalTestObject", wienerLinienOperator);
        additionalInfoRouteRequest.put("ait:additionalTestList", Lists.newArrayList(1, 2, 3, 4, 5, 6, 7));
        additionalInfoRouteRequest.put("ait:additionalTestString", "hello this is a String");

        GeoJSONFeature<GeoJSONPolygon> forbiddenPolygon = GeoJSONFeature
                .newPolygonFeatureFromCoordinatePoints(Arrays.asList(Arrays.asList(new CoordinatePoint("16", "48"),
                        new CoordinatePoint("16.1", "48"), new CoordinatePoint("16.1", "48.1"),
                        new CoordinatePoint("16.1", "48"), new CoordinatePoint("16", "48"))));
        GeoJSONFeature<GeoJSONMultiPolygon> forbiddenAreas = GeoJSONFeature
                .newMultiPolygonFeatureFromPolygons(Arrays.asList(forbiddenPolygon));

        List<RequestModeOfTransport> requestModes = new ArrayList<>();
        requestModes.add(RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_FOOT)
                .withMaximumDistanceMeters(2000).withSpeed(Speed.FAST.name()).withForbiddenAreas(forbiddenAreas)
                .build());
        requestModes.add(RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_BICYCLE)
                .withMaximumTravelTimeSeconds(45 * 60).withSpeed(Speed.SLOW.name())
                .withLocations(Arrays.asList(adalbertStifterStrasse15, privateBicycleHopsagasse)).build());
        requestModes
                .add(RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_MOTORCYCLE).build());
        RequestModeOfTransport carMot = RequestModeOfTransport.builder()
                .withModeOfTransport(ModeOfTransport.STANDARD_CAR).withLocations(Arrays.asList(treustrasse92)).build();
        requestModes.add(carMot);
        requestModes.add(RequestPTModeOfTransport.builder().withModeOfTransport(wienerLinienMot)
                .withExcludedPublicTransportModes(Arrays.asList(DetailedModeOfTransportType.AERIALWAY,
                        DetailedModeOfTransportType.AIRPLANE, DetailedModeOfTransportType.SHIP))
                .build());
        requestModes.add(RequestModeOfTransport.builder().withModeOfTransport(citybikeMot).build());
        requestModes.add(RequestModeOfTransport.builder().withModeOfTransport(car2goMot).build());
        requestModes.add(RequestModeOfTransport.builder().withModeOfTransport(flincMot).build());

        return RoutingRequest.builder().withServiceId("ariadne_webservice_vienna").withFrom(giefinggasseAit)
                .withTo(scholzgasse1).withDepartureTime("2016-01-01T15:00:00+01:00").withLanguage("DE")
                .withAccessibilityRestrictions(Arrays.asList(AccessibilityRestriction.NO_ELEVATOR))
                .withModesOfTransport(requestModes).withOptimizedFor("traveltime").withMaximumTransfers(10)
                .withEndModeOfTransport(carMot).withAdditionalInfo(additionalInfoRouteRequest).build();
    }

    private LinkedList<RouteSegment> createRouteSegments() {
        LinkedList<RouteSegment> segments = new LinkedList<>();
        GeoJSONFeature<GeoJSONLineString> geometryGeoJson;

        int segmentNr = 0;

        // ### walk to bus ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(giefinggasseAit, heinrichVonBuolGasseBusStop,
                new CoordinatePoint("16.4263", "48.2682"), new CoordinatePoint("16.42824", "48.26719"));
        // NOTE: example on how to add geometry for single edges
        GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = createGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(
                geometryGeoJson);
        walkToBusStopHeinrichVonBuolGasse = RouteSegment.builder().withNr(++segmentNr).withFrom(giefinggasseAit)
                .withTo(heinrichVonBuolGasseBusStop).withDistanceMeters(200).withDurationSeconds(60)
                .withStartTime("2016-01-01T15:00:00+01:00").withEndTime("2016-01-01T15:01:00+01:00")
                .withModeOfTransport(ModeOfTransport.STANDARD_FOOT).withGeometryGeoJson(geometryGeoJson)
                .withGeometryGeoJsonEdges(geometryGeoJsonEdges).build();
        segments.add(walkToBusStopHeinrichVonBuolGasse);

        // ### wait for bus ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(heinrichVonBuolGasseBusStop, heinrichVonBuolGasseBusStop);
        transferToBusHeinrichVonBuolGasse = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(heinrichVonBuolGasseBusStop).withTo(heinrichVonBuolGasseBusStop).withDistanceMeters(0)
                // 5 minutes waiting (=boarding) time
                .withDurationSeconds(60 * 5).withBoardingSeconds(60 * 5).withStartTime("2016-01-01T15:01:00+01:00")
                .withEndTime("2016-01-01T15:06:00+01:00").withModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(transferToBusHeinrichVonBuolGasse);

        // ### ride bus (bus is not wheelchair accessible!) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(heinrichVonBuolGasseBusStop, floridsdorfBusStop,
                new CoordinatePoint("16.42354", "48.26306"), new CoordinatePoint("16.4236", "48.2621"),
                new CoordinatePoint("16.4044", "48.2576"), new CoordinatePoint("16.40305", "48.25621"),
                new CoordinatePoint("16.40127", "48.25698"));
        busFromHeinrichVonBuolGgasseToFloridsdorf = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(heinrichVonBuolGasseBusStop).withTo(floridsdorfBusStop).withDistanceMeters(2500)
                .withDurationSeconds(60 * 10 + 30).withStartTime("2016-01-01T15:06:00+01:00")
                .withEndTime("2016-01-01T15:16:30+01:00")
                .withModeOfTransport(ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BUS)
                        .withAccessibility(Arrays.asList(VehicleAccessibility.HIGH_FLOOR_VEHICLE))
                        .withService(service28A).withOperator(wienerLinienOperator).build())
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(busFromHeinrichVonBuolGgasseToFloridsdorf);

        // ### transfer from bus to subway and wait for subway (walk down
        // stairs) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(floridsdorfBusStop, floridsdorfSubwayStop);
        RouteSegment transferFloridsdorfFromBusToSubway = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(floridsdorfBusStop).withTo(floridsdorfSubwayStop).withDistanceMeters(40)
                // 1 minute walking time + 3 minutes waiting (=boarding) time
                .withDurationSeconds(60 * 4).withBoardingSeconds(60 * 3).withStartTime("2016-01-01T15:16:30+01:00")
                .withEndTime("2016-01-01T15:20:30+01:00").withModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
                .withAccessibility(Arrays.asList(Accessibility.STAIRS_DOWN)).withGeometryGeoJson(geometryGeoJson)
                .build();
        segments.add(transferFloridsdorfFromBusToSubway);

        // ### ride subway (wheelchair accessible!) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(floridsdorfSubwayStop, handelskaiSubwayStop,
                new CoordinatePoint("16.39468", "48.24630"));
        RouteSegment subwayFromFloridsdorfToHandelskai = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(floridsdorfSubwayStop).withTo(handelskaiSubwayStop).withDistanceMeters(2000)
                .withDurationSeconds(60 * 4).withStartTime("2016-01-01T15:20:30+01:00")
                .withEndTime("2016-01-01T15:24:30+01:00")
                .withIntermediateStops(Arrays.asList(createIntermediateStopNeueDonau()))
                .withModeOfTransport(ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.SUBWAY)
                        .withAccessibility(Arrays.asList(VehicleAccessibility.LOW_FLOOR_VEHICLE)).withService(serviceU6)
                        .withOperator(wienerLinienOperator).build())
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(subwayFromFloridsdorfToHandelskai);

        // ### transfer from subway ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(handelskaiSubwayStop, handelskaiSubwayEntry);
        RouteSegment transferHandelskaiFromSubwayToExit = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(handelskaiSubwayStop).withTo(handelskaiSubwayEntry).withDistanceMeters(40)
                // 3 minutes walking time
                .withDurationSeconds(60 * 3).withStartTime("2016-01-01T15:24:30+01:00")
                .withEndTime("2016-01-01T15:27:30+01:00").withModeOfTransport(ModeOfTransport.STANDARD_TRANSFER)
                .withAccessibility(Arrays.asList(Accessibility.ESCALATOR_DOWN, Accessibility.STAIRS_DOWN))
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(transferHandelskaiFromSubwayToExit);

        // ### walk from subway to station-based sharing (bike-sharing) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(handelskaiSubwayEntry, handelskaiCitybike,
                new CoordinatePoint("16.3845846", "48.2418792"));
        RouteSegment walkToCitybikeHandelskai = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(handelskaiSubwayEntry).withTo(handelskaiCitybike).withDistanceMeters(57)
                .withDurationSeconds(40).withStartTime("2016-01-01T15:27:30+01:00")
                .withEndTime("2016-01-01T15:28:10+01:00")
                .withModeOfTransport(
                        ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT).build())
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(walkToCitybikeHandelskai);

        // ### ride station-based sharing (bike-sharing) - including rent &
        // return times ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(handelskaiCitybike, friedrichEngelsPlatzCitybike,
                new CoordinatePoint("16.3838145", "48.2413853"), new CoordinatePoint("16.3807639", "48.2442201"),
                new CoordinatePoint("16.3793906", "48.2438237"));
        // NOTE: example on how to add additional properties to the geometry be
        // used e.g. in Leaflet
        geometryGeoJson.properties.put("color", "#FFBBCC");
        geometryGeoJson.properties.put("weight", "7");
        geometryGeoJson.properties.put("opacity", "0.9");
        citybikeFromHandelskaiToFriedrichEngelsPlatz = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(handelskaiCitybike).withTo(friedrichEngelsPlatzCitybike).withDistanceMeters(692)
                // 3 minutes ride, 2 minutes renting the bike, 1 minute
                // returning the bike
                .withDurationSeconds(60 * 6).withBoardingSeconds(60 * 2).withAlightingSeconds(60 * 1)
                .withStartTime("2016-01-01T15:28:10+01:00").withEndTime("2016-01-01T15:34:10+01:00")
                .withModeOfTransport(citybikeMot).withGeometryGeoJson(geometryGeoJson).build();
        segments.add(citybikeFromHandelskaiToFriedrichEngelsPlatz);

        // ### walk to free-floating sharing (car-sharing) - over stairs
        // (general info that there are stairs) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(friedrichEngelsPlatzCitybike, car2goPickup,
                new CoordinatePoint("16.37763", "48.24369"));
        RouteSegment walkToCar2go = RouteSegment.builder().withNr(++segmentNr).withFrom(friedrichEngelsPlatzCitybike)
                .withTo(car2goPickup).withDistanceMeters(150).withDurationSeconds(115)
                .withStartTime("2016-01-01T15:34:10+01:00").withEndTime("2016-01-01T15:36:05+01:00")
                .withAccessibility(Arrays.asList(Accessibility.STAIRS_UP))
                .withModeOfTransport(ModeOfTransport.STANDARD_FOOT).withGeometryGeoJson(geometryGeoJson).build();
        segments.add(walkToCar2go);

        // ### ride free-floating sharing (electric car-sharing) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(car2goPickup, adalbertStifterStrasse15,
                new CoordinatePoint("16.373601", "48.24218"));
        car2goAlongAdalbertStifterStrasse = RouteSegment.builder().withNr(++segmentNr).withFrom(car2goPickup)
                .withTo(adalbertStifterStrasse15).withDistanceMeters(692)
                // 4 minutes ride, 2 minutes renting the car, 1 minute parking
                // the car
                .withDurationSeconds(60 * 7).withBoardingSeconds(60 * 2).withAlightingSeconds(60 * 1)
                .withStartTime("2016-01-01T15:36:05+01:00").withEndTime("2016-01-01T15:43:05+01:00")
                .withModeOfTransport(ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.CAR)
                        .withSharingType(Sharing.FREE_FLOATING_VEHICLE_SHARING).withElectric(true)
                        .withOperator(car2goOperator)
                        // for now specific information goes as additional info
                        .withAdditionalInfo(ImmutableMap.of("licensePlate", "W-123456", "fuelPercentage", "80",
                                "interiorState", "good", "exteriorState", "unacceptable"))
                        .build())
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(car2goAlongAdalbertStifterStrasse);

        // ### ride private vehicle (bicycle) - and there is a park as potential
        // stop on the way ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(adalbertStifterStrasse15, treustrasse92,
                new CoordinatePoint("16.36515", "48.23729"), new CoordinatePoint("16.3656", "48.23515"),
                new CoordinatePoint("16.36288", "48.23509"));
        bicycleFromAdalbertStifterStrasseToTreugasse = RouteSegment.builder().withNr(++segmentNr)
                .withFrom(adalbertStifterStrasse15).withTo(treustrasse92).withDistanceMeters(597)
                // 106 seconds ride, 1 minutes unlocking bike, 1 minute parking
                // & locking bike
                .withDurationSeconds(106 + 60 * 2).withBoardingSeconds(60 * 1).withAlightingSeconds(60 * 1)
                .withStartTime("2016-01-01T15:43:05+01:00").withEndTime("2016-01-01T15:46:51+01:00")
                .withIntermediateStops(Arrays.asList(IntermediateStop.builder().withStop(antonKummererPark).build()))
                // additional info about vehicles provided in the request can be
                // added here (such as in project SMILE)
                .withAdditionalInfo(ImmutableMap.of("name", "Univega Mountainbike"))
                .withModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).withGeometryGeoJson(geometryGeoJson).build();
        segments.add(bicycleFromAdalbertStifterStrasseToTreugasse);

        // ### ride-sharing (via car) ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(treustrasse92, gaussplatz,
                new CoordinatePoint("16.3644", "48.2311"), new CoordinatePoint("16.36638", "48.22886"));
        rideSharingFromTreugasseToGaussplatz = RouteSegment.builder().withNr(++segmentNr).withFrom(treustrasse92)
                .withTo(gaussplatz).withDistanceMeters(941)
                // 112 seconds ride, 3 minutes waiting for ride-sharing &
                // hopping on board
                .withDurationSeconds(112 + 60 * 3).withBoardingSeconds(60 * 3)
                .withStartTime("2016-01-01T15:46:51+01:00").withEndTime("2016-01-01T15:51:43+01:00")
                .withModeOfTransport(ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.CAR)
                        .withSharingType(Sharing.RIDE_SHARING).withOperator(flincOperator)
                        // for now specific information goes as additional info
                        .withAdditionalInfo(
                                ImmutableMap.of("userName", "herbertWien78", "phoneNumber", "+43 650 7734343"))
                        .build())
                .withGeometryGeoJson(geometryGeoJson).build();
        segments.add(rideSharingFromTreugasseToGaussplatz);

        // ### private car ###
        geometryGeoJson = GeoJSONFeature.newLineStringFeature(gaussplatz, scholzgasse1,
                new CoordinatePoint("16.3688", "48.2263"), new CoordinatePoint("16.3693", "48.2257"),
                new CoordinatePoint("16.3697", "48.2256"));
        carFromGaussplatzToScholzgasse = RouteSegment.builder().withNr(++segmentNr).withFrom(gaussplatz)
                .withTo(scholzgasse1).withDistanceMeters(299)
                // 57 seconds ride
                // 1 minute unlocking/entering time
                // 5 minutes searching for a parking space
                .withDurationSeconds(57 + 60 * 6).withBoardingSeconds(60).withAlightingSeconds(60 * 5)
                .withStartTime("2016-01-01T15:51:43+01:00").withEndTime("2016-01-01T15:58:40+01:00")
                .withModeOfTransport(ModeOfTransport.STANDARD_CAR).withGeometryGeoJson(geometryGeoJson).build();
        segments.add(carFromGaussplatzToScholzgasse);

        return segments;
    }

    private GeoJSONFeatureCollection<GeoJSONLineString> createGeometryGeoJsonEdgesFromGiefinggasseToHeinrichVonBuolGasse(
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

    private IntermediateStop createIntermediateStopNeueDonau() {
        ZonedDateTime arrivalTime = ZonedDateTime.parse("2016-01-01T15:22:30+01:00");
        ZonedDateTime departureTime = arrivalTime.plus(60, ChronoUnit.SECONDS);
        return IntermediateStop.builder().withStop(neueDonauSubwayStop).withPlannedArrivalTime(arrivalTime)
                .withPlannedDepartureTime(departureTime).withEstimatedArrivalTime(arrivalTime)
                .withEstimatedDepartureTime(departureTime).build();
    }

    public RouteSegment getFootSegment() {
        return walkToBusStopHeinrichVonBuolGasse;
    }

    public RouteSegment getPrivateBicycleSegment() {
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

    // TODO add exemplary navigation instructions
    // List<Instruction> navigationInstructions = new ArrayList<>();
    // navigationInstructions.add(BasicRoadInstruction
    // .builder()
    // .forRouteEnd(routeGeometry.getFirst(), Optional.of("Hügelweg"),
    // Optional.of(FormOfWay.PEDESTRIAN_ZONE),
    // Optional.empty()).build());
    // navigationInstructions.add(RoundaboutInstruction.enterBuilder(routeGeometry.getFirst())
    // .withOntoStreetName("Bergstraße").withOntoFormOfWay(FormOfWay.CYCLEPATH).withExitNr(3).build());
}
