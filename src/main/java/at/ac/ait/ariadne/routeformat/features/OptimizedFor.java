package at.ac.ait.ariadne.routeformat.features;

import java.util.Map;
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
    private Map<String, String> text = new TreeMap<>();
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
     * @return human readable description text, the key of the map is the
     *         language (RFC 1766), e.g. 'en' or 'de-AT'.
     */
    public Map<String, String> getText() {
        return text;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public OptimizedFor setId(String id) {
        this.id = id;
        return this;
    }

    public OptimizedFor setText(Map<String, String> text) {
        this.text = new TreeMap<>(text);
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
    public String toString() {
        return "OptimizedFor [id=" + id + ", text=" + text + ", additionalInfo=" + additionalInfo + "]";
    }

}
