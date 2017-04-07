package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(required = true)
    public Preposition getPreposition() {
        return preposition;
    }

    @JsonProperty(required = true)
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

    public static Landmark createMinimal(Preposition preposition, Location<?> location) {
        return new Landmark().setPreposition(preposition).setLocation(location);
    }

    public Landmark shallowCopy() {
        return new Landmark().setPreposition(preposition).setLocation(location).setDirection(direction.orElse(null))
                .setAdditionalInfo(additionalInfo);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(preposition != null, "preposition is mandatory but missing");
        Preconditions.checkArgument(location != null, "location is mandatory but missing");
        location.validate();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((preposition == null) ? 0 : preposition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Landmark other = (Landmark) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (direction == null) {
            if (other.direction != null)
                return false;
        } else if (!direction.equals(other.direction))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (preposition != other.preposition)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Landmark [preposition=" + preposition + ", location=" + location + ", direction=" + direction
                + ", additionalInfo=" + additionalInfo + "]";
    }

}
