package at.ac.ait.ariadne.routeformat;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.location.Address;

/**
 * {@link Operator} of a {@link ModeOfTransport} such as a public transport or
 * car/bike-sharing
 * <p>
 * In its minimal form it consists of a name.
 * <p>
 * {@link #equals(Object)} returns <code>true</code> for instances set the same
 * content.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class Operator implements Validatable {

	private String name;
	private Optional<String> id = Optional.empty();
	private Optional<Address> address = Optional.empty();
	private Optional<String> website = Optional.empty();
	private Optional<String> customerServiceEmail = Optional.empty();
	private Optional<String> customerServicePhone = Optional.empty();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

	@JsonProperty(required = true)
	public String getName() {
		return name;
	}

	public Optional<String> getId() {
		return id;
	}

	public Optional<Address> getAddress() {
		return address;
	}

	public Optional<String> getWebsite() {
		return website;
	}

	public Optional<String> getCustomerServiceEmail() {
		return customerServiceEmail;
	}

	public Optional<String> getCustomerServicePhone() {
		return customerServicePhone;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setter

	public Operator setName(String name) {
		this.name = name;
		return this;
	}

	public Operator setId(String id) {
		this.id = Optional.ofNullable(id);
		return this;
	}

	public Operator setAddress(Address address) {
		this.address = Optional.ofNullable(address);
		return this;
	}

	public Operator setWebsite(String website) {
		this.website = Optional.ofNullable(website);
		return this;
	}

	public Operator setCustomerServiceEmail(String customerServiceEmail) {
		this.customerServiceEmail = Optional.ofNullable(customerServiceEmail);
		return this;
	}

	public Operator setCustomerServicePhone(String customerServicePhone) {
		this.customerServicePhone = Optional.ofNullable(customerServicePhone);
		return this;
	}

	public Operator setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static Operator createMinimal(String name) {
		return new Operator().setName(name);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(name != null, "name is mandatory but missing");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((customerServiceEmail == null) ? 0 : customerServiceEmail.hashCode());
		result = prime * result + ((customerServicePhone == null) ? 0 : customerServicePhone.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
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
		Operator other = (Operator) obj;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (customerServiceEmail == null) {
			if (other.customerServiceEmail != null)
				return false;
		} else if (!customerServiceEmail.equals(other.customerServiceEmail))
			return false;
		if (customerServicePhone == null) {
			if (other.customerServicePhone != null)
				return false;
		} else if (!customerServicePhone.equals(other.customerServicePhone))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", id=" + id + ", address=" + address + ", website=" + website
				+ ", customerServiceEmail=" + customerServiceEmail + ", customerServicePhone=" + customerServicePhone
				+ ", additionalInfo=" + additionalInfo + "]";
	}

}
