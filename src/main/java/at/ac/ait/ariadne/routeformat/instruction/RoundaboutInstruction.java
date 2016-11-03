package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;

/**
 * Instructions for navigating a roundabout.
 * <p>
 * In its minimal form it consists of a position and a {@link #getSubType()}. In
 * case of {@link SubType#ENTER} the {@link #getExitNr()} is mandatory as well.
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
 * ENTER = "Take", EXIT_NUMBER, "exit", ["onto", NAME_OR_TYPE], "on roundabout", [STREET_NAME_STRING];
 * EXIT = "Exit roundabout onto", [STREET_NAME_STRING], [CONFIRMATION_LANDMARK_PART], [CONTINUE];
 * 
 * EXIT_NUMBER = "first", "second",...;
 * NAME_OR_TYPE = STREET_NAME_STRING | FORM_OF_WAY_STRING;
 * CONTINUE = "and follow it", ["for", UNIT], ["until", CONTINUE_LANDMARK_PART]; (* at least one of the two *)
 * UNIT = [DISTANCE_STRING], [TIME_STRING]; (* at least one of the two *)
 * 
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
	private Optional<String> roundaboutStreetName = Optional.empty();
	private Optional<String> ontoStreetName = Optional.empty();
	private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
	private Optional<Integer> exitNr = Optional.empty();
	private Optional<Integer> continueMeters = Optional.empty(), continueSeconds = Optional.empty();
	private Optional<Landmark> confirmationLandmark = Optional.empty();

	// -- getters

	public SubType getSubType() {
		return subType;
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

	public Optional<Landmark> getConfirmationLandmark() {
		return confirmationLandmark;
	}

	// -- setters

	public RoundaboutInstruction setSubType(SubType subType) {
		this.subType = subType;
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

	public RoundaboutInstruction setConfirmationLandmark(Landmark confirmationLandmark) {
		this.confirmationLandmark = Optional.ofNullable(confirmationLandmark);
		return this;
	}

	// --

	public static RoundaboutInstruction createMinimalEnter(Coordinate position, int exitNr) {
		return new RoundaboutInstruction().setPosition(position).setSubType(SubType.ENTER).setExitNr(exitNr);
	}

	public static RoundaboutInstruction createMinimalExit(Coordinate position) {
		return new RoundaboutInstruction().setPosition(position).setSubType(SubType.EXIT);
	}

	// /**
	// * Set all attributes useful for a {@link SubType#ENTER}
	// *
	// * @param exitNr
	// * mandatory
	// */
	// public Builder createMinimalEnterInstruction(CoordinatePoint position,
	// Optional<String> roundaboutStreetName,
	// Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay, int
	// exitNr) {
	// this.subType = SubType.ENTER;
	// this.position = GeoJSONFeature.newPointFeature(position);
	// this.roundaboutStreetName = roundaboutStreetName;
	// this.ontoStreetName = ontoStreetName;
	// this.ontoFormOfWay = ontoFormOfWay;
	// this.exitNr = Optional.of(exitNr);
	// return this;
	// }
	//
	// /**
	// * Set all attributes useful for a {@link SubType#EXIT}
	// */
	// public Builder forExitingRoundabout(CoordinatePoint position,
	// Optional<String> ontoStreetName,
	// Optional<FormOfWay> ontoFormOfWay, Optional<Integer> continueMeters,
	// Optional<Integer> continueSeconds,
	// Optional<Landmark> confirmationLandmark) {
	// this.subType = SubType.EXIT;
	// this.position = GeoJSONFeature.newPointFeature(position);
	// this.ontoStreetName = ontoStreetName;
	// this.ontoFormOfWay = ontoFormOfWay;
	// this.continueMeters = continueMeters;
	// this.continueSeconds = continueSeconds;
	// this.confirmationLandmark = confirmationLandmark;
	// return this;
	// }

	@Override
	public void validate() {
		super.validate();
		Preconditions.checkArgument(subType != null, "subType is mandatory but missing");
		if (subType.equals(SubType.ENTER))
			Preconditions.checkArgument(exitNr.isPresent(), "exit nr is mandatory for enter-instructions");
	}

	@Override
	public String toString() {
		return super.toString() + " -> RoundaboutInstruction [subType=" + subType + ", roundaboutStreetName="
				+ roundaboutStreetName + ", ontoStreetName=" + ontoStreetName + ", ontoFormOfWay=" + ontoFormOfWay
				+ ", exitNr=" + exitNr + ", continueMeters=" + continueMeters + ", continueSeconds=" + continueSeconds
				+ ", confirmationLandmark=" + confirmationLandmark + "]";
	}

}
