package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.Sproute;
import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.location.PublicTransportStop.Builder2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A {@link PublicTransportStop} is used in two contexts: as from or to position of a {@link RouteSegment} and as an
 * intermediate stop. In the case of from / to position it contains information relevant for the route, i.e. the
 * accessibility information is relevant for the the way along the route and not necessarily valid for the whole
 * station. In the latter case (as an intermediate stop) the information can be interpreted more generally.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class PublicTransportStop extends Location {

	private final String name;
	private final Optional<String> platform;
	private final Map<String, DetailedModeOfTransportType> relatedLines;
	private final List<Sproute.Accessibility> accessibility;

	public PublicTransportStop(Builder<?> builder) {
		super(builder);
		this.name = builder.name;
		this.platform = builder.platform;
		this.relatedLines = builder.relatedLines;
		this.accessibility = builder.accessibility;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return name of the platform the user is arriving to / departing from
	 */
	public Optional<String> getPlatform() {
		return platform;
	}

	/**
	 * @return public transport lines the user can change to at this stop (detailed public transport type and name,
	 *         which may be an empty string)
	 */
	public Map<String, DetailedModeOfTransportType> getRelatedLines() {
		return relatedLines;
	}

	public List<Sproute.Accessibility> getAccessibility() {
		return accessibility;
	}

	public static Builder<?> builder() {
		return new Builder2();
	}

	public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
		private String name;
		private Optional<String> platform = Optional.empty();
		private Map<String, DetailedModeOfTransportType> relatedLines = Collections.emptyMap();
		private List<Sproute.Accessibility> accessibility = Collections.emptyList();

		public T withName(String name) {
			this.name = name;
			return self();
		}

		public T withPlatform(String platform) {
			this.platform = Optional.ofNullable(platform);
			return self();
		}

		public T withRelatedLines(Map<String, DetailedModeOfTransportType> relatedLines) {
			this.relatedLines = ImmutableMap.copyOf(relatedLines);
			return self();
		}

		public T withAccessibility(List<Sproute.Accessibility> accessibility) {
			this.accessibility = ImmutableList.copyOf(accessibility);
			return self();
		}

		public PublicTransportStop build() {
			validate();
			return new PublicTransportStop(this);
		}

		private void validate() {
			Preconditions.checkNotNull(name, "name is mandatory");
		}

	}

	static class Builder2 extends Builder<Builder2> {
		@Override
		protected Builder2 self() {
			return this;
		}
	}

}
