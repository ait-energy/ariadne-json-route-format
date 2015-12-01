package at.ac.ait.sproute.routeformat.instruction;

import java.util.Optional;

/**
 * A {@link BasicRoadInstruction} contains episodes with classic-style turn navigations for street-based modes of
 * transport such as walking, cycling and driving (keep straight, turn left/right, make a u-turn). Most fields are
 * optional, but at least one of {@link #ontoStreetName} and {@link #ontoFormOfWay} is guaranteed to be available.
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable text. Elements ending with STRING are
 * terminal (not defined any further).
 * 
 * <pre>
 * {@code
 * BASIC_INSTRUCTION = ROUTE_START | ROUTE_END | STRAIGHT | TURN | U_TURN;
 * 
 * ROUTE_START = "Start", [LANDMARK_PART], "on", NAME_OR_TYPE, [INITIAL_DIRECTION], [CONTINUE];
 * ROUTE_END = "You reached your destination", [LANDMARK_PART], "on", NAME_OR_TYPE;
 * STRAIGHT = "Keep straight", [LANDMARK_PART], "on", NAME_OR_TYPE, [CONTINUE];
 * TURN = "Turn", ["slight"], DIRECTION, [LANDMARK_PART], "onto", NAME_OR_TYPE, [CONTINUE];
 * U_TURN = "Make a u-turn", [LANDMARK_PART], onto", NAME_OR_TYPE, [CONTINUE];
 * 
 * LANDMARK_PART = PREPOSITION, LANDMARK_STRING;
 * PREPOSITION = "before" | "at" | "after";
 * 
 * NAME_OR_TYPE = STREET_NAME_STRING | FORM_OF_WAY_STRING;
 * 
 * INITIAL_DIRECTION = "heading", COMPASS_STRING, ["into the direction of", LANDMARK_STRING];
 * 
 * CONTINUE = "and follow it", ["for", UNIT], ["until", LANDMARK_PART]; (* at least one of the two *)
 * UNIT = [DISTANCE_STRING], [TIME_STRING]; (* at least one of the two *)
 * 
 * DIRECTION = "left" | "right";
 * }
 * </pre>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class BasicRoadInstruction implements Instruction {

	public enum Type {
		ROUTE_START, ROUTE_END, STRAIGHT, TURN, U_TURN
	}

	public final Type type;
	public final Optional<TurnDirection> turnDirection;
	public final Optional<CompassDirection> compassDirection;
	/** road name or type has changed */
	public final Optional<Boolean> roadChange;
	public final Optional<String> ontoStreetName;
	public final Optional<FormOfWay> ontoFormOfWay;
	public final Optional<Integer> continueMeters, continueSeconds;
	public final Optional<Landmark> turnLandmark, continueLandmark;

	/**
	 * Create a new {@link BasicRoadInstruction} for straight and (u-)turns
	 */
	public static BasicRoadInstruction newInstruction(TurnDirection turnDirection, boolean roadChange,
			Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay, Optional<Integer> continueMeters,
			Optional<Integer> continueSeconds, Optional<Landmark> turnLandmark, Optional<Landmark> continueLandmark) {
		Type type = getType(turnDirection);
		Optional<CompassDirection> compassDirection = Optional.empty();
		return new BasicRoadInstruction(type, Optional.of(turnDirection), compassDirection, Optional.of(roadChange),
				ontoStreetName, ontoFormOfWay, continueMeters, continueSeconds, turnLandmark, continueLandmark);
	}

	/**
	 * @param compassDirection
	 *            the direction in which the route is starting
	 * @param turnLandmark
	 *            the landmark we are starting at
	 * @param continueLandmark
	 *            the landmark we are heading towards
	 */
	public static BasicRoadInstruction newRouteStartInstruction(CompassDirection compassDirection,
			Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay, Optional<Integer> continueMeters,
			Optional<Integer> continueSeconds, Optional<Landmark> turnLandmark, Optional<Landmark> continueLandmark) {
		Type type = Type.ROUTE_START;
		Optional<TurnDirection> turnDirection = Optional.empty();
		Optional<Boolean> roadChange = Optional.empty();
		return new BasicRoadInstruction(type, turnDirection, Optional.of(compassDirection), roadChange, ontoStreetName,
				ontoFormOfWay, continueMeters, continueSeconds, turnLandmark, continueLandmark);
	}

	/**
	 * @param turnLandmark
	 *            the landmark where the route ends
	 */
	public static BasicRoadInstruction newRouteEndInstruction(Optional<String> ontoStreetName,
			Optional<FormOfWay> ontoFormOfWay, Optional<Landmark> turnLandmark) {
		Type type = Type.ROUTE_END;
		Optional<TurnDirection> turnDirection = Optional.empty();
		Optional<CompassDirection> compassDirection = Optional.empty();
		Optional<Boolean> roadChange = Optional.empty();
		Optional<Integer> continueMeters = Optional.empty();
		Optional<Integer> continueSeconds = Optional.empty();
		Optional<Landmark> continueLandmark = Optional.empty();
		return new BasicRoadInstruction(type, turnDirection, compassDirection, roadChange, ontoStreetName,
				ontoFormOfWay, continueMeters, continueSeconds, turnLandmark, continueLandmark);
	}

	private BasicRoadInstruction(Type type, Optional<TurnDirection> turnDirection,
			Optional<CompassDirection> compassDirection, Optional<Boolean> roadChange, Optional<String> ontoStreetName,
			Optional<FormOfWay> ontoFormOfWay, Optional<Integer> continueMeters, Optional<Integer> continueSeconds,
			Optional<Landmark> turnLandmark, Optional<Landmark> continueLandmark) {
		if (!ontoStreetName.isPresent() && !ontoFormOfWay.isPresent())
			throw new IllegalArgumentException("at least one onto-type is required");
		this.type = type;
		this.turnDirection = turnDirection;
		this.compassDirection = compassDirection;
		this.roadChange = roadChange;
		this.ontoStreetName = ontoStreetName;
		this.ontoFormOfWay = ontoFormOfWay;
		this.continueMeters = continueMeters;
		this.continueSeconds = continueSeconds;
		this.turnLandmark = turnLandmark;
		this.continueLandmark = continueLandmark;
	}

	private static Type getType(TurnDirection turnDirection) {
		if (turnDirection == TurnDirection.U_TURN)
			return Type.U_TURN;
		else if (turnDirection == TurnDirection.STRAIGHT)
			return Type.STRAIGHT;
		else
			return Type.TURN;
	}

}
