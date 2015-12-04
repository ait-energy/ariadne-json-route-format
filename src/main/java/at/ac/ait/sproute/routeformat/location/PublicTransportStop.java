package at.ac.ait.sproute.routeformat.location;

import java.util.List;

import at.ac.ait.sproute.routeformat.location.PublicTransportStop.Builder2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class PublicTransportStop extends Location {

	private final String name;
	private final List<String> relatedLines;

	// FIXME barrierfree info?

	public PublicTransportStop(Builder<?> builder) {
		super(builder);
		this.name = builder.name;
		this.relatedLines = builder.relatedLines;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return public transport lines the user can change to at this stop
	 */
	public List<String> getRelatedLines() {
		return relatedLines;
	}

	public static Builder<?> builder() {
		return new Builder2();
	}

	public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
		private String name;
		private List<String> relatedLines;

		public T withName(String name) {
			this.name = name;
			return self();
		}

		public T withRelatedLines(List<String> relatedLines) {
			this.relatedLines = ImmutableList.copyOf(relatedLines);
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
