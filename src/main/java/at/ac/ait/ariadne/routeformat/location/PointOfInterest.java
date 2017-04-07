package at.ac.ait.ariadne.routeformat.location;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * A point of interest.
 * <p>
 * In its minimal form it does not contain any additional attributes, but just
 * the information that this {@link Location} is a point of interest.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class PointOfInterest extends Location<PointOfInterest> {

    private Optional<String> poiType = Optional.empty();
    private Optional<String> name = Optional.empty();

    // -- getters

    public Optional<String> getPoiType() {
        return poiType;
    }

    public Optional<String> getName() {
        return name;
    }

    // -- setters

    public PointOfInterest setPoiType(String poiType) {
        this.poiType = Optional.ofNullable(poiType);
        return this;
    }

    public PointOfInterest setName(String name) {
        this.name = Optional.ofNullable(name);
        return this;
    }

    // --

    public static PointOfInterest createMinimal(GeoJSONCoordinate position) {
        return new PointOfInterest().setCoordinate(position);
    }

    @Override
    public void validate() {
        super.validate();
        // no other requirements
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((poiType == null) ? 0 : poiType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PointOfInterest other = (PointOfInterest) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (poiType == null) {
            if (other.poiType != null)
                return false;
        } else if (!poiType.equals(other.poiType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " -> PointOfInterest [poiType=" + poiType + ", name=" + name + ", address="
                + getAddress() + "]";
    }

}
