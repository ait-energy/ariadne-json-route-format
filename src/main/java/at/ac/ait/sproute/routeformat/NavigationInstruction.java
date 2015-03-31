package at.ac.ait.sproute.routeformat;

import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigationInstruction {
	
	public enum Preposition {
		BEFORE, AT, AFTER
	};

	public enum Direction {
		ORIGIN, DESTINATION,
		//
		CONTINUE_STRAIGHT, KEEP_STRAIGHT, KEEP_LEFT, KEEP_RIGHT, //
		TURN_LEFT, TURN_RIGHT, TURN_SLIGHT_LEFT, TURN_SLIGHT_RIGHT, TURN_SHARP_LEFT, TURN_SHARP_RIGHT, U_TURN,
		//
		ROUNDABOUT_THIRD_EXIT, ROUNDABOUT_SECOND_EXIT, ROUNDABOUT_FIRST_EXIT, //
		ROUNDABOUT_ENTER, ROUNDABOUT_STAY, ROUNDABOUT_EXIT_NOW,
		//
		ESCALATOR_DOWN, ESCALATOR_UP, ELEVATOR_DOWN, ELEVATOR_UP, STAIRS_UP, STAIRS_DOWN,
		//
		PUBLIC_TRANSPORT_BOARD, PUBLIC_TRANSPORT_ALIGHT, PUBLIC_TRANSPORT_STAY
	};

	@JsonProperty(required = true)
	public String landmarkType;
	
	public String landmarkName;
	
	@JsonProperty(required = true)
	public Preposition preposition;
	
	@JsonProperty(required = true)
	public Direction turnDirection;
	
	@JsonProperty(required = true)
	public String target;
	
	public int metersToNextDecisionPoint;
	
	@JsonProperty(required = true)
	public GeoJSONFeature<GeoJSONPoint> turnTriggerPosition;
	
	public GeoJSONFeature<GeoJSONPoint> previewTriggerPosition;
	public GeoJSONFeature<GeoJSONPoint> confirmationTriggerPosition;
	
}
