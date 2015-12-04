package at.ac.ait.sproute.routeformat;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.Sproute.VehicleType;
import at.ac.ait.sproute.routeformat.Vehicle.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Vehicle {

	private final VehicleType type;
	private final Optional<String> id;
	private final Optional<Service> service;
	private final Optional<Boolean> electric;
	private final Optional<Boolean> shared;
	private final Map<String, String> additionalInfo;

	@JsonProperty(required = true)
	public VehicleType getType() {
		return type;
	}

	/**
	 * E.g. number plate for cars, bicycle nr for bike-sharing,..
	 */
	public Optional<String> getId() {
		return id;
	}

	/**
	 * @return a service in case this vehicle is part of public transport
	 */
	public Optional<Service> getService() {
		return service;
	}

	public Optional<Boolean> isElectric() {
		return electric;
	}

	/**
	 * @return <code>true</code> in case of shared mobility services such as car sharing or bike sharing
	 */
	public Optional<Boolean> isShared() {
		return shared;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	private Vehicle(Builder builder) {
		this.type = builder.type;
		this.id = builder.id;
		this.service = builder.service;
		this.electric = builder.electric;
		this.shared = builder.shared;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private VehicleType type;
		private Optional<String> id = Optional.empty();
		private Optional<Service> service = Optional.empty();
		private Optional<Boolean> electric = Optional.empty();
		private Optional<Boolean> shared = Optional.empty();
		private Map<String, String> additionalInfo = Collections.emptyMap();

		public Builder withType(VehicleType type) {
			this.type = type;
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.of(id);
			return this;
		}

		public Builder withService(Service service) {
			this.service = Optional.of(service);
			return this;
		}

		public Builder withElectric(boolean electric) {
			this.electric = Optional.of(electric);
			return this;
		}

		public Builder withShared(boolean shared) {
			this.shared = Optional.of(shared);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, String> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public Vehicle build() {
			validate();
			return new Vehicle(this);
		}

		private void validate() {
			Preconditions.checkArgument(type != null, "type is mandatory but missing");
		}
	}

}
