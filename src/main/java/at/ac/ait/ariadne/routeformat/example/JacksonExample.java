package at.ac.ait.ariadne.routeformat.example;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import at.ac.ait.ariadne.routeformat.Location;
import at.ac.ait.ariadne.routeformat.Route;
import at.ac.ait.ariadne.routeformat.RouteFormatRoot;
import at.ac.ait.ariadne.routeformat.RouteFormatRoot.Status;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.RouteSegment.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

/**
 * We use the "Data Binding" method of Jackson (to map POJOs to JSON).
 * 
 * Note: unfortunately the Jackson Schema generator does not export
 * "defaultValue=*" and does not allow specification of minItems, maxItems,..
 * Let's omit this information for now.
 * 
 * @author mstraub
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

	public void writeExampleJson() throws JsonGenerationException, JsonMappingException, IOException {
		LinkedList<CoordinatePoint> routeGeometry = new LinkedList<>();
		routeGeometry.add(new CoordinatePoint("16.4265263", "48.2686617"));
		routeGeometry.add(new CoordinatePoint("16.426593", "48.269826"));
		routeGeometry.add(new CoordinatePoint("16.4319054", "48.269724"));
		
		GeoJSONFeature<GeoJSONPoint> pointFeature = GeoJSONFeature.newPointFeature(routeGeometry.getFirst());
		Location giefinggasse = Location.newAddressLocation(pointFeature, "Austria", "Wien", "1210", "Giefinggasse", "2/4/S314"); 
		pointFeature = GeoJSONFeature.newPointFeature(routeGeometry.getLast());
		Location richardneutragasse = Location.newAddressLocation(pointFeature, "Austria", "Wien", "1210", "Richard-Neutra-Gasse", "6");
		
		int durationSeconds = 2*60 + 30;
		int lengthMeters = 570;
		ZonedDateTime departureTime = ZonedDateTime.parse("2015-01-01T10:15:30+01:00");
		ZonedDateTime arrivalTime = departureTime.plus(durationSeconds, ChronoUnit.SECONDS);
		
		Route route = new Route(giefinggasse, richardneutragasse, departureTime.toString(), arrivalTime.toString(), lengthMeters, durationSeconds);
		RouteSegment segment = new RouteSegment(1, giefinggasse, richardneutragasse, departureTime.toString(), arrivalTime.toString(), lengthMeters, durationSeconds, ModeOfTransport.BICYCLE);
		segment.geometryGeoJson = GeoJSONFeature.newLineStringFeature(routeGeometry);
		segment.geometryGeoJson.properties.put("color", "#FF11BB");
		segment.geometryGeoJson.properties.put("weight", 2);
		segment.geometryGeoJson.properties.put("opacity", "0.5");
		
		GeoJSONFeatureCollection<GeoJSONLineString> featureCollection = new GeoJSONFeatureCollection<>();
		GeoJSONFeature<GeoJSONLineString> giefinggasseFeature = GeoJSONFeature.newLineStringFeature(routeGeometry.subList(0, 2));
		giefinggasseFeature.properties.put("name", "Giefinggasse");
		giefinggasseFeature.properties.put("frc", "6");
		giefinggasseFeature.properties.put("edgeWeight", "123");
		featureCollection.features.add(giefinggasseFeature);
		GeoJSONFeature<GeoJSONLineString> paukerwerkgasseJson = GeoJSONFeature.newLineStringFeature(routeGeometry.subList(1, 3));
		paukerwerkgasseJson.properties.put("name", "Paukerwerkgasse");
		paukerwerkgasseJson.properties.put("frc", "6");
		paukerwerkgasseJson.properties.put("edgeWeight", "187");
		featureCollection.features.add(paukerwerkgasseJson);
		segment.geometryGeoJsonEdges = featureCollection;
		
		route.segments.add(segment);
		
		RoutingRequest request = new RoutingRequest();
		request.id = "999";
		request.from = giefinggasse;
		request.to = richardneutragasse;
		request.routes.add(route);
		
		RouteFormatRoot root = new RouteFormatRoot();
		root.setCalculationTimeNow();
		root.coordinateReferenceSystem = "EPSG:4326";
		root.status = Status.OK;
		root.debugMessage = "Route calculated in 0.002 seconds";
		root.request = request;
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		
		System.out.println(mapper.writeValueAsString(root));
		mapper.writeValue(new File(exampleFile), root);
	}
	
	public void readExampleJson() throws JsonParseException, JsonMappingException, IOException {
		// variant 1 - data binding
		ObjectMapper mapper = new ObjectMapper();
		RouteFormatRoot root = mapper.readValue(new File(exampleFile), RouteFormatRoot.class);
		Status status = root.status;
		List<BigDecimal> secondGeometryPointOfRoute = root.request.routes.get(0).segments.get(0).geometryGeoJson.geometry.coordinates.get(1);
		System.out.println(status);
		System.out.println(secondGeometryPointOfRoute);
		
		// variant 2 - tree model
		JsonNode rootNode = mapper.readValue(new File(exampleFile), JsonNode.class);
		System.out.println(rootNode.get("status").asText());
		System.out.println(rootNode.get("request").get("routes").get(0).get("segments").get(0).get("geometryGeoJson").get("geometry").get("coordinates").get(1));
	}
	
	public void writeSchema() throws JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		mapper.acceptJsonFormatVisitor(mapper.constructType(RouteFormatRoot.class), visitor);
		JsonSchema jsonSchema = visitor.finalSchema();
		jsonSchema.set$schema("http://json-schema.org/draft-04/schema#");
		
		System.out.println(mapper.writeValueAsString(jsonSchema));
		mapper.writeValue(new File(schemaFile), jsonSchema);
	}
	
}
