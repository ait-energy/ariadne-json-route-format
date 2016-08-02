package at.ac.ait.ariadne.routeformat.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.location.PointOfInterest.Builder2;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class PointOfInterest extends Location {

    private final String poiType;
    private final String name;

    public String getPoiType() {
        return poiType;
    }

    public String getName() {
        return name;
    }

    public PointOfInterest(Builder<?> builder) {
        super(builder);
        this.poiType = builder.poiType;
        this.name = builder.name;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    @Override
    public String toString() {
        return "PointOfInterest [poiType=" + poiType + ", name=" + name + ", address=" + getAddress() + "]";
    }

    public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
        private String poiType;
        private String name;

        public T withPoiType(String poiType) {
            this.poiType = poiType;
            return self();
        }

        public T withName(String name) {
            this.name = name;
            return self();
        }

        public PointOfInterest build() {
            validate();
            return new PointOfInterest(this);
        }

        void validate() {
            super.validate();
            Preconditions.checkArgument(poiType != null, "poiType is mandatory but missing");
            Preconditions.checkArgument(name != null, "name is mandatory but missing");
        }

    }

    static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
