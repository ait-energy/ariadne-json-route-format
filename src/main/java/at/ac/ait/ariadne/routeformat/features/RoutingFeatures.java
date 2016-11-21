package at.ac.ait.ariadne.routeformat.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * Defines all features supported by a routing service.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoutingFeatures implements Validatable {

	private String name;
	private Optional<String> url = Optional.empty();
	private Optional<String> logoUrl = Optional.empty();
	private Optional<String> credits = Optional.empty();
	private List<Site> sites = new ArrayList<>();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

	/**
	 * @return a human-readable (potentially internationalized) name of the
	 *         routing service
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a URL e.g. to the website of the research project this routing
	 *         service is part of
	 */
	public Optional<String> getUrl() {
		return url;
	}

	/**
	 * @return a URL e.g. the logo of the research project this routing service
	 *         is part of
	 */
	public Optional<String> getLogoUrl() {
		return logoUrl;
	}

	/**
	 * @return credits / copyright / usage information about this service (plain
	 *         text or HTML)
	 */
	public Optional<String> getCredits() {
		return credits;
	}

	/**
	 * @return all {@link Site}s supported by this service
	 */
	public List<Site> getSites() {
		return sites;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	public RoutingFeatures setName(String name) {
		this.name = name;
		return this;
	}

	public RoutingFeatures setUrl(String url) {
		this.url = Optional.ofNullable(url);
		return this;
	}

	public RoutingFeatures setLogoUrl(String logoUrl) {
		this.logoUrl = Optional.ofNullable(logoUrl);
		return this;
	}

	public RoutingFeatures setCredits(String credits) {
		this.credits = Optional.ofNullable(credits);
		return this;
	}

	public RoutingFeatures setSites(List<Site> sites) {
		this.sites = new ArrayList<>(sites);
		return this;
	}

	public RoutingFeatures setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static RoutingFeatures createMinimal(String name, List<Site> sites) {
		return new RoutingFeatures().setName(name).setSites(sites);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(name != null, "name is mandatory but missing");
	}

	@Override
	public String toString() {
		return "RoutingFeatures [name=" + name + ", url=" + url + ", logoUrl=" + logoUrl + ", credits=" + credits
				+ ", sites=" + sites + ", additionalInfo=" + additionalInfo + "]";
	}

}
