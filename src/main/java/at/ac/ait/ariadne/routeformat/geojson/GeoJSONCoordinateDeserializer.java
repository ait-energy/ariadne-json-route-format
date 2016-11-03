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

public class GeoJSONCoordinateDeserializer extends JsonDeserializer<GeoJSONCoordinate> {

	@Override
	public GeoJSONCoordinate deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		if (!p.isExpectedStartArrayToken())
			throw ctxt.mappingException("expected array start for coordinate");

		List<BigDecimal> coordinates = new ArrayList<>();
		for (JsonToken t = p.nextToken(); t != null; t = p.nextToken()) {
			if (t.equals(JsonToken.VALUE_NUMBER_INT) || t.equals(JsonToken.VALUE_NUMBER_FLOAT))
				coordinates.add(new BigDecimal(p.getValueAsString()));
			else
				break;
		}
		return GeoJSONCoordinate.create(coordinates);
	}

}
