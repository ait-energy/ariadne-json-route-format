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
    private Map<String, Object> additionalInfo = new TreeMap<>();

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

    public Map<String, Object> getAdditionalInfo() {
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

    public Address setAdditionalInfo(Map<String, Object> additionalInfo) {
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((houseNumber == null) ? 0 : houseNumber.hashCode());
        result = prime * result + ((postCode == null) ? 0 : postCode.hashCode());
        result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (houseNumber == null) {
            if (other.houseNumber != null)
                return false;
        } else if (!houseNumber.equals(other.houseNumber))
            return false;
        if (postCode == null) {
            if (other.postCode != null)
                return false;
        } else if (!postCode.equals(other.postCode))
            return false;
        if (streetName == null) {
            if (other.streetName != null)
                return false;
        } else if (!streetName.equals(other.streetName))
            return false;
        return true;
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
