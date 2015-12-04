package at.ac.ait.sproute.routeformat;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.Service.Builder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Service {
	private final ServiceProvider provider;
	private final String name;
	private final Optional<String> id;
	private final Optional<String> towards;
	private final Optional<String> direction;
	private final Optional<String> platform;
	private final Map<String, String> additionalInfo;

	public ServiceProvider getProvider() {
		return provider;
	}

	/**
	 * @return the official name of the service such as the line name (e.g. U3 for a metro/underground line) to be
	 *         provided to the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return an identifier for internal use, most probably not useful for the end user
	 */
	public Optional<String> getId() {
		return id;
	}

	public Optional<String> getTowards() {
		return towards;
	}

	public Optional<String> getDirection() {
		return direction;
	}

	public Optional<String> getPlatform() {
		return platform;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public Service(Builder builder) {
		this.provider = builder.provider;
		this.name = builder.name;
		this.id = builder.id;
		this.towards = builder.towards;
		this.direction = builder.direction;
		this.platform = builder.platform;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private ServiceProvider provider;
		private String name;
		private Optional<String> id = Optional.empty();
		private Optional<String> towards = Optional.empty();
		private Optional<String> direction = Optional.empty();
		private Optional<String> platform = Optional.empty();
		private Map<String, String> additionalInfo = Collections.emptyMap();

		public Builder withProvider(ServiceProvider provider) {
			this.provider = provider;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.of(id);
			return this;
		}

		public Builder withTowards(String towards) {
			this.towards = Optional.of(towards);
			return this;
		}

		public Builder withDirection(String direction) {
			this.direction = Optional.of(direction);
			return this;
		}

		public Builder withPlatform(String platform) {
			this.platform = Optional.of(platform);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, String> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public Service build() {
			validate();
			return new Service(this);
		}

		private void validate() {
			Preconditions.checkArgument(provider != null, "provider is mandatory but missing");
			Preconditions.checkArgument(name != null, "name is mandatory but missing");
		}
	}

}
