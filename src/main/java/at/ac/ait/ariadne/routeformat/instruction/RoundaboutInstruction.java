package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.FormOfWay;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.ariadne.routeformat.instruction.RoundaboutInstruction.Builder;

/**
 * Instructions for navigating a roundabout. The {@link #exitNr} is mandatory
 * for {@link SubType#ENTER}.
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
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RoundaboutInstruction extends Instruction {

    public enum SubType {
        ENTER, EXIT
    }

    private final SubType subType;
    private final Optional<String> roundaboutStreetName;
    private final Optional<String> ontoStreetName;
    private final Optional<FormOfWay> ontoFormOfWay;
    private final Optional<Integer> exitNr;
    private final Optional<Integer> continueMeters, continueSeconds;
    private final Optional<Landmark> confirmationLandmark;

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

    private RoundaboutInstruction(Builder builder) {
        super(builder.position, builder.previewTriggerPosition, builder.confirmationTriggerPosition);
        this.subType = builder.subType;
        this.roundaboutStreetName = builder.roundaboutStreetName;
        this.ontoStreetName = builder.ontoStreetName;
        this.ontoFormOfWay = builder.ontoFormOfWay;
        this.exitNr = builder.exitNr;
        this.continueMeters = builder.continueMeters;
        this.continueSeconds = builder.continueSeconds;
        this.confirmationLandmark = builder.confirmationLandmark;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder enterBuilder(CoordinatePoint point) {
        return new Builder().withSubType(SubType.ENTER).withPosition(GeoJSONFeature.newPointFeature(point));
    }

    public static Builder exitBuilder(CoordinatePoint point) {
        return new Builder().withSubType(SubType.EXIT).withPosition(GeoJSONFeature.newPointFeature(point));
    }

    @Override
    public String toString() {
        return "RoundaboutInstruction [subType=" + subType + ", roundaboutStreetName=" + roundaboutStreetName
                + ", ontoStreetName=" + ontoStreetName + ", ontoFormOfWay=" + ontoFormOfWay + ", exitNr=" + exitNr
                + ", continueMeters=" + continueMeters + ", continueSeconds=" + continueSeconds
                + ", confirmationLandmark=" + confirmationLandmark + "]";
    }

    public static class Builder {

        private SubType subType;
        private GeoJSONFeature<GeoJSONPoint> position;
        private Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition;
        private Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition;
        private Optional<String> roundaboutStreetName = Optional.empty();
        private Optional<String> ontoStreetName = Optional.empty();
        private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
        private Optional<Integer> exitNr = Optional.empty();
        private Optional<Integer> continueMeters = Optional.empty(), continueSeconds = Optional.empty();
        private Optional<Landmark> confirmationLandmark = Optional.empty();

        public Builder withSubType(SubType subType) {
            this.subType = subType;
            return this;
        }

        public Builder withPosition(GeoJSONFeature<GeoJSONPoint> position) {
            this.position = position;
            return this;
        }

        public Builder withPreviewTriggerPosition(GeoJSONFeature<GeoJSONPoint> previewTriggerPosition) {
            this.previewTriggerPosition = Optional.ofNullable(previewTriggerPosition);
            return this;
        }

        public Builder withConfirmationTriggerPosition(GeoJSONFeature<GeoJSONPoint> confirmationTriggerPosition) {
            this.confirmationTriggerPosition = Optional.ofNullable(confirmationTriggerPosition);
            return this;
        }

        public Builder withRoundaboutStreetName(String roundaboutStreetName) {
            this.roundaboutStreetName = Optional.ofNullable(roundaboutStreetName);
            return this;
        }

        public Builder withOntoStreetName(String ontoStreetName) {
            this.ontoStreetName = Optional.ofNullable(ontoStreetName);
            return this;
        }

        public Builder withOntoFormOfWay(FormOfWay ontoFormOfWay) {
            this.ontoFormOfWay = Optional.ofNullable(ontoFormOfWay);
            return this;
        }

        public Builder withExitNr(Integer exitNr) {
            this.exitNr = Optional.ofNullable(exitNr);
            return this;
        }

        public Builder withContinueMeters(Integer continueMeters) {
            this.continueMeters = Optional.ofNullable(continueMeters);
            return this;
        }

        public Builder withContinueSeconds(Integer continueSeconds) {
            this.continueSeconds = Optional.ofNullable(continueSeconds);
            return this;
        }
        
        public Builder withConfirmationLandmark(Landmark confirmationLandmark) {
            this.confirmationLandmark = Optional.ofNullable(confirmationLandmark);
            return this;
        }

        /**
         * Set all attributes useful for a {@link SubType#ENTER}
         * 
         * @param exitNr
         *            mandatory
         */
        public Builder forEnteringRoundabout(CoordinatePoint position, Optional<String> roundaboutStreetName,
                Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay, int exitNr) {
            this.subType = SubType.ENTER;
            this.position = GeoJSONFeature.newPointFeature(position);
            this.roundaboutStreetName = roundaboutStreetName;
            this.ontoStreetName = ontoStreetName;
            this.ontoFormOfWay = ontoFormOfWay;
            this.exitNr = Optional.of(exitNr);
            return this;
        }

        /**
         * Set all attributes useful for a {@link SubType#EXIT}
         */
        public Builder forExitingRoundabout(CoordinatePoint position, Optional<String> ontoStreetName,
                Optional<FormOfWay> ontoFormOfWay, Optional<Integer> continueMeters,
                Optional<Integer> continueSeconds, Optional<Landmark> confirmationLandmark) {
            this.subType = SubType.EXIT;
            this.position = GeoJSONFeature.newPointFeature(position);
            this.ontoStreetName = ontoStreetName;
            this.ontoFormOfWay = ontoFormOfWay;
            this.continueMeters = continueMeters;
            this.continueSeconds = continueSeconds;
            this.confirmationLandmark = confirmationLandmark;
            return this;
        }

        public RoundaboutInstruction build() {
            validate();
            return new RoundaboutInstruction(this);
        }

        private void validate() {
            Preconditions.checkArgument(subType != null, "subType is mandatory but missing");
            Preconditions.checkArgument(position != null, "position is mandatory but missing");
            if (subType.equals(SubType.ENTER))
                Preconditions.checkArgument(exitNr.isPresent(), "exit nr is mandatory for enter-instructions");
        }
    }

}
