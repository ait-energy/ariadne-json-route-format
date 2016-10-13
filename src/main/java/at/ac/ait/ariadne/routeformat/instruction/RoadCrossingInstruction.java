package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.ariadne.routeformat.instruction.RoadCrossingInstruction.Builder;

/**
 * Instructions for crossing a road <b>away from a junction</b>, e.g. a
 * pedestrian or cyclist changing the side of the road at a zebra crossing or
 * bicycle crossing, but it is also possible that this just is a recommended
 * place to cross the road (without an marked crossing).
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable
 * text and what's mandatory / optional. Elements ending with STRING are
 * terminal (not defined any further).
 * <p>
 * 
 * Cross the road [on the zebra crossing | at the traffic light] [and continue
 * in the same direction | and walk into the other direction].
 * 
 * <pre>
 * {@code
 * ROAD_CROSSING_INSTRUCTION = "Change to the other side of the road", [CROSSING_TYPE], [CONTINUE];
 * CROSSING_TYPE = "at the", CROSSING_INFRASTRUCTURE;
 * CROSSING_INFRASTRUCTURE = "zebra crossing" | "traffic light" | ...;
 * CONTINUE = "and continue in the", "same" | "opposite", "direction"; 
 * }
 * </pre>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RoadCrossingInstruction extends Instruction {

    public enum ContinueDirection {
        SAME, OPPOSITE
    }

    private final Optional<String> crossingInfrastructure;
    private final Optional<ContinueDirection> continueDirection;

    public Optional<String> getCrossingInfrastructure() {
        return crossingInfrastructure;
    }

    public Optional<ContinueDirection> getContinueDirection() {
        return continueDirection;
    }

    private RoadCrossingInstruction(Builder builder) {
        super(builder.position, builder.previewTriggerPosition, builder.confirmationTriggerPosition, builder.text,
                builder.additionalInfo);
        this.crossingInfrastructure = builder.crossingInfrastructure;
        this.continueDirection = builder.continueDirection;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "RoadCrossingInstruction [crossingInfrastructure=" + crossingInfrastructure + ", continueDirection="
                + continueDirection + "]";
    }

    public static class Builder {

        private GeoJSONFeature<GeoJSONPoint> position;
        private Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition;
        private Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition;
        private Map<String, String> text = Collections.emptyMap();
        private Map<String, Object> additionalInfo = Collections.emptyMap();
        private Optional<String> crossingInfrastructure = Optional.empty();
        private Optional<ContinueDirection> continueDirection = Optional.empty();

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

        public Builder withText(Map<String, String> text) {
            this.text = ImmutableSortedMap.copyOf(text);
            return this;
        }

        public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
            this.additionalInfo = ImmutableSortedMap.copyOf(additionalInfo);
            return this;
        }

        public Builder withCrossingInfrastructure(String crossingInfrastructure) {
            this.crossingInfrastructure = Optional.ofNullable(crossingInfrastructure);
            return this;
        }

        public Builder withContinueDirection(ContinueDirection continueDirection) {
            this.continueDirection = Optional.ofNullable(continueDirection);
            return this;
        }

        public RoadCrossingInstruction build() {
            validate();
            return new RoadCrossingInstruction(this);
        }

        private void validate() {
            Preconditions.checkArgument(position != null, "position is mandatory but missing");
        }
    }

}
