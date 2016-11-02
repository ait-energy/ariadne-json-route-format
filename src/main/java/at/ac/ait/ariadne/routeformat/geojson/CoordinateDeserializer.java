package at.ac.ait.ariadne.routeformat.geojson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CoordinateDeserializer extends JsonDeserializer<Coordinate> {

	@Override
	public Coordinate deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		List<BigDecimal> coordinates = new ArrayList<>();
		for (JsonToken t = p.currentToken(); t != null; t = p.nextToken()) {
			switch (t) {
			case VALUE_NUMBER_INT:
			case VALUE_NUMBER_FLOAT:
				coordinates.add(new BigDecimal(p.getValueAsString()));
				break;
			default:
				// ignore non-values
			}
		}
		return Coordinate.create(coordinates);
	}

}
