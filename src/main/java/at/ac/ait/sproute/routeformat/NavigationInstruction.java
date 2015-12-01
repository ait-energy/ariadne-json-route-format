package at.ac.ait.sproute.routeformat;

import java.util.Optional;

import at.ac.ait.sproute.routeformat.NavigationInstruction.Builder;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 * 
 * @deprecated will be completely redesigned
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
@Deprecated
public class NavigationInstruction {

	public enum Preposition {
		BEFORE, AT, AFTER;
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
	 //FIXME  ROUNDABOUT_FOURTH ect..._EXIT?
	private String landmarkType;
	private Optional<String> landmarkName; //FIXME could be of Type Location?
	private Preposition preposition;
	private Direction turnDirection;  //FIXME bearing in degree
	private String target; //FIXME could be of Type Location?
	private Optional<Integer> metersToNextDecisionPoint;
	private GeoJSONFeature<GeoJSONPoint> turnTriggerPosition;
	private Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition;
	private Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition;

	@JsonProperty(required = true)
	public String getLandmarkType() {
		return landmarkType;
	}

	public Optional<String> getLandmarkName() {
		return landmarkName;
	}

	@JsonProperty(required = true)
	public Preposition getPreposition() {
		return preposition;
	}

	@JsonProperty(required = true)
	public Direction getTurnDirection() {
		return turnDirection;
	}

	@JsonProperty(required = true)
	public String getTarget() {
		return target;
	}

	public Optional<Integer> getMetersToNextDecisionPoint() {
		return metersToNextDecisionPoint;
	}

	@JsonProperty(required = true)
	public GeoJSONFeature<GeoJSONPoint> getTurnTriggerPosition() {
		return turnTriggerPosition;
	}

	public Optional<GeoJSONFeature<GeoJSONPoint>> getPreviewTriggerPosition() {
		return previewTriggerPosition;
	}

	public Optional<GeoJSONFeature<GeoJSONPoint>> getConfirmationTriggerPosition() {
		return confirmationTriggerPosition;
	}

	private NavigationInstruction(Builder builder) {
		this.landmarkType = builder.landmarkType;
		this.landmarkName = builder.landmarkName;
		this.preposition = builder.preposition;
		this.turnDirection = builder.turnDirection;
		this.target = builder.target;
		this.metersToNextDecisionPoint = builder.metersToNextDecisionPoint;
		this.turnTriggerPosition = builder.turnTriggerPosition;
		this.previewTriggerPosition = builder.previewTriggerPosition;
		this.confirmationTriggerPosition = builder.confirmationTriggerPosition;
	}

	public Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String landmarkType;
		private Optional<String> landmarkName = Optional.empty();
		private Preposition preposition;
		private Direction turnDirection;
		private String target;
		private Optional<Integer> metersToNextDecisionPoint = Optional.empty();
		private GeoJSONFeature<GeoJSONPoint> turnTriggerPosition;
		private Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition = Optional.empty();

		public Builder withLandmarkType(String landmarkType) {
			this.landmarkType = landmarkType;
			return this;
		}

		public Builder withLandmarkName(String landmarkName) {
			this.landmarkName = Optional.ofNullable(landmarkName);
			return this;
		}

		public Builder withPreposition(Preposition preposition) {
			this.preposition = preposition;
			return this;
		}

		public Builder withTurnDirection(Direction turnDirection) {
			this.turnDirection = turnDirection;
			return this;
		}

		public Builder withTarget(String target) {
			this.target = target;
			return this;
		}

		public Builder withMetersToNextDecisionPoint(int metersToNextDecisionPoint) {
			this.metersToNextDecisionPoint = Optional.of(metersToNextDecisionPoint);
			return this;
		}

		public Builder withTurnTriggerPosition(GeoJSONFeature<GeoJSONPoint> turnTriggerPosition) {
			this.turnTriggerPosition = turnTriggerPosition;
			return this;
		}

		public Builder withPreviewTriggerPosition(GeoJSONFeature<GeoJSONPoint> previewTriggerPosition) {
			this.previewTriggerPosition = Optional.ofNullable(previewTriggerPosition);
			return this;
		}

		public Builder withConfirmationTriggerPosition(
				GeoJSONFeature<GeoJSONPoint> confirmationTriggerPosition) {
			this.confirmationTriggerPosition = Optional.ofNullable(confirmationTriggerPosition);
			return this;
		}

		public NavigationInstruction build() {
			validate();
			return new NavigationInstruction(this);
		}

		private void validate() {
			Preconditions.checkArgument(landmarkType != null, "landmarkType is mandatory but missing");
			Preconditions.checkArgument(preposition != null, "preposition is mandatory but missing");
			Preconditions.checkArgument(turnDirection != null, "turnDirection is mandatory but missing");
			Preconditions.checkArgument(target != null, "target is mandatory but missing");
			Preconditions.checkArgument(turnTriggerPosition != null,
					"turnTriggerPosition is mandatory but missing");
		}
	}

}
