package at.ac.ait.ariadne.routeformat.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.SiteFeature;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RoutingRequest;
import at.ac.ait.ariadne.routeformat.Validatable;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPoint;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;

/**
 * Details for a site, i.e. a region, that is supported by the routing service.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class Site implements Validatable {

    private String id;
    private Optional<String> name = Optional.empty();
    private List<OptimizedFor> optimizedFor = new ArrayList<>();
    private List<ModeOfTransport> modesOfTransport = new ArrayList<>();
    private Optional<GeoJSONFeature<GeoJSONPoint>> focusPoint = Optional.empty();
    private Optional<Integer> zoomLevel = Optional.empty();
    private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingPolygon = Optional.empty();
    private List<SiteFeature> features = new ArrayList<>();
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
     * @return a human readable name
     */
    public Optional<String> getName() {
        return name;
    }

    /**
     * @return a list of the the supported criteria for
     *         {@link RoutingRequest#getOptimizedFor()}
     */
    public List<OptimizedFor> getOptimizedFor() {
        return optimizedFor;
    }

    /**
     * @return all modes of transport that are supported for this site for use
     *         in {@link RoutingRequest#getModesOfTransport()}
     */
    public List<ModeOfTransport> getModesOfTransport() {
        return modesOfTransport;
    }

    /**
     * @return the point a map of the site should be focused / centered on
     */
    public Optional<GeoJSONFeature<GeoJSONPoint>> getFocusPoint() {
        return focusPoint;
    }

    /**
     * @return the zoom level to be used when displaying an overview map of the
     *         {@link Site}, see http://wiki.openstreetmap.org/wiki/Zoom_levels
     */
    public Optional<Integer> getZoomLevel() {
        return zoomLevel;
    }

    /**
     * @return a polygon defining the bounds of the site e.g. for displaying it
     *         on a map
     */
    public Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingPolygon() {
        return boundingPolygon;
    }

    public List<SiteFeature> getFeatures() {
        return features;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public Site setId(String id) {
        this.id = id;
        return this;
    }

    public Site setName(String name) {
        this.name = Optional.ofNullable(name);
        return this;
    }

    public Site setOptimizedFor(List<OptimizedFor> optimizedFor) {
        this.optimizedFor = new ArrayList<>(optimizedFor);
        return this;
    }

    public Site setModesOfTransport(List<ModeOfTransport> modesOfTransport) {
        this.modesOfTransport = new ArrayList<>(modesOfTransport);
        return this;
    }

    public Site setFocusPoint(GeoJSONFeature<GeoJSONPoint> focusPoint) {
        this.focusPoint = Optional.ofNullable(focusPoint);
        return this;
    }

    public Site setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = Optional.ofNullable(zoomLevel);
        return this;
    }

    public Site setBoundingPolygon(GeoJSONFeature<GeoJSONPolygon> boundingPolygon) {
        this.boundingPolygon = Optional.ofNullable(boundingPolygon);
        return this;
    }

    public Site setFeatures(List<SiteFeature> features) {
        this.features = new ArrayList<>(features);
        return this;
    }

    public Site setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    public static Site createMinimal(String id) {
        return new Site().setId(id);
    }

    public static Site createShallowCopy(Site s) {
        Site copy = createMinimal(s.getId()).setOptimizedFor(s.getOptimizedFor())
                .setModesOfTransport(s.getModesOfTransport()).setAdditionalInfo(s.getAdditionalInfo());
        s.getName().ifPresent(n -> copy.setName(n));
        s.focusPoint.ifPresent(f -> copy.setFocusPoint(f));
        s.zoomLevel.ifPresent(z -> copy.setZoomLevel(z));
        s.boundingPolygon.ifPresent(b -> copy.setBoundingPolygon(b));
        return copy;
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
        result = prime * result + ((boundingPolygon == null) ? 0 : boundingPolygon.hashCode());
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        result = prime * result + ((focusPoint == null) ? 0 : focusPoint.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((modesOfTransport == null) ? 0 : modesOfTransport.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((optimizedFor == null) ? 0 : optimizedFor.hashCode());
        result = prime * result + ((zoomLevel == null) ? 0 : zoomLevel.hashCode());
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
        Site other = (Site) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (boundingPolygon == null) {
            if (other.boundingPolygon != null)
                return false;
        } else if (!boundingPolygon.equals(other.boundingPolygon))
            return false;
        if (features == null) {
            if (other.features != null)
                return false;
        } else if (!features.equals(other.features))
            return false;
        if (focusPoint == null) {
            if (other.focusPoint != null)
                return false;
        } else if (!focusPoint.equals(other.focusPoint))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (modesOfTransport == null) {
            if (other.modesOfTransport != null)
                return false;
        } else if (!modesOfTransport.equals(other.modesOfTransport))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (optimizedFor == null) {
            if (other.optimizedFor != null)
                return false;
        } else if (!optimizedFor.equals(other.optimizedFor))
            return false;
        if (zoomLevel == null) {
            if (other.zoomLevel != null)
                return false;
        } else if (!zoomLevel.equals(other.zoomLevel))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Site [id=" + id + ", name=" + name + ", optimizedFor=" + optimizedFor + ", modesOfTransport="
                + modesOfTransport + ", focusPoint=" + focusPoint + ", zoomLevel=" + zoomLevel + ", boundingPolygon="
                + boundingPolygon + ", siteFeatures=" + features + ", additionalInfo=" + additionalInfo + "]";
    }

}
