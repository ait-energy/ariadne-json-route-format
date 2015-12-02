package at.ac.ait.sproute.routeformat.instruction;

import java.util.Optional;

import at.ac.ait.sproute.routeformat.instruction.RoundaboutInstruction.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * Instructions for navigating a roundabout. At least one of the onto* attributes is mandatory. The {@link #exitNr} is
 * mandatory for {@link SubType#ENTER}.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RoundaboutInstruction implements Instruction {

	public enum SubType {
		ENTER, EXIT
	}

	private final SubType subType;
	private final Optional<String> ontoStreetName;
	private final Optional<FormOfWay> ontoFormOfWay;
	private final Optional<Integer> exitNr;

	public SubType getSubType() {
		return subType;
	}

	public Optional<String> getOntoStreetName() {
		return ontoStreetName;
	}

	public Optional<FormOfWay> getOntoFormOfWay() {
		return ontoFormOfWay;
	}

	public Optional<Integer> getExitNr() {
		return exitNr;
	}

	private RoundaboutInstruction(Builder builder) {
		this.subType = builder.subType;
		this.ontoStreetName = builder.ontoStreetName;
		this.ontoFormOfWay = builder.ontoFormOfWay;
		this.exitNr = builder.exitNr;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder enterBuilder() {
		return new Builder().withSubType(SubType.ENTER);
	}

	public static Builder exitBuilder() {
		return new Builder().withSubType(SubType.EXIT);
	}

	public static class Builder {

		private SubType subType;
		private Optional<String> ontoStreetName = Optional.empty();
		private Optional<FormOfWay> ontoFormOfWay = Optional.empty();
		private Optional<Integer> exitNr = Optional.empty();

		public Builder withSubType(SubType subType) {
			this.subType = subType;
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
			Preconditions.checkArgument(ontoStreetName.isPresent() || ontoFormOfWay.isPresent(),
					"at least one onto-type is required");
			if (subType.equals(SubType.ENTER))
				Preconditions.checkArgument(exitNr.isPresent(), "exit nr is mandatory for enter-instructions");
		}
	}

}
