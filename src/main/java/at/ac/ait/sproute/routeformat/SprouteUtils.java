package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class SprouteUtils {
	
	public static ZonedDateTime parseZonedDateTime(String zonedDateTimeString, String variableName) {
		try {
    		return ZonedDateTime.parse(zonedDateTimeString);
    	} catch (DateTimeParseException e) {
    		throw new IllegalArgumentException(variableName + " could not be parsed: " + e.getMessage());
    	}
	}

}
