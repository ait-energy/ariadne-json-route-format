package at.ac.ait.ariadne.routeformat;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.Operator.Builder;
import at.ac.ait.ariadne.routeformat.location.Address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * {@link Operator} of a {@link ModeOfTransport} such as a public transport or
 * car/bike-sharing
 * <p>
 * {@link #equals(Object)} returns <code>true</code> for instances with the same
 * content.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Operator {

	private final String name;
	private final Optional<String> id;
	private final Optional<Address> address;
	private final Optional<String> website;
	private final Optional<String> customerServiceEmail;
	private final Optional<String> customerServicePhone;
	private final Map<String, Object> additionalInfo;

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

	private Operator(Builder builder) {
		this.name = builder.name;
		this.id = builder.id;
		this.address = builder.address;
		this.website = builder.website;
		this.customerServiceEmail = builder.customerServiceEmail;
		this.customerServicePhone = builder.customerServicePhone;
		this.additionalInfo = builder.additionalInfo;
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String name;
		private Optional<String> id = Optional.empty();
		private Optional<Address> address = Optional.empty();
		private Optional<String> website = Optional.empty();
		private Optional<String> customerServiceEmail = Optional.empty();
		private Optional<String> customerServicePhone = Optional.empty();
		private Map<String, Object> additionalInfo = Collections.emptyMap();

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
			return this;
		}

		public Builder withAddress(Address address) {
			this.address = Optional.ofNullable(address);
			return this;
		}

		public Builder withWebsite(String website) {
			this.website = Optional.ofNullable(website);
			return this;
		}

		public Builder withCustomerServiceEmail(String customerServiceEmail) {
			this.customerServiceEmail = Optional.ofNullable(customerServiceEmail);
			return this;
		}

		public Builder withCustomerServicePhone(String customerServicePhone) {
			this.customerServicePhone = Optional.ofNullable(customerServicePhone);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public Operator build() {
			validate();
			return new Operator(this);
		}

		private void validate() {
			Preconditions.checkArgument(name != null, "name is mandatory but missing");
		}
	}

}
