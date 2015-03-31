package at.ac.ait.sproute.routeformat.example;

import org.json.JSONObject;
import org.json.JSONWriter;

import pl.zientarski.SchemaMapper;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeatureCollection;

public class SchemaGenerator {
	
	public static void main(String[] args) {
		SchemaMapper schemaMapper = new SchemaMapper();
		JSONObject schema = schemaMapper.toJsonSchema4(GeoJSONFeatureCollection.class, true);
		JSONWriter writer = new JSONWriter(null);
		System.out.println(schema);
		
		// JJ variant
		// JsonSchemaGenerator v4generator =
		// SchemaGeneratorBuilder.draftV4Schema().build();
		// JsonNode jsonSchema =
		// v4generator.generateSchema(RouteFormatRoot.class);
		// System.out.println(mapper.writeValueAsString(jsonSchema));
	}

}
