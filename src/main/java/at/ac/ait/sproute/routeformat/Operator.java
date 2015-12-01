package at.ac.ait.sproute.routeformat;

import java.util.Optional;

import at.ac.ait.sproute.routeformat.Operator.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * Provider of a mode of transport (e.g. public transport provider)
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Operator {

	private String name;
	private Optional<String> phoneNumber;
	private Optional<String> website;
	private Optional<String> description;

	@JsonProperty(required = true)
	public String getName() {
		return name;
	}

	public Optional<String> getPhoneNumber() {
		return phoneNumber;
	}

	public Optional<String> getWebsite() {
		return website;
	}

	/**
	 * @return additional information to be provided to the user
	 */
	public Optional<String> getDescription() {
		return description;
	}

	private Operator(Builder builder) {
		this.name = builder.name;
		this.phoneNumber = builder.phoneNumber;
		this.website = builder.website;
		this.description = builder.description;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", phoneNumber=" + phoneNumber + ", website=" + website + ", description="
				+ description + "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String name;
		private Optional<String> phoneNumber = Optional.empty();
		private Optional<String> website = Optional.empty();
		private Optional<String> description = Optional.empty();

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withPhoneNumber(String phoneNumber) {
			this.phoneNumber = Optional.of(phoneNumber);
			return this;
		}

		public Builder withWebsite(String website) {
			this.website = Optional.of(website);
			return this;
		}

		public Builder withDescription(String description) {
			this.description = Optional.of(description);
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
