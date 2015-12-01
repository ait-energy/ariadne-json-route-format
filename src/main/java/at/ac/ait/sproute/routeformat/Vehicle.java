package at.ac.ait.sproute.routeformat;

import at.ac.ait.sproute.routeformat.Vehicle.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Vehicle { // FIXME split to service and vehicle -> email bilal 2015-11-27

	public enum VehicleType { // FIXME Vehicle
		// "classic" public transport
		TRAIN, LIGHTRAIL, SUBWAY, MONORAIL, TRAM, BUS, TROLLEYBUS, AERIALWAY, FERRY,
		// individual transport
		PRIVATE_BICYCLE, SHARED_BICYCLE, PRIVATE_MOTORCYCLE, SHARED_MOTORCYCLE, PRIVATE_CAR, SHARED_CAR, TAXI, CALL_TAXI
	} // FIXME Service SHARED or PRIVATE

	private VehicleType type; // FIXME Vehicle
	private Optional<Boolean> electric; // FIXME Vehicle
	private Optional<String> id; // FIXME Vehicle

	// FIXME additional vehicle info specially for public transport vehicles (, information to barrierFree) Service info
	// : name, towards, direction, platform

	@JsonProperty(required = true)
	public VehicleType getType() {
		return type;
	}

	public Optional<Boolean> isElectric() {
		return electric;
	}

	/**
	 * E.g. number plate for cars, bicycle nr for bike-sharing,..
	 */
	public Optional<String> getId() {
		return id;
	}

	private Vehicle(Builder b) {
		this.type = b.type;
		this.electric = b.electric;
		this.id = b.id;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private VehicleType type;
		private Optional<Boolean> electric = Optional.empty();
		private Optional<String> id = Optional.empty();

		public Builder withType(VehicleType type) {
			this.type = type;
			return this;
		}

		public Builder withElectric(boolean electric) {
			this.electric = Optional.of(electric);
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
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
