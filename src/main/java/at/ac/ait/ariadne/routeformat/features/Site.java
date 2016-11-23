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
	private Map<String, String> optimizedFor = new TreeMap<>();
	private List<ModeOfTransport> modesOfTransport = new ArrayList<>();
	private Optional<GeoJSONFeature<GeoJSONPoint>> focusPoint = Optional.empty();
	private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingPolygon = Optional.empty();
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
	 * @return a map of the the supported values for
	 *         {@link RoutingRequest#getOptimizedFor()} as keys and a human
	 *         readable description as values
	 */
	public Map<String, String> getOptimizedFor() {
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
	 * @return a polygon defining the bounds of the site e.g. for displaying it
	 *         on a map
	 */
	public Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingPolygon() {
		return boundingPolygon;
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

	public Site setOptimizedFor(Map<String, String> optimizedFor) {
		this.optimizedFor = new TreeMap<>(optimizedFor);
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

	public Site setBoundingPolygon(GeoJSONFeature<GeoJSONPolygon> boundingPolygon) {
		this.boundingPolygon = Optional.ofNullable(boundingPolygon);
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
		s.boundingPolygon.ifPresent(b -> copy.setBoundingPolygon(b));
		return copy;
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(id != null, "id is mandatory but missing");
	}

	@Override
	public String toString() {
		return "Site [id=" + id + ", name=" + name + ", optimizedFor=" + optimizedFor + ", modesOfTransport="
				+ modesOfTransport + ", focusPoint=" + focusPoint + ", boundingPolygon=" + boundingPolygon
				+ ", additionalInfo=" + additionalInfo + "]";
	}

}
