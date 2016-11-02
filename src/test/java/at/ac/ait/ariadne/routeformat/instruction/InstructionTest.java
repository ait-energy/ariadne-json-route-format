package at.ac.ait.ariadne.routeformat.instruction;

import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;

public class InstructionTest {

	private static ObjectMapper mapper;

	@BeforeClass
	public static void setup() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}

	@Test
	public void testProperDeSerialization() throws IOException {
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
		BasicRoadInstruction instruction = BasicRoadInstruction.createMinimalRouteStart(
				new Coordinate("48.123", "16"), Optional.of("Testweg"), Optional.of(FormOfWay.ROAD));
		instruction.validate();

		String expected = "{\"type\":\"BasicRoadInstruction\",\"position\":{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[48.123,16]},\"properties\":{}},\"subType\":\"ROUTE_START\",\"ontoStreetName\":\"Testweg\",\"ontoFormOfWay\":\"ROAD\"}";
		Assert.assertEquals("serialization failed", expected, mapper.writeValueAsString(instruction));

		Instruction<?> deserializedInstruction = mapper.readValue(expected, Instruction.class);
		System.out.println(deserializedInstruction);
		Assert.assertEquals("deserialization failed", expected, mapper.writeValueAsString(deserializedInstruction));
	}

	// @Test
	// public void testSchema() throws JsonProcessingException {
	// mapper.enable(SerializationFeature.INDENT_OUTPUT);
	// JsonSchemaGenerator jsonSchemaGenerator = new
	// JsonSchemaGenerator(mapper);
	// Option<String> emtpyOption = Option.empty();
	// JsonNode jsonSchema =
	// jsonSchemaGenerator.generateJsonSchema(Instruction.class, emtpyOption,
	// emtpyOption);
	//
	// System.out.println(mapper.writeValueAsString(jsonSchema));
	// }

}
