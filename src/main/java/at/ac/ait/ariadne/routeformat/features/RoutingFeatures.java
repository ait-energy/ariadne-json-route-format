package at.ac.ait.ariadne.routeformat.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * Defines all features supported by a routing service.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoutingFeatures implements Validatable {

	private List<Site> sites = new ArrayList<>();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

	public List<Site> getSites() {
		return sites;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	public RoutingFeatures setSites(List<Site> sites) {
		this.sites = new ArrayList<>(sites);
		return this;
	}

	public RoutingFeatures setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static RoutingFeatures create(List<Site> sites) {
		return new RoutingFeatures().setSites(sites);
	}

	@Override
	public void validate() {
	}

	@Override
	public String toString() {
		return "RoutingFeatures [sites=" + sites + ", additionalInfo=" + additionalInfo + "]";
	}

}
