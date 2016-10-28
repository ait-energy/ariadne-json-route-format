package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.Preposition;
import at.ac.ait.ariadne.routeformat.Constants.RelativeDirection;
import at.ac.ait.ariadne.routeformat.Validatable;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * A landmark, i.e. a salient object in the real world, that is used in
 * navigation instructions.
 * <p>
 * In its minimal form it consists of a preposition and a {@link Location}, e.g.
 * "before the public transport stop".
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Landmark implements Validatable {
	private Preposition preposition;
	private Location<?> location;
	private Optional<RelativeDirection> direction = Optional.empty();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

	/**
	 * @return the preposition describing the location of the landmark relative
	 *         to the route (i.e. the point on the route an {@link Instruction}
	 *         is valid for)
	 */
	public Preposition getPreposition() {
		return preposition;
	}

	public Location<?> getLocation() {
		return location;
	}

	/**
	 * @return the detailed direction in which the landmark lies relative to the
	 *         route (i.e. the point on the route an {@link Instruction} is
	 *         valid for)
	 */
	public Optional<RelativeDirection> getDirection() {
		return direction;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	public Landmark setPreposition(Preposition preposition) {
		this.preposition = preposition;
		return this;
	}

	public Landmark setLocation(Location<?> location) {
		this.location = location;
		return this;
	}

	public Landmark setDirection(RelativeDirection direction) {
		this.direction = Optional.ofNullable(direction);
		return this;
	}

	public Landmark setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static Landmark createMinimalLandmark(Preposition preposition, Location<?> location) {
		return new Landmark().setPreposition(preposition).setLocation(location);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(preposition != null, "preposition is mandatory but missing");
		Preconditions.checkArgument(location != null, "location is mandatory but missing");
	}

	@Override
	public String toString() {
		return "Landmark [preposition=" + preposition + ", location=" + location + ", direction=" + direction
				+ ", additionalInfo=" + additionalInfo + "]";
	}

}
