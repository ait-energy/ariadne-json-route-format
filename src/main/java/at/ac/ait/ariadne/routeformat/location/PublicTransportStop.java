package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class PublicTransportStop extends Location {

	private final String name;
	private final Map<String, DetailedModeOfTransportType> relatedLines;
	private final List<Sproute.Accessibility> accessibilityRestrictions;

	public PublicTransportStop(Builder<?> builder) {
		super(builder);
		this.name = builder.name;
		this.relatedLines = builder.relatedLines;
		this.accessibilityRestrictions = builder.accessibilityRestrictions;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return public transport lines the user can change to at this stop (detailed public transport type and name,
	 *         which may be an empty string)
	 */
	public Map<String, DetailedModeOfTransportType> getRelatedLines() {
		return relatedLines;
	}

	public List<Sproute.Accessibility> getAccessibilityRestrictions() {
		return accessibilityRestrictions;
	}

	public static Builder<?> builder() {
		return new Builder2();
	}

	public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
		private String name;
		private Map<String, DetailedModeOfTransportType> relatedLines = Collections.emptyMap();
		private List<Sproute.Accessibility> accessibilityRestrictions = Collections.emptyList();

		public T withName(String name) {
			this.name = name;
			return self();
		}

		public T withRelatedLines(Map<String, DetailedModeOfTransportType> relatedLines) {
			this.relatedLines = ImmutableMap.copyOf(relatedLines);
			return self();
		}

		public T withAccessibilityRestrictions(List<Sproute.Accessibility> accessibilityRestrictions) {
			this.accessibilityRestrictions = ImmutableList.copyOf(accessibilityRestrictions);
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
