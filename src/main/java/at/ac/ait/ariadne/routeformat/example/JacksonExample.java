package at.ac.ait.ariadne.routeformat.example;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import at.ac.ait.ariadne.routeformat.Constants.Status;
import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.RoutingResponse;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.instruction.Instruction;
import scala.Option;

/**
 * We use the "Data Binding" method of Jackson (to map POJOs to JSON).
 * <p>
 * <b>Note:</b>
 * <ul>
 * <li>unfortunately the Jackson v3 Schema generator did not export
 * "defaultValue=*" and does not allow specification of minItems, maxItems,.. so
 * we did not add this information. Maybe let's do that in the future.</li>
 * <li>The v4 schema export is not working yet</li>
 * </ul>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class JacksonExample {

    public static final String schema3File = "src/main/resources/ariadne-json-route-format_schema_v3.json";
    public static final String schema4File = "src/main/resources/ariadne-json-route-format_schema_v4.json";
    public static final String exampleFile = "src/main/resources/ariadne-json-route-format_example.json";
    public static final String exampleUnknownAttributesFile = "src/main/resources/operator-with-unknown-fields.json";

    private ObjectMapper mapper;

    public JacksonExample() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
        JacksonExample main = new JacksonExample();
        main.writeExampleJson();
        main.readExampleJson();
        main.writeSchemav3();
        // main.writeSchemav4();
        main.readFileWithUnkownAttributes();
    }

    public void writeExampleJson() throws JsonGenerationException, JsonMappingException, IOException {
        RoutingResponse root = new IntermodalRouteExample().getRoutingResponse();

        System.out.println(mapper.writeValueAsString(root));
        System.out.println("##########");
        mapper.writeValue(new File(exampleFile), root);
    }

    public void readExampleJson() throws JsonParseException, JsonMappingException, IOException {
        // variant 1 - data binding
        RoutingResponse root = mapper.readValue(new File(exampleFile), RoutingResponse.class);
        Status status = root.getStatus();
        GeoJSONCoordinate secondGeometryPointOfRoute = root.getRoutes().get(0).getSegments().get(0).getGeometryGeoJson()
                .get().getGeometry().getCoordinates().get(1);
        System.out.println(status);
        System.out.println(secondGeometryPointOfRoute);
        System.out.println(root.getProcessedTime());
        System.out.println("##########");

        // variant 2 - tree model
        JsonNode rootNode = mapper.readValue(new File(exampleFile), JsonNode.class);
        System.out.println(rootNode.get("status").asText());
        System.out.println(rootNode.get("routes").get(0).get("segments").get(0).get("geometryGeoJson").get("geometry")
                .get("coordinates").get(1));
        System.out.println(rootNode.get("calculationTime"));
        System.out.println("##########");
    }

    public void readFileWithUnkownAttributes() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Operator operator = mapper.readValue(new File(exampleUnknownAttributesFile), Operator.class);
        System.out.println(operator);
        System.out.println("##########");
    }

    public void writeSchemav3() throws JsonGenerationException, IOException {
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        mapper.acceptJsonFormatVisitor(mapper.constructType(RoutingResponse.class), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();
        // jsonSchema.set$schema("http://json-schema.org/draft-04/schema#");

        System.out.println(mapper.writeValueAsString(jsonSchema));
        System.out.println("##########");
        mapper.writeValue(new File(schema3File), jsonSchema);
    }

    public void writeSchemav4() throws JsonGenerationException, IOException {
        // FIXME not working yet, Wrong class error
        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);
        Option<String> emptyOption = Option.empty();
        JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(Instruction.class, emptyOption, emptyOption);

        System.out.println(mapper.writeValueAsString(jsonSchema));
        System.out.println("##########");
        mapper.writeValue(new File(schema4File), jsonSchema);

        // FIXME maybe use JJ library instead?
        // SchemaMapper schemaMapper = new SchemaMapper();
        // JSONObject schema =
        // schemaMapper.toJsonSchema4(GeoJSONFeatureCollection.class, true);
        //// JSONWriter writer = new JSONWriter(null);
        // System.out.println(schema);

        // JJ variant
        // JsonSchemaGenerator v4generator =
        // SchemaGeneratorBuilder.draftV4Schema().build();
        // JsonNode jsonSchema =
        // v4generator.generateSchema(RoutingResponse.class);
        // System.out.println(mapper.writeValueAsString(jsonSchema));
    }

}
