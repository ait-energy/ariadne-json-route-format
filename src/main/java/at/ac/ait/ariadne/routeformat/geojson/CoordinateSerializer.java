package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CoordinateSerializer extends JsonSerializer<Coordinate> {

	@Override
	public void serialize(Coordinate value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartArray();
		gen.writeNumber(value.getX());
		gen.writeNumber(value.getY());
		if (value.getZ().isPresent())
			gen.writeNumber(value.getZ().get());
		gen.writeEndArray();
	}

}
