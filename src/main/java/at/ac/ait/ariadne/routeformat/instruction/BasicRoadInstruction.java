package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.CompassDirection;
import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.Constants.TurnDirection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * A {@link BasicRoadInstruction} contains episodes with classic-style turn
 * navigations for street-based modes of transport such as walking, cycling and
 * driving (keep straight, turn left/right, make a u-turn).
 * <p>
 * In its minimal form it consists of a position, a {@link #getSubType()} and at
 * least one of {@link #getOntoStreetName()} and {@link #getOntoFormOfWay()}.
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable
 * text and what's mandatory / optional. Elements ending with STRING are
 * terminal (not defined any further).
 * <p>
 * CONTINUE_LANDMARK_STRING must be retrieved from the next {@link Instruction},
 * it can be a classic landmark or also the type of the next instruction, e.g.
 * roundabout.
 * 
 * <pre>
 * {@code
 * BASIC_INSTRUCTION = ROUTE_START | ROUTE_END | STRAIGHT | TURN | U_TURN;
 * 
 * ROUTE_START = "Start", [LANDMARK_PART], "on", NAME_OR_TYPE, [INITIAL_DIRECTION], [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * ROUTE_END = "You reached your destination", [LANDMARK_PART], "on", NAME_OR_TYPE;
 * STRAIGHT = "Keep straight", [LANDMARK_PART], "on", NAME_OR_TYPE, [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * TURN = "Turn", ["slight"], DIRECTION, [LANDMARK_PART], "on", NAME_OR_TYPE, [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * U_TURN = "Make a u-turn", [LANDMARK_PART], on", NAME_OR_TYPE, [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * 
 * NAME_OR_TYPE = STREET_NAME_STRING | FORM_OF_WAY_STRING;
 * 
 * INITIAL_DIRECTION = "heading", COMPASS_STRING, ["into the direction of", CONTINUE_LANDMARK_STRING];
 *
 * CONTINUE = "and follow it", ["for", UNIT], ["until", CONTINUE_LANDMARK_PART]; (* at least one of the two *)
 * UNIT = [DISTANCE_STRING], [TIME_STRING]; (* at least one of the two *)
 * 
 * LANDMARK_PART = PREPOSITION, LANDMARK_STRING;
 * CONFIRMATION_LANDMARK_PART = CONFIRMATION_PREPOSITION, CONFIRMATION_LANDMARK_STRING;
 * CONTINUE_LANDMARK_PART = PREPOSITION, CONTINUE_LANDMARK_STRING;
 * 
 * PREPOSITION = "before" | "at" | "after";
 * CONFIRMATION_PREPOSITION = "towards" | "through" | "along" | "past";
 * 
 * DIRECTION = "left" | "right";
 * }
 * </pre>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class BasicRoadInstruction extends Instruction<BasicRoadInstruction> {

	public enum SubType {
		ROUTE_START, ROUTE_END, STRAIGHT, TURN, U_TURN
	}

	private SubType subType;
	private Optional<TurnDirection> turnDirection = Optional.empty();
	private Optional<CompassDirection> compassDirection = Optional.empty();
	private Optional<Boolean> roadChange = Optional.empty();
	private Optional<String> ontoStreetName = Optional.empty();
	private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
	private Optional<Integer> continueMeters = Optional.empty(), continueSeconds = Optional.empty();
	private Optional<Landmark> landmark = Optional.empty(), confirmationLandmark = Optional.empty();

	// -- getters

	public SubType getSubType() {
		return subType;
	}

	/**
	 * @return the turn direction relative to the direction until this point
	 */
	public Optional<TurnDirection> getTurnDirection() {
		return turnDirection;
	}

	/**
	 * @return the heading after this point
	 */
	public Optional<CompassDirection> getCompassDirection() {
		return compassDirection;
	}

	/** @return <code>true</code> if the road name or type has changed */
	public Optional<Boolean> getRoadChange() {
		return roadChange;
	}

	public Optional<String> getOntoStreetName() {
		return ontoStreetName;
	}

	public Optional<FormOfWay> getOntoFormOfWay() {
		return ontoFormOfWay;
	}

	public Optional<Integer> getContinueMeters() {
		return continueMeters;
	}

	public Optional<Integer> getContinueSeconds() {
		return continueSeconds;
	}

	/**
	 * @return the landmark at begin of the instruction, i.e. at the turn, or at
	 *         the begin (for {@link SubType#ROUTE_START}) or at the end (for
	 *         {@link SubType#ROUTE_END}) of the route. At the same time this
	 *         landmark is the continue-landmark for the previous instruction,
	 *         i.e. the landmark after {@link #getContinueMeters()}.
	 */
	public Optional<Landmark> getLandmark() {
		return landmark;
	}

	/**
	 * @return a landmark between this and the next instruction (or a global
	 *         landmark in the general direction after this instruction) that
	 *         helps users to stay on track
	 */
	public Optional<Landmark> getConfirmationLandmark() {
		return confirmationLandmark;
	}

	// -- setters

	public BasicRoadInstruction setSubType(SubType subType) {
		this.subType = subType;
		return this;
	}

	public BasicRoadInstruction setTurnDirection(TurnDirection turnDirection) {
		this.turnDirection = Optional.ofNullable(turnDirection);
		return this;
	}

	public BasicRoadInstruction setCompassDirection(CompassDirection compassDirection) {
		this.compassDirection = Optional.ofNullable(compassDirection);
		return this;
	}

	public BasicRoadInstruction setRoadChange(Boolean roadChange) {
		this.roadChange = Optional.ofNullable(roadChange);
		return this;
	}

	public BasicRoadInstruction setOntoStreetName(String ontoStreetName) {
		this.ontoStreetName = Optional.ofNullable(ontoStreetName);
		return this;
	}

	public BasicRoadInstruction setOntoFormOfWay(FormOfWay ontoFormOfWay) {
		this.ontoFormOfWay = Optional.ofNullable(ontoFormOfWay);
		return this;
	}

	public BasicRoadInstruction setContinueMeters(Integer continueMeters) {
		this.continueMeters = Optional.ofNullable(continueMeters);
		return this;
	}

	public BasicRoadInstruction setContinueSeconds(Integer continueSeconds) {
		this.continueSeconds = Optional.ofNullable(continueSeconds);
		return this;
	}

	/**
	 * @param landmark
	 *            the landmark at the start point, end point, or decision point
	 */
	public BasicRoadInstruction setLandmark(Landmark landmark) {
		this.landmark = Optional.ofNullable(landmark);
		return this;
	}

	public BasicRoadInstruction setConfirmationLandmark(Landmark confirmationLandmark) {
		this.confirmationLandmark = Optional.ofNullable(confirmationLandmark);
		return this;
	}

	// --

	/**
	 * either street name or form of way must be present
	 */
	public static BasicRoadInstruction createMinimalRouteStart(GeoJSONCoordinate position,
			Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay) {
		return new BasicRoadInstruction().setPosition(position).setSubType(SubType.ROUTE_START)
				.setOntoStreetName(ontoStreetName.orElse(null)).setOntoFormOfWay(ontoFormOfWay.orElse(null));
	}

	/**
	 * either street name or form of way must be present
	 */
	public static BasicRoadInstruction createMinimalOnRoute(GeoJSONCoordinate position, TurnDirection turnDirection,
			Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay) {
		return new BasicRoadInstruction().setPosition(position).setSubType(getSubType(turnDirection))
				.setTurnDirection(turnDirection).setOntoStreetName(ontoStreetName.orElse(null))
				.setOntoFormOfWay(ontoFormOfWay.orElse(null));
	}

	/**
	 * either street name or form of way (of the destination) must be present
	 */
	public static BasicRoadInstruction createMinimalRouteEnd(GeoJSONCoordinate position,
			Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay) {
		return new BasicRoadInstruction().setPosition(position).setSubType(SubType.ROUTE_END)
				.setOntoStreetName(ontoStreetName.orElse(null)).setOntoFormOfWay(ontoFormOfWay.orElse(null));
	}

	// /**
	// * Create an instructions for a {@link SubType#ROUTE_START} with all
	// * relevant attributes
	// *
	// * @param compassDirection
	// * the direction in which the route is starting
	// * @param landmark
	// * the landmark where the route starts
	// */
	// public static BasicRoadInstruction
	// createFullInstructionForRouteStart(CoordinatePoint position,
	// CompassDirection compassDirection,
	// Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay,
	// Optional<Integer> continueMeters,
	// Optional<Integer> continueSeconds, Optional<Landmark> landmark,
	// Optional<Landmark> confirmationLandmark) {
	// return new BasicRoadInstruction()
	// this.subType = SubType.ROUTE_START;
	// this.position = GeoJSONFeature.newPointFeature(position);
	// this.compassDirection = Optional.of(compassDirection);
	// this.ontoStreetName = ontoStreetName;
	// this.ontoFormOfWay = ontoFormOfWay;
	// this.continueMeters = continueMeters;
	// this.continueSeconds = continueSeconds;
	// this.landmark = landmark;
	// this.confirmationLandmark = confirmationLandmark;
	// return this;
	// }

	// /**
	// * Set all attributes useful for an instruction for going straight and
	// * turning
	// */
	// public BasicRoadInstruction forNormalInstruction(CoordinatePoint
	// position, TurnDirection turnDirection,
	// boolean roadChange, Optional<String> ontoStreetName, Optional<FormOfWay>
	// ontoFormOfWay,
	// Optional<Integer> continueMeters, Optional<Integer> continueSeconds,
	// Optional<Landmark> landmark,
	// Optional<Landmark> confirmationLandmark) {
	// this.subType = getSubType(turnDirection);
	// this.position = GeoJSONFeature.newPointFeature(position);
	// this.turnDirection = Optional.of(turnDirection);
	// this.roadChange = Optional.of(roadChange);
	// this.ontoStreetName = ontoStreetName;
	// this.ontoFormOfWay = ontoFormOfWay;
	// this.continueMeters = continueMeters;
	// this.continueSeconds = continueSeconds;
	// this.landmark = landmark;
	// this.confirmationLandmark = confirmationLandmark;
	// return this;
	// }
	//
	// /**
	// * Set all attributes useful for a {@link SubType#ROUTE_END}
	// *
	// * @param landmark
	// * the landmark where the route ends
	// */
	// public BasicRoadInstruction forRouteEnd(CoordinatePoint position,
	// Optional<String> ontoStreetName,
	// Optional<FormOfWay> ontoFormOfWay, Optional<Landmark> landmark) {
	// this.subType = SubType.ROUTE_END;
	// this.position = GeoJSONFeature.newPointFeature(position);
	// this.ontoStreetName = ontoStreetName;
	// this.ontoFormOfWay = ontoFormOfWay;
	// this.landmark = landmark;
	// return this;
	// }

	private static SubType getSubType(TurnDirection turnDirection) {
		if (turnDirection == TurnDirection.U_TURN)
			return SubType.U_TURN;
		else if (turnDirection == TurnDirection.STRAIGHT)
			return SubType.STRAIGHT;
		else
			return SubType.TURN;
	}

	@Override
	public void validate() {
		super.validate();
		Preconditions.checkArgument(subType != null, "subType is mandatory but missing");
		Preconditions.checkArgument(ontoStreetName.isPresent() || ontoFormOfWay.isPresent(),
				"at least one onto-type is required");
	}

	@Override
	public String toString() {
		return super.toString() + " -> BasicRoadInstruction [subType=" + subType + ", turnDirection=" + turnDirection
				+ ", compassDirection=" + compassDirection + ", roadChange=" + roadChange + ", ontoStreetName="
				+ ontoStreetName + ", ontoFormOfWay=" + ontoFormOfWay + ", continueMeters=" + continueMeters
				+ ", continueSeconds=" + continueSeconds + ", landmark=" + landmark + ", confirmationLandmark="
				+ confirmationLandmark + "]";
	}

}
