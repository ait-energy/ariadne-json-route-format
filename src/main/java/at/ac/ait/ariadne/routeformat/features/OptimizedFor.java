package at.ac.ait.ariadne.routeformat.features;

import java.util.Map;
import com.google.common.base.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * Defines a criterion a route can be optimized for
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class OptimizedFor implements Validatable {

    private String id;
    private Optional<String> description = Optional.absent();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    /**
     * @return a unique ID
     */
    @JsonProperty(required = true)
    public String getId() {
        return id;
    }

    /**
     * @return human readable description text
     */
    public Optional<String> getDescription() {
        return description;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public OptimizedFor setId(String id) {
        this.id = id;
        return this;
    }

    public OptimizedFor setDescription(String description) {
        this.description = Optional.fromNullable(description);
        return this;
    }

    public OptimizedFor setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    public static OptimizedFor createMinimal(String id) {
        return new OptimizedFor().setId(id);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(id != null, "id is mandatory but missing");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        OptimizedFor other = (OptimizedFor) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OptimizedFor [id=" + id + ", description=" + description + ", additionalInfo=" + additionalInfo + "]";
    }

}
