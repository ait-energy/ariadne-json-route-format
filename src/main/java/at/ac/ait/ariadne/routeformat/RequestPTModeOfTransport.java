package at.ac.ait.ariadne.routeformat;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;

/**
 * Similar to a {@link RequestModeOfTransport} but only for public transport.
 * <p>
 * In its minimal form it only consists of a {@link ModeOfTransport}.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RequestPTModeOfTransport extends RequestModeOfTransport<RequestPTModeOfTransport> {

	private Set<DetailedModeOfTransportType> excludedPublicTransportModes = new TreeSet<>();

	// -- getters

	/**
	 * @return a set of public transport types which must not be used for
	 *         routing. It is guaranteed that for all returned mots
	 *         {@link DetailedModeOfTransportType#getGeneralizedType()} returns
	 *         {@link GeneralizedModeOfTransportType#PUBLIC_TRANSPORT}
	 */
	@JsonProperty
	public Set<DetailedModeOfTransportType> getExcludedPublicTransportModes() {
		return excludedPublicTransportModes;
	}

	// -- setters

	public RequestPTModeOfTransport setExcludedPublicTransportModes(
			Set<DetailedModeOfTransportType> excludedPublicTransportModes) {
		this.excludedPublicTransportModes = new TreeSet<>(excludedPublicTransportModes);
		return this;
	}

	// --

	public static RequestPTModeOfTransport createMinimal(ModeOfTransport modeOfTransport) {
		return new RequestPTModeOfTransport().setModeOfTransport(modeOfTransport);
	}

	@Override
	public void validate() {
		super.validate();
		Preconditions.checkArgument(
				modeOfTransport.getGeneralizedType().equals(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT),
				"only public transport allowed");
		for (DetailedModeOfTransportType mot : excludedPublicTransportModes) {
			Preconditions.checkArgument(mot.getGeneralizedType() == GeneralizedModeOfTransportType.PUBLIC_TRANSPORT,
					"only detailed public transport mots allowed when excluding public transport");
		}
	}

	@Override
	public String toString() {
		return super.toString() + " -> RequestPTModeOfTransport [excludedPublicTransportModes="
				+ excludedPublicTransportModes + "]";
	}

}
