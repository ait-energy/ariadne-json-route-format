package at.ac.ait.ariadne.routeformat.instruction;

import java.io.IOException;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.TestUtil;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

public class InstructionTest {

    @Test
    public void testProperDeSerialization() throws IOException {
        RoadInstruction instruction = RoadInstruction.createMinimalRouteStart(
                GeoJSONCoordinate.create("48.123", "16"),
                Optional.of("Testweg"),
                Optional.of(FormOfWay.ROAD));
        instruction.validate();

        String expected = "{\"type\":\"RoadInstruction\",\"position\":{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[48.123,16]},\"properties\":{}},\"text\":{},\"additionalInfo\":{},\"subType\":\"ROUTE_START\",\"ontoStreetName\":\"Testweg\",\"ontoFormOfWay\":\"ROAD\"}";
        System.out.println(TestUtil.MAPPER.writeValueAsString(instruction));
        Assert.assertEquals("serialization failed", expected, TestUtil.MAPPER.writeValueAsString(instruction));

        Instruction<?> deserializedInstruction = TestUtil.MAPPER.readValue(expected, Instruction.class);
        System.out.println(deserializedInstruction);
        Assert.assertEquals(
                "deserialization failed",
                expected,
                TestUtil.MAPPER.writeValueAsString(deserializedInstruction));
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
