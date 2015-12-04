package at.ac.ait.sproute.routeformat;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.ServiceProvider.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Provider of a mode of transport (e.g. public transport provider)
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class ServiceProvider {

	private final String name;
	private final Optional<String> id;
	private final Optional<String> phoneNumber;
	private final Optional<String> website;
	private final Map<String, String> additionalInfo;

	@JsonProperty(required = true)
	public String getName() {
		return name;
	}

	public Optional<String> getId() {
		return id;
	}

	public Optional<String> getPhoneNumber() {
		return phoneNumber;
	}

	public Optional<String> getWebsite() {
		return website;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	private ServiceProvider(Builder builder) {
		this.name = builder.name;
		this.id = builder.id;
		this.phoneNumber = builder.phoneNumber;
		this.website = builder.website;
		this.additionalInfo = builder.additionalInfo;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", phoneNumber=" + phoneNumber + ", website=" + website + ", description="
				+ additionalInfo + "]";
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String name;
		private Optional<String> id = Optional.empty();
		private Optional<String> phoneNumber = Optional.empty();
		private Optional<String> website = Optional.empty();
		private Map<String, String> additionalInfo = Collections.emptyMap();

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
			return this;
		}

		public Builder withPhoneNumber(String phoneNumber) {
			this.phoneNumber = Optional.ofNullable(phoneNumber);
			return this;
		}

		public Builder withWebsite(String website) {
			this.website = Optional.ofNullable(website);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, String> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public ServiceProvider build() {
			validate();
			return new ServiceProvider(this);
		}

		private void validate() {
			Preconditions.checkArgument(name != null, "name is mandatory but missing");
		}
	}

}
