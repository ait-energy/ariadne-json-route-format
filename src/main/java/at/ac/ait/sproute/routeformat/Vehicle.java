package at.ac.ait.sproute.routeformat;

import java.util.Optional;

import at.ac.ait.sproute.routeformat.Vehicle.Builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder=Builder.class)
public class Vehicle {
	
	public enum VehicleType { 
		// "classic" public transport
		TRAIN, LIGHTRAIL, SUBWAY, MONORAIL, TRAM, BUS, TROLLEYBUS, AERIALWAY, FERRY,
		// individual transport
		PRIVATE_BICYCLE, SHARED_BICYCLE, PRIVATE_MOTORCYCLE, SHARED_MOTORCYCLE, PRIVATE_CAR, SHARED_CAR, TAXI, CALL_TAXI
	}
	
	private VehicleType type;
	private Optional<Boolean> electric;
	private Optional<String> id;
	
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
