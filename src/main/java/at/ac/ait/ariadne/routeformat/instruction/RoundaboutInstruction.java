package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.CompassDirection;
import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * Instructions for navigating a roundabout. For pre-trip instructions a single
 * detailed {@link SubType#ENTER} instruction should be used. In case of on-trip
 * navigation an additional {@link SubType#EXIT} instruction could be useful.
 * <p>
 * In its minimal form it consists of a position and a {@link #getSubType()}.
 * 
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable
 * text and what's mandatory / optional. Elements ending with STRING are
 * terminal (not defined any further).
 * <p>
 * CONTINUE_LANDMARK_STRING must be retrieved from the next {@link Instruction}
 * 
 * <pre>
 * {@code
 * ROUNDABOUT_INSTRUCTION = ENTER | EXIT;
 * 
 * ENTER = "In the roundabout", [LANDMARK_PART], [ "on" ROUNDABOUT_STREET_NAME_STRING], "take the" [EXIT_NUMBER], "exit", 
 *         ["onto", NAME_OR_TYPE], [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * EXIT = "Exit roundabout now", ["onto", NAME_OR_TYPE], [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * 
 * EXIT_NUMBER = "first", "second",...;
 * NAME_OR_TYPE = ONTO_STREET_NAME_STRING | ONTO_FORM_OF_WAY_STRING;
 * CONTINUE = "and follow it", ["for", UNIT], ["until", CONTINUE_LANDMARK_PART]; (* at least one of the two *)
 * UNIT = [DISTANCE_STRING], [TIME_STRING]; (* at least one of the two *)
 * 
 * LANDMARK_PART = PREPOSITION, LANDMARK_STRING;
 * CONTINUE_LANDMARK_PART = PREPOSITION, CONTINUE_LANDMARK_STRING;
 * CONFIRMATION_LANDMARK_PART = CONFIRMATION_PREPOSITION, CONFIRMATION_LANDMARK_STRING;
 * PREPOSITION = "before" | "at" | "after";
 * CONFIRMATION_PREPOSITION = "towards" | "through" | "along" | "past";
 * }
 * </pre>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoundaboutInstruction extends Instruction<RoundaboutInstruction> {

	public enum SubType {
		ENTER, EXIT
	}

	private SubType subType;
	private Optional<CompassDirection> compassDirection = Optional.empty();
	private Optional<String> roundaboutStreetName = Optional.empty();
	private Optional<String> ontoStreetName = Optional.empty();
	private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
	private Optional<Integer> exitNr = Optional.empty();
	private Optional<Integer> continueMeters = Optional.empty(), continueSeconds = Optional.empty();
	private Optional<Landmark> landmark = Optional.empty(), confirmationLandmark = Optional.empty();

	// -- getters

	@JsonProperty(required = true)
	public SubType getSubType() {
		return subType;
	}

	public Optional<CompassDirection> getCompassDirection() {
		return compassDirection;
	}

	/** street name of the roundabout itself */
	public Optional<String> getRoundaboutStreetName() {
		return roundaboutStreetName;
	}

	/** street name of the street the roundabout exits to */
	public Optional<String> getOntoStreetName() {
		return ontoStreetName;
	}

	/** form of way of the street the roundabout exits to */
	public Optional<FormOfWay> getOntoFormOfWay() {
		return ontoFormOfWay;
	}

	public Optional<Integer> getExitNr() {
		return exitNr;
	}

	public Optional<Integer> getContinueMeters() {
		return continueMeters;
	}

	public Optional<Integer> getContinueSeconds() {
		return continueSeconds;
	}

	/**
	 * @return the landmark at roundabout. At the same time this landmark is the
	 *         continue-landmark for the previous instruction, i.e. the landmark
	 *         after {@link #getContinueMeters()}.
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

	public RoundaboutInstruction setSubType(SubType subType) {
		this.subType = subType;
		return this;
	}

	public RoundaboutInstruction setCompassDirection(CompassDirection compassDirection) {
		this.compassDirection = Optional.ofNullable(compassDirection);
		return this;
	}

	public RoundaboutInstruction setRoundaboutStreetName(String roundaboutStreetName) {
		this.roundaboutStreetName = Optional.ofNullable(roundaboutStreetName);
		return this;
	}

	public RoundaboutInstruction setOntoStreetName(String ontoStreetName) {
		this.ontoStreetName = Optional.ofNullable(ontoStreetName);
		return this;
	}

	public RoundaboutInstruction setOntoFormOfWay(FormOfWay ontoFormOfWay) {
		this.ontoFormOfWay = Optional.ofNullable(ontoFormOfWay);
		return this;
	}

	public RoundaboutInstruction setExitNr(Integer exitNr) {
		this.exitNr = Optional.ofNullable(exitNr);
		return this;
	}

	public RoundaboutInstruction setContinueMeters(Integer continueMeters) {
		this.continueMeters = Optional.ofNullable(continueMeters);
		return this;
	}

	public RoundaboutInstruction setContinueSeconds(Integer continueSeconds) {
		this.continueSeconds = Optional.ofNullable(continueSeconds);
		return this;
	}

	public RoundaboutInstruction setLandmark(Landmark landmark) {
		this.landmark = Optional.ofNullable(landmark);
		return this;
	}

	public RoundaboutInstruction setConfirmationLandmark(Landmark confirmationLandmark) {
		this.confirmationLandmark = Optional.ofNullable(confirmationLandmark);
		return this;
	}

	// --

	public static RoundaboutInstruction createMinimalEnter(GeoJSONCoordinate position) {
		return new RoundaboutInstruction().setPosition(position).setSubType(SubType.ENTER);
	}

	public static RoundaboutInstruction createMinimalExit(GeoJSONCoordinate position) {
		return new RoundaboutInstruction().setPosition(position).setSubType(SubType.EXIT);
	}

	@Override
	public void validate() {
		super.validate();
		Preconditions.checkArgument(subType != null, "subType is mandatory but missing");
	}

	@Override
	public String toString() {
		return super.toString() + " -> RoundaboutInstruction [subType=" + subType + ", compassDirection="
				+ compassDirection + ", roundaboutStreetName=" + roundaboutStreetName + ", ontoStreetName="
				+ ontoStreetName + ", ontoFormOfWay=" + ontoFormOfWay + ", exitNr=" + exitNr + ", continueMeters="
				+ continueMeters + ", continueSeconds=" + continueSeconds + ", landmark=" + landmark
				+ ", confirmationLandmark=" + confirmationLandmark + "]";
	}

}
