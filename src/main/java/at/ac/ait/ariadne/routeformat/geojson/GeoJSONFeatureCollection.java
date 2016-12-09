package at.ac.ait.ariadne.routeformat.geojson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * A feature collection restricted to features of just one type. It may be
 * empty.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class GeoJSONFeatureCollection<T extends GeoJSONGeometryObject> implements Validatable {

    @JsonProperty(required = true)
    public final String type = GeoJSONUtil.getTypeName(this.getClass());
    private List<GeoJSONFeature<T>> features = new ArrayList<>();

    // -- getters

    @JsonInclude(Include.ALWAYS)
    @JsonProperty(required = true)
    public List<GeoJSONFeature<T>> getFeatures() {
        return features;
    }

    // -- setters

    public GeoJSONFeatureCollection<T> setFeatures(List<GeoJSONFeature<T>> features) {
        this.features = new ArrayList<>(features);
        return this;
    }

    // --

    public static <T extends GeoJSONGeometryObject> GeoJSONFeatureCollection<T> create(
            List<GeoJSONFeature<T>> features) {
        return new GeoJSONFeatureCollection<T>().setFeatures(features);
    }

    @Override
    public void validate() {
        features.forEach(f -> f.validate());
    }

    public List<String> toWKT() {
        return features.stream().map(f -> f.toWKT()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GeoJSONFeatureCollection [featuresWKT=" + toWKT() + "]";
    }

}