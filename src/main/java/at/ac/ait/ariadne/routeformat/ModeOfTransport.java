package at.ac.ait.ariadne.routeformat;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.Sharing;

/**
 * A {@link ModeOfTransport} at minimum specifies a
 * {@link GeneralizedModeOfTransportType}, but can also contain detailed
 * information about vehicles (e.g. buses, shared cars,..).
 * <p>
 * Some standard modes of transport where additional information is seldom
 * required are provided, e.g. STANDARD_FOOT.
 * <p>
 * {@link #equals(Object)} returns <code>true</code> for instances with the same
 * content.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class ModeOfTransport implements Validatable {

	public static final ModeOfTransport STANDARD_FOOT = createMinimal(DetailedModeOfTransportType.FOOT);
	public static final ModeOfTransport STANDARD_BICYCLE = createMinimal(DetailedModeOfTransportType.BICYCLE);
	public static final ModeOfTransport STANDARD_MOTORCYCLE = createMinimal(DetailedModeOfTransportType.MOTORCYCLE);
	public static final ModeOfTransport STANDARD_CAR = createMinimal(DetailedModeOfTransportType.CAR);
	public static final ModeOfTransport STANDARD_TRANSFER = createMinimal(DetailedModeOfTransportType.TRANSFER);
	public static final ModeOfTransport STANDARD_PUBLIC_TRANSPORT = createMinimal(
			GeneralizedModeOfTransportType.PUBLIC_TRANSPORT);

	private GeneralizedModeOfTransportType generalizedType;
	private Optional<DetailedModeOfTransportType> detailedType = Optional.empty();
	private Optional<String> id = Optional.empty();
	private Optional<Service> service = Optional.empty();
	private Optional<Operator> operator = Optional.empty();
	private Optional<Boolean> electric = Optional.empty();
	private Optional<Sharing> sharingType = Optional.empty();
	private Set<Constants.VehicleAccessibility> accessibility = new TreeSet<>();
	private Map<String, Object> additionalInfo = new TreeMap<>();

	// -- getters

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

	public Optional<Boolean> getElectric() {
		return electric;
	}

	/**
	 * @return the sharing type. If empty the mode of transport is not shared.
	 */
	public Optional<Sharing> getSharingType() {
		return sharingType;
	}

	public Set<Constants.VehicleAccessibility> getAccessibility() {
		return accessibility;
	}

	public Map<String, Object> getAdditionalInfo() {
		return additionalInfo;
	}

	// -- setters

	public ModeOfTransport setGeneralizedType(GeneralizedModeOfTransportType generalizedType) {
		this.generalizedType = generalizedType;
		return this;
	}

	/**
	 * Set detailed <b>and</b> generalized type in one step
	 */
	public ModeOfTransport setDetailedType(DetailedModeOfTransportType detailedType) {
		this.detailedType = Optional.ofNullable(detailedType);
		if (detailedType == null)
			generalizedType = null;
		else
			generalizedType = detailedType.getGeneralizedType();
		this.detailedType = Optional.ofNullable(detailedType);
		return this;
	}

	public ModeOfTransport setId(String id) {
		this.id = Optional.ofNullable(id);
		return this;
	}

	public ModeOfTransport setService(Service service) {
		this.service = Optional.ofNullable(service);
		return this;
	}

	public ModeOfTransport setOperator(Operator operator) {
		this.operator = Optional.ofNullable(operator);
		return this;
	}

	public ModeOfTransport setElectric(boolean electric) {
		this.electric = Optional.ofNullable(electric);
		return this;
	}

	public ModeOfTransport setSharingType(Sharing sharingType) {
		this.sharingType = Optional.ofNullable(sharingType);
		return this;
	}

	public ModeOfTransport setAccessibility(Set<Constants.VehicleAccessibility> accessibility) {
		this.accessibility = new TreeSet<>(accessibility);
		return this;
	}

	public ModeOfTransport setAdditionalInfo(Map<String, Object> additionalInfo) {
		this.additionalInfo = new TreeMap<>(additionalInfo);
		return this;
	}

	// --

	public static ModeOfTransport createMinimal(GeneralizedModeOfTransportType generalizedType) {
		return new ModeOfTransport().setGeneralizedType(generalizedType);
	}

	public static ModeOfTransport createMinimal(DetailedModeOfTransportType detailedType) {
		return new ModeOfTransport().setDetailedType(detailedType);
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(generalizedType != null, "generalizedType is mandatory but missing");
		if (detailedType.isPresent())
			Preconditions.checkArgument(detailedType.get().getGeneralizedType() == generalizedType,
					"mode of transpor types do not match");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessibility == null) ? 0 : accessibility.hashCode());
		result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
		result = prime * result + ((detailedType == null) ? 0 : detailedType.hashCode());
		result = prime * result + ((electric == null) ? 0 : electric.hashCode());
		result = prime * result + ((generalizedType == null) ? 0 : generalizedType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((sharingType == null) ? 0 : sharingType.hashCode());
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
		ModeOfTransport other = (ModeOfTransport) obj;
		if (accessibility == null) {
			if (other.accessibility != null)
				return false;
		} else if (!accessibility.equals(other.accessibility))
			return false;
		if (additionalInfo == null) {
			if (other.additionalInfo != null)
				return false;
		} else if (!additionalInfo.equals(other.additionalInfo))
			return false;
		if (detailedType == null) {
			if (other.detailedType != null)
				return false;
		} else if (!detailedType.equals(other.detailedType))
			return false;
		if (electric == null) {
			if (other.electric != null)
				return false;
		} else if (!electric.equals(other.electric))
			return false;
		if (generalizedType != other.generalizedType)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (sharingType == null) {
			if (other.sharingType != null)
				return false;
		} else if (!sharingType.equals(other.sharingType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(generalizedType.name());
		detailedType.ifPresent(d -> builder.append("-" + d));
		service.ifPresent(s -> builder.append(" " + s.toString()));
		return builder.toString();
	}
}
