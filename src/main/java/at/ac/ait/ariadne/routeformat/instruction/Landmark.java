package at.ac.ait.ariadne.routeformat.instruction;

import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.location.Location;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Landmark {

    public enum Preposition {
        BEFORE, AT, AFTER
    }

    private final Preposition preposition;
    private final Location location;

    public Preposition getPreposition() {
        return preposition;
    }

    public Location getLocation() {
        return location;
    }

    private Landmark(Builder builder) {
        super();
        this.preposition = builder.preposition;
        this.location = builder.location;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Preposition preposition, Location location) {
        return new Builder().withPreposition(preposition).withLocation(location);
    }

    @Override
    public String toString() {
        return "Landmark [preposition=" + preposition + ", location=" + location + "]";
    }

    public static class Builder {
        private Preposition preposition;
        private Location location;

        public Builder withPreposition(Preposition preposition) {
            this.preposition = preposition;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
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
