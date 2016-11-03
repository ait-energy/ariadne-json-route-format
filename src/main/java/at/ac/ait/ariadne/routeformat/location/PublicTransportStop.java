package at.ac.ait.ariadne.routeformat.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.Constants;
import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.Coordinate;

/**
 * A {@link PublicTransportStop} is used in two contexts: as from or to position
 * of a {@link RouteSegment} and as an intermediate stop. In the case of from /
 * to position it contains information relevant for the route, i.e. the
 * accessibility information is relevant for the the way along the route and not
 * necessarily valid for the whole station. In the latter case (as an
 * intermediate stop) the information can be interpreted more generally.
 * <p>
 * In its minimal form it does not contain any additional attributes, but just
 * the information that this {@link Location} is a public transport stop.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class PublicTransportStop extends Location<PublicTransportStop> {

	private Optional<String> name = Optional.empty();
	private Optional<String> platform = Optional.empty();
	private Map<String, DetailedModeOfTransportType> relatedLines = new TreeMap<>();
	private List<Constants.Accessibility> accessibility = new ArrayList<>();

	// -- getters

	public Optional<String> getName() {
		return name;
	}

	/**
	 * @return name of the platform the user is arriving to / departing from
	 */
	public Optional<String> getPlatform() {
		return platform;
	}

	/**
	 * @return public transport lines the user can change to at this stop
	 *         (detailed public transport type and name, which may be an empty
	 *         string)
	 */
	public Map<String, DetailedModeOfTransportType> getRelatedLines() {
		return relatedLines;
	}

	public List<Constants.Accessibility> getAccessibility() {
		return accessibility;
	}

	// -- setters

	public PublicTransportStop setName(String name) {
		this.name = Optional.ofNullable(name);
		return this;
	}

	public PublicTransportStop setPlatform(String platform) {
		this.platform = Optional.ofNullable(platform);
		return this;
	}

	public PublicTransportStop setRelatedLines(Map<String, DetailedModeOfTransportType> relatedLines) {
		this.relatedLines = new TreeMap<>(relatedLines);
		return this;
	}

	public PublicTransportStop setAccessibility(List<Constants.Accessibility> accessibility) {
		this.accessibility = Lists.newArrayList(accessibility);
		return this;
	}

	// --

	public static PublicTransportStop createMinimal(Coordinate position) {
		return new PublicTransportStop().setCoordinate(position);
	}

	@Override
	public void validate() {
		super.validate();
		// no other requirements
	}

	@Override
	public String toString() {
		return super.toString() + " -> PublicTransportStop [name=" + name + ", platform=" + platform + ", relatedLines="
				+ relatedLines + ", accessibility=" + accessibility + "]";
	}

}
