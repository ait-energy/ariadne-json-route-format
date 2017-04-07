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
 * In its minimal form it consists of a position, a {@link #getSubType()} and at
 * least one of {@link #getOntoStreetName()} and {@link #getOntoFormOfWay()}.
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
    private Optional<String> continueUntilIntersectingStreetName = Optional.empty();
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
     * @return the name of an intersecting road at the end of the current
     *         instruction, i.e. the place where the next instruction is
     */
    public Optional<String> getContinueUntilIntersectingStreetName() {
        return continueUntilIntersectingStreetName;
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

    public RoundaboutInstruction setContinueUntilIntersectingStreetName(String continueUntilIntersectingStreetName) {
        this.continueUntilIntersectingStreetName = Optional.ofNullable(continueUntilIntersectingStreetName);
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
        Preconditions.checkArgument(ontoStreetName.isPresent() || ontoFormOfWay.isPresent(),
                "at least one onto-type is required");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((compassDirection == null) ? 0 : compassDirection.hashCode());
        result = prime * result + ((confirmationLandmark == null) ? 0 : confirmationLandmark.hashCode());
        result = prime * result + ((continueMeters == null) ? 0 : continueMeters.hashCode());
        result = prime * result + ((continueSeconds == null) ? 0 : continueSeconds.hashCode());
        result = prime * result
                + ((continueUntilIntersectingStreetName == null) ? 0 : continueUntilIntersectingStreetName.hashCode());
        result = prime * result + ((exitNr == null) ? 0 : exitNr.hashCode());
        result = prime * result + ((landmark == null) ? 0 : landmark.hashCode());
        result = prime * result + ((ontoFormOfWay == null) ? 0 : ontoFormOfWay.hashCode());
        result = prime * result + ((ontoStreetName == null) ? 0 : ontoStreetName.hashCode());
        result = prime * result + ((roundaboutStreetName == null) ? 0 : roundaboutStreetName.hashCode());
        result = prime * result + ((subType == null) ? 0 : subType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoundaboutInstruction other = (RoundaboutInstruction) obj;
        if (compassDirection == null) {
            if (other.compassDirection != null)
                return false;
        } else if (!compassDirection.equals(other.compassDirection))
            return false;
        if (confirmationLandmark == null) {
            if (other.confirmationLandmark != null)
                return false;
        } else if (!confirmationLandmark.equals(other.confirmationLandmark))
            return false;
        if (continueMeters == null) {
            if (other.continueMeters != null)
                return false;
        } else if (!continueMeters.equals(other.continueMeters))
            return false;
        if (continueSeconds == null) {
            if (other.continueSeconds != null)
                return false;
        } else if (!continueSeconds.equals(other.continueSeconds))
            return false;
        if (continueUntilIntersectingStreetName == null) {
            if (other.continueUntilIntersectingStreetName != null)
                return false;
        } else if (!continueUntilIntersectingStreetName.equals(other.continueUntilIntersectingStreetName))
            return false;
        if (exitNr == null) {
            if (other.exitNr != null)
                return false;
        } else if (!exitNr.equals(other.exitNr))
            return false;
        if (landmark == null) {
            if (other.landmark != null)
                return false;
        } else if (!landmark.equals(other.landmark))
            return false;
        if (ontoFormOfWay == null) {
            if (other.ontoFormOfWay != null)
                return false;
        } else if (!ontoFormOfWay.equals(other.ontoFormOfWay))
            return false;
        if (ontoStreetName == null) {
            if (other.ontoStreetName != null)
                return false;
        } else if (!ontoStreetName.equals(other.ontoStreetName))
            return false;
        if (roundaboutStreetName == null) {
            if (other.roundaboutStreetName != null)
                return false;
        } else if (!roundaboutStreetName.equals(other.roundaboutStreetName))
            return false;
        if (subType != other.subType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RoundaboutInstruction [subType=" + subType + ", compassDirection=" + compassDirection
                + ", roundaboutStreetName=" + roundaboutStreetName + ", ontoStreetName=" + ontoStreetName
                + ", ontoFormOfWay=" + ontoFormOfWay + ", exitNr=" + exitNr + ", continueMeters=" + continueMeters
                + ", continueSeconds=" + continueSeconds + ", continueUntilIntersectingStreetName="
                + continueUntilIntersectingStreetName + ", landmark=" + landmark + ", confirmationLandmark="
                + confirmationLandmark + "]";
    }

}
