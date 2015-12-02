package at.ac.ait.sproute.routeformat.instruction;

import java.util.Optional;

import at.ac.ait.sproute.routeformat.geojson.CoordinatePoint;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.sproute.routeformat.instruction.RoundaboutInstruction.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * Instructions for navigating a roundabout. The {@link #exitNr} is mandatory for {@link SubType#ENTER}.
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable text and what's mandatory / optional.
 * Elements ending with STRING are terminal (not defined any further).
 * 
 * <pre>
 * {@code
 * ROUNDABOUT_INSTRUCTION = ENTER | EXIT;
 * 
 * ENTER = "Take", EXIT_NUMBER, "exit", ["onto", NAME_OR_TYPE], "on roundabout", [STREET_NAME_STRING];
 * EXIT = "Exit roundabout", [STREET_NAME_STRING], "now onto", NAME_OR_TYPE;
 * 
 * EXIT_NUMBER = "first", "second",...;
 * NAME_OR_TYPE = STREET_NAME_STRING | FORM_OF_WAY_STRING;
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

	private RoundaboutInstruction(Builder builder) {
		super(builder.position, builder.previewTriggerPosition, builder.confirmationTriggerPosition);
		this.subType = builder.subType;
		this.roundaboutStreetName = builder.roundaboutStreetName;
		this.ontoStreetName = builder.ontoStreetName;
		this.ontoFormOfWay = builder.ontoFormOfWay;
		this.exitNr = builder.exitNr;
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

	public static class Builder {

		private SubType subType;
		private GeoJSONFeature<GeoJSONPoint> position;
		private Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition;
		private Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition;
		private Optional<String> roundaboutStreetName = Optional.empty();
		private Optional<String> ontoStreetName = Optional.empty();
		private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
		private Optional<Integer> exitNr = Optional.empty();

		public Builder withSubType(SubType subType) {
			this.subType = subType;
			return this;
		}

		public Builder withPosition(GeoJSONFeature<GeoJSONPoint> position) {
			this.position = position;
			return this;
		}

		public Builder withPreviewTriggerPosition(GeoJSONFeature<GeoJSONPoint> previewTriggerPosition) {
			this.previewTriggerPosition = Optional.of(previewTriggerPosition);
			return this;
		}

		public Builder withConfirmationTriggerPosition(GeoJSONFeature<GeoJSONPoint> confirmationTriggerPosition) {
			this.confirmationTriggerPosition = Optional.of(confirmationTriggerPosition);
			return this;
		}

		public Builder withRoundaboutStreetName(String roundaboutStreetName) {
			this.roundaboutStreetName = Optional.of(roundaboutStreetName);
			return this;
		}

		public Builder withOntoStreetName(String ontoStreetName) {
			this.ontoStreetName = Optional.of(ontoStreetName);
			return this;
		}

		public Builder withOntoFormOfWay(FormOfWay ontoFormOfWay) {
			this.ontoFormOfWay = Optional.of(ontoFormOfWay);
			return this;
		}

		public Builder withExitNr(int exitNr) {
			this.exitNr = Optional.of(exitNr);
			return this;
		}

		public RoundaboutInstruction build() {
			validate();
			return new RoundaboutInstruction(this);
		}

		private void validate() {
			Preconditions.checkNotNull(subType, "subType is mandatory");
			Preconditions.checkNotNull(position, "position is mandatory");
			if (subType.equals(SubType.ENTER))
				Preconditions.checkArgument(exitNr.isPresent(), "exit nr is mandatory for enter-instructions");
		}
	}

}
