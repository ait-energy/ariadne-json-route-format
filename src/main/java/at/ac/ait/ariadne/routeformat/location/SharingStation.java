package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.location.SharingStation.Builder2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class SharingStation extends Location {

	private final String name;
	private final Optional<String> id;
	private final Set<GeneralizedModeOfTransportType> modesOfTransport;
	private final Optional<Operator> operator;

	public SharingStation(Builder<?> builder) {
		super(builder);
		this.name = builder.name;
		this.id = builder.id;
		this.modesOfTransport = builder.modesOfTransport;
		this.operator = builder.operator;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return an ID that should be visible to / of interest for the user
	 */
	public Optional<String> getId() {
		return id;
	}

	/**
	 * @return at least (and typically exactly) one mode of transport
	 */
	public Set<GeneralizedModeOfTransportType> getModesOfTransport() {
		return modesOfTransport;
	}

	public Optional<Operator> getOperator() {
		return operator;
	}

	public static Builder<?> builder() {
		return new Builder2();
	}

	public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
		private String name;
		private Optional<String> id = Optional.empty();
		private Set<GeneralizedModeOfTransportType> modesOfTransport = Collections.emptySet();
		private Optional<Operator> operator = Optional.empty();

		public T withName(String name) {
			this.name = name;
			return self();
		}

		public T withId(String id) {
			this.id = Optional.ofNullable(id);
			return self();
		}

		public T withModesOfTransport(Set<GeneralizedModeOfTransportType> modesOfTransport) {
			this.modesOfTransport = ImmutableSet.copyOf(modesOfTransport);
			return self();
		}

		public T withOperator(Operator operator) {
			this.operator = Optional.ofNullable(operator);
			return self();
		}

		public SharingStation build() {
			validate();
			return new SharingStation(this);
		}

		private void validate() {
			Preconditions.checkNotNull(name, "name is mandatory");
			Preconditions.checkArgument(modesOfTransport.size() >= 1, "at least one mode of transport is required");
		}

	}

	static class Builder2 extends Builder<Builder2> {
		@Override
		protected Builder2 self() {
			return this;
		}
	}

}
