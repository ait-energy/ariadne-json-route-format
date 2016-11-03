package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeoJSONCoordinateSerializer extends JsonSerializer<GeoJSONCoordinate> {

	@Override
	public void serialize(GeoJSONCoordinate value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartArray();
		gen.writeNumber(value.getX());
		gen.writeNumber(value.getY());
		if (value.getZ().isPresent())
			gen.writeNumber(value.getZ().get());
		gen.writeEndArray();
	}

}
