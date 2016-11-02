package at.ac.ait.ariadne.routeformat.newstylexample;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.Constants.Preposition;
import at.ac.ait.ariadne.routeformat.Constants.TurnDirection;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;
import at.ac.ait.ariadne.routeformat.instruction.BasicRoadInstruction;
import at.ac.ait.ariadne.routeformat.instruction.Landmark;
import scala.Option;

/**
 * We use the "Data Binding" method of Jackson (to map POJOs to JSON).
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
class MutableJacksonExample {

	public static final String schema4File = "src/main/resources/ariadne-json-route-format_schema_v4.json";
	public static final String exampleFile = "src/main/resources/all-new-example.json";
	public static final String exampleSerFile = "src/main/resources/all-new-example.ser";

	private ObjectMapper mapper;

	public MutableJacksonExample() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		MutableJacksonExample main = new MutableJacksonExample();
		main.writeExampleJson();
		main.readExampleJson();
//		main.writeSchemav4();
	}

	public void writeExampleJson() throws JsonGenerationException, JsonMappingException, IOException {
//		MutableJsonClass example = MutableJsonClass.createDefault(3, "servus");
//		MinimizedMutableClass example = new MinimizedMutableClass();
//		example.setMyInteger(100);
//		example.setMyOptionalString("");
//		example.setMyMap(ImmutableMap.of("a", ContinueDirection.OPPOSITE));
//		RoundaboutInstruction example = RoundaboutInstruction.createMinimalEnterInstruction(new CoordinatePoint(16, 48), 2);
//		MutableJsonClass example = new DetailedMutableJsonClass().setAnotherDetail("yo").setMyInteger(1);
		Landmark landmark = Landmark.createMinimalLandmark(Preposition.AFTER, null);
		BasicRoadInstruction example = BasicRoadInstruction.createMinimalOnRoute(new Coordinate(48, 16),
				TurnDirection.LEFT, Optional.of("Ringstraße"), Optional.of(FormOfWay.ROAD));
		example.setContinueSeconds(50).setLandmark(landmark);
		
		System.out.println(mapper.writeValueAsString(example));
		System.out.println("##########");
		mapper.writeValue(new File(exampleFile), example);
	}

	public void readExampleJson() throws JsonParseException, JsonMappingException, IOException {
		BasicRoadInstruction example = mapper.readValue(new File(exampleFile), BasicRoadInstruction.class);
		System.out.println("read from json file: " + example);
		System.out.println("##########");
	}

	public void writeSchemav4() throws JsonGenerationException, IOException {
		JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);
		Option<String> emtpyOption = Option.empty();
		JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(MutableJsonClass.class, emtpyOption,
				emtpyOption);

		System.out.println(mapper.writeValueAsString(jsonSchema));
		System.out.println("##########");
		mapper.writeValue(new File(schema4File), jsonSchema);
	}

}
