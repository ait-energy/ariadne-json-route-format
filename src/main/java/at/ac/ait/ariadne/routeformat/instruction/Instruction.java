package at.ac.ait.ariadne.routeformat.instruction;

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

	/**
	 * @return the position the instruction is defined for
	 */
	public GeoJSONFeature<GeoJSONPoint> getPosition() {
		return position;
	}

	/**
	 * @return a position where, during on-trip navigation, a first instruction could be presented to the user
	 */
	public Optional<GeoJSONFeature<GeoJSONPoint>> getPreviewTriggerPosition() {
		return previewTriggerPosition;
	}

	/**
	 * @return a position after the maneuver, where, during on-trip navigation, a confirmation message could be
	 *         presented to the user
	 */
	public Optional<GeoJSONFeature<GeoJSONPoint>> getConfirmationTriggerPosition() {
		return confirmationTriggerPosition;
	}

	Instruction(GeoJSONFeature<GeoJSONPoint> position, Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition,
			Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition) {
		this.position = position;
		this.previewTriggerPosition = previewTriggerPosition;
		this.confirmationTriggerPosition = confirmationTriggerPosition;
	}

}
