package at.ac.ait.ariadne.routeformat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.ModeOfTransport.Builder;
import at.ac.ait.ariadne.routeformat.Sproute.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A {@link ModeOfTransport} at minimum specifies a {@link GeneralizedModeOfTransportType}, but can also contain
 * detailed information about vehicles (e.g. buses, shared cars,..).
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class ModeOfTransport {
	private final GeneralizedModeOfTransportType generalizedType;
	private final Optional<DetailedModeOfTransportType> detailedType;
	private final Optional<String> id;
	private final Optional<Service> service;
	private final Optional<Operator> operator;
	private final Optional<Boolean> electric;
	private final Optional<Boolean> shared;
	private final List<Sproute.Accessibility> accessibility;
	private final Map<String, Object> additionalInfo;

	@JsonProperty(required = true)
	public GeneralizedModeOfTransportType getGeneralizedType() {
		return generalizedType;
	}

	public Optional<DetailedModeOfTransportType> getDetailedType() {
		return detailedType;
	}

	/**
	 * E.g. number plate for cars, bicycle nr for bike-sharing,..
	 */
	public Optional<String> getId() {
		return id;
	}

	/**
	 * @return a service (in case this vehicle is part of public transport)
	 */
	public Optional<Service> getService() {
		return service;
	}

	public Optional<Operator> getOperator() {
		return operator;
	}

	public Optional<Boolean> isElectric() {
		return electric;
	}

	/**
	 * @return <code>true</code> for shared mobility services such as car/bike/ride-sharing
	 */
	public Optional<Boolean> isShared() {
		return shared;
	}

	public List<Sproute.Accessibility> getAccessibility() {
		return accessibility;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	private ModeOfTransport(Builder builder) {
		this.generalizedType = builder.generalizedType;
		this.detailedType = builder.detailedType;
		this.id = builder.id;
		this.service = builder.service;
		this.operator = builder.operator;
		this.electric = builder.electric;
		this.shared = builder.shared;
		this.accessibility = builder.accessibility;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private GeneralizedModeOfTransportType generalizedType;
		private Optional<DetailedModeOfTransportType> detailedType = Optional.empty();
		private Optional<String> id = Optional.empty();
		private Optional<Service> service = Optional.empty();
		private Optional<Operator> operator = Optional.empty();
		private Optional<Boolean> electric = Optional.empty();
		private Optional<Boolean> shared = Optional.empty();
		private List<Sproute.Accessibility> accessibility = Collections.emptyList();
		private Map<String, Object> additionalInfo = Collections.emptyMap();

		public Builder withGeneralizedType(GeneralizedModeOfTransportType generalizedType) {
			this.generalizedType = generalizedType;
			return this;
		}

		/**
		 * also sets the generalized type
		 */
		public Builder withDetailedType(DetailedModeOfTransportType detailedType) {
			this.detailedType = Optional.ofNullable(detailedType);
			if (detailedType == null)
				generalizedType = null;
			else
				generalizedType = detailedType.getGeneralizedType();
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
			return this;
		}

		public Builder withService(Service service) {
			this.service = Optional.ofNullable(service);
			return this;
		}

		public Builder withOperator(Operator operator) {
			this.operator = Optional.ofNullable(operator);
			return this;
		}

		public Builder withElectric(boolean electric) {
			this.electric = Optional.ofNullable(electric);
			return this;
		}

		public Builder withShared(boolean shared) {
			this.shared = Optional.ofNullable(shared);
			return this;
		}

		public Builder withAccessibility(List<Sproute.Accessibility> accessibility) {
			this.accessibility = ImmutableList.copyOf(accessibility);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public ModeOfTransport build() {
			validate();
			return new ModeOfTransport(this);
		}

		private void validate() {
			Preconditions.checkArgument(generalizedType != null, "generalizedType is mandatory but missing");
			if (detailedType.isPresent())
				Preconditions.checkArgument(detailedType.get().getGeneralizedType() == generalizedType,
						"mode of transpor types do not match");
		}
	}

}