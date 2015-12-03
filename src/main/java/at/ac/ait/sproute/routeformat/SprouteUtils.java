package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

import at.ac.ait.sproute.routeformat.Sproute.ModeOfTransport;

public class SprouteUtils {
	
	public static ZonedDateTime parseZonedDateTime(String zonedDateTimeString, String variableName) {
		if(zonedDateTimeString == null)
			throw new IllegalArgumentException(variableName + " must not be null");
		
		try {
    		return ZonedDateTime.parse(zonedDateTimeString);
    	} catch (DateTimeParseException e) {
    		throw new IllegalArgumentException(variableName + " could not be parsed: " + e.getMessage());
    	}
	}
	
	/**
	 * @param modesOfTransport a comma-separated list of MOTs
	 */
	public static Set<ModeOfTransport> parseModesOfTransport(String modesOfTransport, String variableName) {
		if(modesOfTransport == null)
			throw new IllegalArgumentException(modesOfTransport + " must not be null");
		
		Set<ModeOfTransport> motSet = new HashSet<>();
		for(String modeOfTransport : modesOfTransport.split(",")) {
			try {
				motSet.add(ModeOfTransport.valueOf(modeOfTransport.trim().toUpperCase()));
			} catch (IllegalArgumentException exc) {
                throw new IllegalArgumentException("'" + modeOfTransport + "' is not a valid mode of transport");
            }
		}
		return motSet;
	}

}
