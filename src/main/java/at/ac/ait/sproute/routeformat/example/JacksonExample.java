package at.ac.ait.sproute.routeformat.example;

import java.io.File;
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

import at.ac.ait.sproute.routeformat.Route;
import at.ac.ait.sproute.routeformat.RouteFormatRoot;
import at.ac.ait.sproute.routeformat.RouteSegment;
import at.ac.ait.sproute.routeformat.RoutingRequest;
import at.ac.ait.sproute.routeformat.Service;
import at.ac.ait.sproute.routeformat.ServiceProvider;
import at.ac.ait.sproute.routeformat.Sproute.FormOfWay;
import at.ac.ait.sproute.routeformat.Sproute.ModeOfTransport;
import at.ac.ait.sproute.routeformat.Sproute.RouteType;
import at.ac.ait.sproute.routeformat.Sproute.Status;
import at.ac.ait.sproute.routeformat.Sproute.VehicleType;
import at.ac.ait.sproute.routeformat.Vehicle;
import at.ac.ait.sproute.routeformat.geojson.CoordinatePoint;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.sproute.routeformat.instruction.BasicRoadInstruction;
import at.ac.ait.sproute.routeformat.instruction.Instruction;
import at.ac.ait.sproute.routeformat.instruction.RoundaboutInstruction;
import at.ac.ait.sproute.routeformat.location.Address;
import at.ac.ait.sproute.routeformat.location.Location;
import at.ac.ait.sproute.routeformat.location.PointOfInterest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * We use the "Data Binding" method of Jackson (to map POJOs to JSON).
 * <p>
 * <b>Note:</b> unfortunately the Jackson Schema generator does not export "defaultValue=*" and does not allow
 * specification of minItems, maxItems,.. Let's omit this information for now.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class JacksonExample {

	public static final String schemaFile = "src/main/resources/routeformat-schema.json";
	public static final String exampleFile = "src/main/resources/routeformat-example.json";

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		JacksonExample main = new JacksonExample();
		main.writeExampleJson();
		main.readExampleJson();
		main.writeSchema();
	}

	public static RouteFormatRoot getExampleRouteFormatRoot() throws JsonGenerationException, JsonMappingException,
			IOException {
		LinkedList<CoordinatePoint> routeGeometry = new LinkedList<>();
		routeGeometry.add(new CoordinatePoint("16.4265263", "48.2686617"));
		routeGeometry.add(new CoordinatePoint("16.426593", "48.269826"));
		routeGeometry.add(new CoordinatePoint("16.4319054", "48.269724"));

		GeoJSONFeature<GeoJSONPoint> coordinate = GeoJSONFeature.newPointFeature(routeGeometry.getFirst());
		Address giefinggasse = Address.builder().withCountry("Austria").withCity("Wien").withPostCode("1210")
				.withStreetName("Giefinggasse").withHouseNumber("2b").build();

		Location giefinggasseLocation = Location.builder().withCoordinate(coordinate).withAddress(giefinggasse).build();
		PointOfInterest giefinggassePoi = PointOfInterest.builder().withCoordinate(coordinate)
				.withAddress(giefinggasse).withName("AIT").withPoiType("company").build();
		coordinate = GeoJSONFeature.newPointFeature(routeGeometry.getLast());
		Address richardneutragasse = Address.builder().withStreetName("Richard-Neutra-Gasse").withHouseNumber("6")
				.build();
		Location richardneutragasseLocation = Location.builder().withCoordinate(coordinate)
				.withAddress(richardneutragasse).build();

		int durationSeconds = 2 * 60 + 30;
		int lengthMeters = 570;
		ZonedDateTime departureTime = ZonedDateTime.parse("2015-01-01T10:15:30+01:00");
		ZonedDateTime arrivalTime = departureTime.plus(durationSeconds, ChronoUnit.SECONDS);

		GeoJSONFeature<GeoJSONLineString> geometryGeoJson = GeoJSONFeature.newLineStringFeature(routeGeometry);
		geometryGeoJson = GeoJSONFeature.newLineStringFeature(routeGeometry);
		geometryGeoJson.properties.put("color", "#FF11BB");
		geometryGeoJson.properties.put("weight", 2);
		geometryGeoJson.properties.put("opacity", "0.5");

		GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges = new GeoJSONFeatureCollection<>();
		GeoJSONFeature<GeoJSONLineString> giefinggasseFeature = GeoJSONFeature.newLineStringFeature(routeGeometry
				.subList(0, 2));
		giefinggasseFeature.properties.put("name", "Giefinggasse");
		giefinggasseFeature.properties.put("frc", "6");
		giefinggasseFeature.properties.put("edgeWeight", "123");
		geometryGeoJsonEdges.features.add(giefinggasseFeature);
		GeoJSONFeature<GeoJSONLineString> paukerwerkgasseJson = GeoJSONFeature.newLineStringFeature(routeGeometry
				.subList(1, 3));
		paukerwerkgasseJson.properties.put("name", "Paukerwerkgasse");
		paukerwerkgasseJson.properties.put("frc", "6");
		paukerwerkgasseJson.properties.put("edgeWeight", "187");
		geometryGeoJsonEdges.features.add(paukerwerkgasseJson);

		List<Instruction> navigationInstructions = new ArrayList<>();
		navigationInstructions.add(BasicRoadInstruction
				.builder()
				.forRouteEnd(routeGeometry.getFirst(), Optional.of("Hügelweg"), Optional.of(FormOfWay.PEDESTRIAN_ZONE),
						Optional.empty()).build());
		navigationInstructions.add(RoundaboutInstruction.enterBuilder(routeGeometry.getFirst())
				.withOntoStreetName("Bergstraße").withOntoFormOfWay(FormOfWay.CYCLEPATH).withExitNr(3).build());

		ServiceProvider wienerLinienProvider = ServiceProvider
				.builder()
				.withName("Wiener Linien")
				.withWebsite("http://www.wienerlinien.at")
				.withAdditionalInfo(
						ImmutableMap.of("email", "post@wienerlinien.at", "email_ticketshop",
								"ticketshop@wienerlinien.at")).build();

		Service service28A = Service.builder().withName("28A").withDirection("Floridsdorf")
				.withProvider(wienerLinienProvider).build();

		Vehicle vehicle28A = Vehicle.builder().withType(VehicleType.BUS).withService(service28A).build();

		RouteSegment segment = RouteSegment.builder().withNr(1).withFrom(giefinggassePoi)
				.withTo(richardneutragasseLocation).withDepartureTime(departureTime).withArrivalTime(arrivalTime)
				.withLengthMeters(lengthMeters).withDurationSeconds(durationSeconds)
				.withModeOfTransport(ModeOfTransport.BICYCLE).withGeometryGeoJson(geometryGeoJson)
				.withGeometryGeoJsonEdges(geometryGeoJsonEdges).withNavigationInstructions(navigationInstructions)
				.build();

		RouteSegment busSegment = RouteSegment.builder().withNr(1).withFrom(giefinggasseLocation)
				.withTo(richardneutragasseLocation).withDepartureTime(departureTime).withArrivalTime(arrivalTime)
				.withLengthMeters(lengthMeters).withDurationSeconds(durationSeconds)
				.withModeOfTransport(ModeOfTransport.PUBLIC_TRANSPORT).withGeometryGeoJson(geometryGeoJson)
				.withGeometryGeoJsonEdges(geometryGeoJsonEdges).withVehicle(vehicle28A).build();

		Route route = Route.builder().withFrom(giefinggasseLocation).withTo(richardneutragasseLocation)
				.withDepartureTime(departureTime).withLengthMeters(lengthMeters).withDurationSeconds(durationSeconds)
				.withSegments(Arrays.asList(segment, busSegment)).withType(RouteType.BICYCLE_AND_PUBLIC_TRANSPORT)
				.build();

		Map<String, Object> additionalInfoRouteRequest = new HashMap<>();
		additionalInfoRouteRequest.put("ait:additionalBigDecimal", new BigDecimal("12.34567"));
		additionalInfoRouteRequest.put("ait:additionalObject", wienerLinienProvider);
		additionalInfoRouteRequest.put("ait:additionalList", Lists.newArrayList(1, 2, 3));
		additionalInfoRouteRequest.put("ait:additionalString", "hello this is a String");

		RoutingRequest request = RoutingRequest.builder().withServiceId("OSM_test").withFrom(giefinggasseLocation)
				.withTo(richardneutragasseLocation).withModesOfTransport(Sets.newHashSet(ModeOfTransport.BICYCLE))
				.withOptimizedFor("traveltime").withAdditionalInfo(additionalInfoRouteRequest).build();

		RouteFormatRoot root = RouteFormatRoot.builder().withRouteFormatVersion("0.10-SNAPSHOT").withRequestId("999")
				.withProcessedTimeNow().withStatus(Status.OK).withDebugMessage("Route calculated in 0.002 seconds")
				.withCoordinateReferenceSystem("EPSG:4326").withRequest(request).withRoutes(Arrays.asList(route))
				.build();

		return root;
	}

	public void writeExampleJson() throws JsonGenerationException, JsonMappingException, IOException {
		RouteFormatRoot root = getExampleRouteFormatRoot();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		System.out.println(mapper.writeValueAsString(root));
		mapper.writeValue(new File(exampleFile), root);
	}

	public void readExampleJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		// variant 1 - data binding
		RouteFormatRoot root = mapper.readValue(new File(exampleFile), RouteFormatRoot.class);
		Status status = root.getStatus();
		List<BigDecimal> secondGeometryPointOfRoute = root.getRoutes().get(0).getSegments().get(0).getGeometryGeoJson()
				.get().geometry.coordinates.get(1);
		System.out.println(status);
		System.out.println(secondGeometryPointOfRoute);
		System.out.println(root.getProcessedTime());

		// variant 2 - tree model
		JsonNode rootNode = mapper.readValue(new File(exampleFile), JsonNode.class);
		System.out.println(rootNode.get("status").asText());
		System.out.println(rootNode.get("routes").get(0).get("segments").get(0).get("geometryGeoJson").get("geometry")
				.get("coordinates").get(1));
		System.out.println(rootNode.get("calculationTime"));
	}

	public void writeSchema() throws JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		mapper.acceptJsonFormatVisitor(mapper.constructType(RouteFormatRoot.class), visitor);
		JsonSchema jsonSchema = visitor.finalSchema();
		jsonSchema.set$schema("http://json-schema.org/draft-04/schema#");

		System.out.println(mapper.writeValueAsString(jsonSchema));
		mapper.writeValue(new File(schemaFile), jsonSchema);
	}

}
