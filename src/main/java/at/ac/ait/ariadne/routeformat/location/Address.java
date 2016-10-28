package at.ac.ait.ariadne.routeformat.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Joiner;

import at.ac.ait.ariadne.routeformat.Validatable;

/**
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class Address implements Validatable {

	private Optional<String> country = Optional.empty();
	private Optional<String> city = Optional.empty();
	private Optional<String> postCode = Optional.empty();
	private Optional<String> streetName = Optional.empty();
	private Optional<String> houseNumber = Optional.empty();
	private Map<String, String> additionalInfo = new TreeMap<>();

	// -- getters

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

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	public Address setCountry(String country) {
		this.country = Optional.ofNullable(country);
		return this;
	}

	public Address setCity(String city) {
		this.city = Optional.ofNullable(city);
		return this;
	}

	public Address setPostCode(String postCode) {
		this.postCode = Optional.ofNullable(postCode);
		return this;
	}

	public Address setStreetName(String streetName) {
		this.streetName = Optional.ofNullable(streetName);
		return this;
	}

	public Address setHouseNumber(String houseNumber) {
		this.houseNumber = Optional.ofNullable(houseNumber);
		return this;
	}

	public Address setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static Address create(String streetName, String houseNumber) {
		return new Address().setStreetName(streetName).setHouseNumber(houseNumber);
	}

	@Override
	public void validate() {
		// no minimum requirements, all fields can be empty
	}

	@Override
	public String toString() {
		List<String> fields = new ArrayList<>();
		country.ifPresent(f -> fields.add(f));
		city.ifPresent(f -> fields.add(f));
		postCode.ifPresent(f -> fields.add(f));
		streetName.ifPresent(f -> fields.add(f));
		houseNumber.ifPresent(f -> fields.add(f));
		return Joiner.on("|").join(fields);
	}

}
