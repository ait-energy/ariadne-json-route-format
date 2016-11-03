package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.Constants.ContinueDirection;
import at.ac.ait.ariadne.routeformat.Constants.CrossingInfrastructure;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * Instructions for crossing a road <b>away from a junction</b>, e.g. a
 * pedestrian or cyclist changing the side of the road at a zebra crossing or
 * bicycle crossing, but it is also possible that this just is a recommended
 * place to cross the road (without an marked crossing).
 * <p>
 * In its minimal form it only contains a position.
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
@JsonInclude(Include.NON_EMPTY)
public class RoadCrossingInstruction extends Instruction<RoadCrossingInstruction> {

	private Optional<CrossingInfrastructure> crossingInfrastructure = Optional.empty();
	private Optional<ContinueDirection> continueDirection = Optional.empty();

	// -- getters

	public Optional<CrossingInfrastructure> getCrossingInfrastructure() {
		return crossingInfrastructure;
	}

	public Optional<ContinueDirection> getContinueDirection() {
		return continueDirection;
	}

	// -- setters

	public RoadCrossingInstruction setCrossingInfrastructure(CrossingInfrastructure crossingInfrastructure) {
		this.crossingInfrastructure = Optional.ofNullable(crossingInfrastructure);
		return this;
	}

	public RoadCrossingInstruction setContinueDirection(ContinueDirection continueDirection) {
		this.continueDirection = Optional.ofNullable(continueDirection);
		return this;
	}

	// --

	public static RoadCrossingInstruction createMinimal(GeoJSONCoordinate position) {
		return new RoadCrossingInstruction().setPosition(position);
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public String toString() {
		return super.toString() + " -> RoadCrossingInstruction [crossingInfrastructure=" + crossingInfrastructure
				+ ", continueDirection=" + continueDirection + "]";
	}

}
