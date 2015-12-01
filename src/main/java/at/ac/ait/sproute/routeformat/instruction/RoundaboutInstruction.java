package at.ac.ait.sproute.routeformat.instruction;

import java.util.Optional;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class RoundaboutInstruction implements Instruction {

	public enum Type {
		ENTER, EXIT
	}

	public final Type type;
	public final Optional<String> ontoStreetName;
	public final Optional<FormOfWay> ontoFormOfWay;
	public final Optional<Integer> exitNr;

	public static RoundaboutInstruction newEnterInstruction(Optional<String> ontoStreetName,
			Optional<FormOfWay> ontoFormOfWay, int exitNr) {
		Type type = Type.ENTER;
		return new RoundaboutInstruction(type, ontoStreetName, ontoFormOfWay, Optional.of(exitNr));
	}

	public static RoundaboutInstruction newExitInstruction(Optional<String> ontoStreetName,
			Optional<FormOfWay> ontoFormOfWay) {
		Type type = Type.EXIT;
		Optional<Integer> exitNr = Optional.empty();
		return new RoundaboutInstruction(type, ontoStreetName, ontoFormOfWay, exitNr);
	}

	private RoundaboutInstruction(Type type, Optional<String> ontoStreetName, Optional<FormOfWay> ontoFormOfWay,
			Optional<Integer> exitNr) {
		if (!ontoStreetName.isPresent() && !ontoFormOfWay.isPresent())
			throw new IllegalArgumentException("at least one onto-type is required");

		this.type = type;
		this.ontoStreetName = ontoStreetName;
		this.ontoFormOfWay = ontoFormOfWay;
		this.exitNr = exitNr;
	}

}
