package at.ac.ait.sproute.routeformat.example;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import at.ac.ait.sproute.routeformat.RouteFormatRoot;
import at.ac.ait.sproute.routeformat.Sproute.Status;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

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

	public void writeExampleJson() throws JsonGenerationException, JsonMappingException, IOException {
		RouteFormatRoot root = new IntermodalRouteExample().getRouteFormatRoot();

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
