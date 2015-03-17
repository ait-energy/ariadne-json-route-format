package at.ac.ait.ariadne.routeformat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Provider of a mode of transport (e.g. public transport provider)
 * @author mstraub
 *
 */
public class Operator {
	
	@JsonProperty(required = true)
	public String name;

	
	public String phoneNumber;
	public String website;
	
	
}
