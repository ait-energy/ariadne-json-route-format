package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;

/**
 * Navigation instruction
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = BasicRoadInstruction.class, name = "BasicRoadInstruction"),
        @JsonSubTypes.Type(value = RoundaboutInstruction.class, name = "RoundaboutInstruction") })
public abstract class Instruction {

    private final GeoJSONFeature<GeoJSONPoint> position;
    private final Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition;
    private final Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition;
    private final Map<String, String> text;
    private final Map<String, Object> additionalInfo;

    /**
     * @return the position the instruction is defined for
     */
    public GeoJSONFeature<GeoJSONPoint> getPosition() {
        return position;
    }

    /**
     * @return a position where, during on-trip navigation, a first instruction
     *         could be presented to the user
     */
    public Optional<GeoJSONFeature<GeoJSONPoint>> getPreviewTriggerPosition() {
        return previewTriggerPosition;
    }

    /**
     * @return a position after the maneuver, where, during on-trip navigation,
     *         a confirmation message could be presented to the user
     */
    public Optional<GeoJSONFeature<GeoJSONPoint>> getConfirmationTriggerPosition() {
        return confirmationTriggerPosition;
    }

    /**
     * @return text versions of the instruction, the key of the map is the
     *         language (RFC 1766), e.g. 'en' or 'de'.
     */
    public Map<String, String> getText() {
        return text;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    Instruction(GeoJSONFeature<GeoJSONPoint> position, Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition,
            Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition, Map<String, String> text,
            Map<String, Object> additionalInfo) {
        this.position = position;
        this.previewTriggerPosition = previewTriggerPosition;
        this.confirmationTriggerPosition = confirmationTriggerPosition;
        this.text = text;
        this.additionalInfo = additionalInfo;
    }

}
