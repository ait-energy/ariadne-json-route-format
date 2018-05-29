package at.ac.ait.ariadne.routeformat;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

/**
 * A {@link Service} typically represents a public transport service / line
 * <p>
 * In its minimal form it consists of a name.
 * <p>
 * {@link #equals(Object)} returns <code>true</code> for instances set the same
 * content.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_ABSENT)
public class Service implements Validatable {
    private String name;
    private Optional<String> towards = Optional.empty();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    /**
     * @return the official name of the service such as the line name (e.g. U3
     *         for a subway line) to be provided to the user
     */
    public String getName() {
        return name;
    }

    /**
     * @return the destination of the line as noted on the headsign of the
     *         vehicle
     */
    public Optional<String> getTowards() {
        return towards;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public Service setName(String name) {
        this.name = name;
        return this;
    }

    public Service setTowards(String towards) {
        this.towards = Optional.ofNullable(towards);
        return this;
    }

    public Service setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    public static Service createMinimal(String name) {
        return new Service().setName(name);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(name != null, "name is mandatory but missing");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((towards == null) ? 0 : towards.hashCode());
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
        Service other = (Service) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (towards == null) {
            if (other.towards != null)
                return false;
        } else if (!towards.equals(other.towards))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name == null ? "null" : name);
        towards.ifPresent(t -> builder.append(" -> " + t));
        return builder.toString();
    }

}
