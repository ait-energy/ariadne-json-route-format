package at.ac.ait.ariadne.routeformat.location;

import java.util.Optional;

import at.ac.ait.ariadne.routeformat.location.Address.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Address {

	private final Optional<String> country;
	private final Optional<String> city;
	private final Optional<String> postCode;
	private final Optional<String> streetName;
	private final Optional<String> houseNumber;

	public Optional<String> getCountry() {
		return country;
	}

	public Optional<String> getCity() {
		return city;
	}

	public Optional<String> getPostCode() {
		return postCode;
	}

	public Optional<String> getStreetName() {
		return streetName;
	}

	public Optional<String> getHouseNumber() {
		return houseNumber;
	}

	private Address(Builder builder) {
		super();
		this.country = builder.country;
		this.city = builder.city;
		this.postCode = builder.postCode;
		this.streetName = builder.streetName;
		this.houseNumber = builder.houseNumber;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Optional<String> country = Optional.empty();
		private Optional<String> city = Optional.empty();
		private Optional<String> postCode = Optional.empty();
		private Optional<String> streetName = Optional.empty();
		private Optional<String> houseNumber = Optional.empty();

		public Builder withCountry(String country) {
			this.country = Optional.ofNullable(country);
			return this;
		}

		public Builder withCity(String city) {
			this.city = Optional.ofNullable(city);
			return this;
		}

		public Builder withPostCode(String postCode) {
			this.postCode = Optional.ofNullable(postCode);
			return this;
		}

		public Builder withStreetName(String streetName) {
			this.streetName = Optional.ofNullable(streetName);
			return this;
		}

		public Builder withHouseNumber(String houseNumber) {
			this.houseNumber = Optional.ofNullable(houseNumber);
			return this;
		}

		public Address build() {
			return new Address(this);
		}
	}
}
