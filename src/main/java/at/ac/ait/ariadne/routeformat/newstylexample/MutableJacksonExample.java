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
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableMap;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import scala.Option;

/**
 * We use the "Data Binding" method of Jackson (to map POJOs to JSON).
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class MutableJacksonExample {

	public static final String schema4File = "src/main/resources/ariadne-json-route-format_schema_v4.json";
	public static final String exampleFile = "src/main/resources/all-new-example.json";
	public static final String exampleSerFile = "src/main/resources/all-new-example.ser";

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		MutableJacksonExample main = new MutableJacksonExample();
		main.writeExampleJsonAndSer();
		main.readExampleJson();
		main.writeSchemav4();
	}

	public void writeExampleJsonAndSer() throws JsonGenerationException, JsonMappingException, IOException {
		MutableJsonClass example = MutableJsonClass.createDefault(3, "servus");
//		MinimizedMutableClass example = new MinimizedMutableClass();
//		example.setMyInteger(100);
//		example.setMyOptionalString("");
//		example.setMyMap(ImmutableMap.of("a", ContinueDirection.OPPOSITE));

		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		System.out.println(mapper.writeValueAsString(example));
		System.out.println("##########");
		mapper.writeValue(new File(exampleFile), example);
		
//		try (FileOutputStream fileOut = new FileOutputStream(exampleSerFile);
//				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
//			out.writeObject(example);
//		} catch (IOException i) {
//			i.printStackTrace();
//		}
	}

	public void readExampleJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());

		// variant 1 - data binding
		MutableJsonClass example = mapper.readValue(new File(exampleFile), MutableJsonClass.class);
		System.out.println("read from json file: " + example);
		System.out.println("##########");
	}

	public void writeSchemav4() throws JsonGenerationException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);
		Option<String> emtpyOption = Option.empty();
		JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(MutableJsonClass.class, emtpyOption,
				emtpyOption);

		System.out.println(mapper.writeValueAsString(jsonSchema));
		System.out.println("##########");
		mapper.writeValue(new File(schema4File), jsonSchema);
	}

}
