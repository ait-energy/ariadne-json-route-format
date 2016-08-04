package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;

import at.ac.ait.ariadne.routeformat.Constants.Preposition;
import at.ac.ait.ariadne.routeformat.Constants.RelativeDirection;
import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Landmark {
    private final Preposition preposition;
    private final Location location;
    private final Optional<RelativeDirection> direction;
    private final Map<String, Object> additionalInfo;

    /**
     * @return the preposition describing the location of the landmark relative
     *         to the route (i.e. the point on the route an {@link Instruction}
     *         is valid for)
     */
    public Preposition getPreposition() {
        return preposition;
    }

    public Location getLocation() {
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

    private Landmark(Builder builder) {
        super();
        this.preposition = builder.preposition;
        this.location = builder.location;
        this.direction = builder.direction;
        this.additionalInfo = builder.additionalInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Preposition preposition, Location location) {
        return new Builder().withPreposition(preposition).withLocation(location);
    }

    @Override
    public String toString() {
        return "Landmark [preposition=" + preposition + ", location=" + location + ", direction=" + direction
                + ", additionalInfo=" + additionalInfo + "]";
    }

    public static class Builder {
        private Preposition preposition;
        private Location location;
        private Optional<RelativeDirection> direction = Optional.empty();
        private Map<String, Object> additionalInfo = Collections.emptyMap();;

        public Builder withPreposition(Preposition preposition) {
            this.preposition = preposition;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder withDirection(RelativeDirection direction) {
            this.direction = Optional.ofNullable(direction);
            return this;
        }

        public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
            this.additionalInfo = ImmutableSortedMap.copyOf(additionalInfo);
            return this;
        }

        public Landmark build() {
            validate();
            return new Landmark(this);
        }

        void validate() {
            Preconditions.checkArgument(preposition != null, "preposition is mandatory but missing");
            Preconditions.checkArgument(location != null, "location is mandatory but missing");
        }
    }

}
