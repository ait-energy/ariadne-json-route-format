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
import at.ac.ait.ariadne.routeformat.Constants.VehicleAccessibility;

/**
 * Contains information about the mode of transport up to the detail level of
 * information about the operator (e.g. of a taxi), the details about a public
 * transport line or the type of vehicle (e.g. electric or not).
 * <p>
 * In its minimal form it consists of a {@link GeneralizedModeOfTransportType}.
 * <p>
 * Some standard modes of transport where additional information is seldom
 * required are provided, e.g. STANDARD_FOOT
 * <p>
 * {@link #equals(Object)} returns <code>true</code> for instances with the same
 * content.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class ModeOfTransport implements Validatable {

    public static final ModeOfTransport STANDARD_FOOT = createMinimal(DetailedModeOfTransportType.FOOT).setId("foot")
            .setColor("#377eb8");
    public static final ModeOfTransport STANDARD_BICYCLE = createMinimal(DetailedModeOfTransportType.BICYCLE)
            .setId("bicycle").setColor("#4daf4a");
    public static final ModeOfTransport STANDARD_MOTORCYCLE = createMinimal(DetailedModeOfTransportType.MOTORCYCLE)
            .setId("motorcycle").setColor("#ff7f00");
    public static final ModeOfTransport STANDARD_CAR = createMinimal(DetailedModeOfTransportType.CAR).setId("car")
            .setColor("#e41a1c");
    public static final ModeOfTransport STANDARD_TRANSFER = createMinimal(DetailedModeOfTransportType.TRANSFER)
            .setId("transfer").setColor("#a088a3");
    public static final ModeOfTransport STANDARD_PUBLIC_TRANSPORT = createMinimal(
            GeneralizedModeOfTransportType.PUBLIC_TRANSPORT).setId("publictransport").setColor("#984ea3");

    private GeneralizedModeOfTransportType generalizedType;
    private Optional<DetailedModeOfTransportType> detailedType = Optional.empty();
    private Optional<String> id = Optional.empty();
    private Optional<Service> service = Optional.empty();
    private Optional<Operator> operator = Optional.empty();
    private Optional<Boolean> electric = Optional.empty();
    private Optional<Integer> weightKg = Optional.empty();
    private Optional<Integer> widthMm = Optional.empty();
    private Optional<Integer> heightMm = Optional.empty();
    private Optional<Integer> lengthMm = Optional.empty();
    private Optional<Sharing> sharingType = Optional.empty();
    private Set<VehicleAccessibility> accessibility = new TreeSet<>();
    private Optional<String> color = Optional.empty();
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

    public Optional<Integer> getWeightKg() {
        return weightKg;
    }

    public Optional<Integer> getWidthMm() {
        return widthMm;
    }

    public Optional<Integer> getHeightMm() {
        return heightMm;
    }

    public Optional<Integer> getLengthMm() {
        return lengthMm;
    }

    /**
     * @return the sharing type. If empty the mode of transport is not shared.
     */
    public Optional<Sharing> getSharingType() {
        return sharingType;
    }

    public Set<VehicleAccessibility> getAccessibility() {
        return accessibility;
    }

    /**
     * @return the color of the mode of transport for visualization purposes
     *         (e.g. red for U1 in Vienna) in hash-prepended six-digit
     *         hexadacimal notation (e.g. #FF0000)
     */
    public Optional<String> getColor() {
        return color;
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

    public ModeOfTransport setWeightKg(Integer weightKg) {
        this.weightKg = Optional.ofNullable(weightKg);
        return this;
    }

    public ModeOfTransport setWidthMm(Integer widthMm) {
        this.widthMm = Optional.ofNullable(widthMm);
        return this;
    }

    public ModeOfTransport setHeightMm(Integer heightMm) {
        this.heightMm = Optional.ofNullable(heightMm);
        return this;
    }

    public ModeOfTransport setLengthMm(Integer lengthMm) {
        this.lengthMm = Optional.ofNullable(lengthMm);
        return this;
    }

    public ModeOfTransport setSharingType(Sharing sharingType) {
        this.sharingType = Optional.ofNullable(sharingType);
        return this;
    }

    public ModeOfTransport setAccessibility(Set<VehicleAccessibility> accessibility) {
        this.accessibility = new TreeSet<>(accessibility);
        return this;
    }

    public ModeOfTransport setColor(String color) {
        this.color = Optional.ofNullable(color);
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
        detailedType.ifPresent(d -> Preconditions.checkArgument(d.getGeneralizedType() == generalizedType,
                "mode of transpor types do not match"));
        service.ifPresent(s -> s.validate());
        operator.ifPresent(o -> o.validate());
        if (color.isPresent()) {
            String colorStr = color.get();
            String error = "color must be represented as hash-prepended six-digit hexadecimal String but was %s";
            Preconditions.checkArgument(colorStr.startsWith("#"), error, colorStr);
            Preconditions.checkArgument(colorStr.length() == 7, error, colorStr);
            try {
                Long.parseLong(colorStr.substring(1, 7), 16);
            } catch (NumberFormatException e) {
                Preconditions.checkArgument(false, error, colorStr);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessibility == null) ? 0 : accessibility.hashCode());
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((detailedType == null) ? 0 : detailedType.hashCode());
        result = prime * result + ((electric == null) ? 0 : electric.hashCode());
        result = prime * result + ((generalizedType == null) ? 0 : generalizedType.hashCode());
        result = prime * result + ((heightMm == null) ? 0 : heightMm.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lengthMm == null) ? 0 : lengthMm.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((service == null) ? 0 : service.hashCode());
        result = prime * result + ((sharingType == null) ? 0 : sharingType.hashCode());
        result = prime * result + ((weightKg == null) ? 0 : weightKg.hashCode());
        result = prime * result + ((widthMm == null) ? 0 : widthMm.hashCode());
        return result;
    }

    /**
     * @return <code>true</code> only if every aspect of the two
     *         {@link ModeOfTransport} is the same, also e.g. the color or
     *         properties {@link #getAdditionalInfo()}
     */
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
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
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
        if (heightMm == null) {
            if (other.heightMm != null)
                return false;
        } else if (!heightMm.equals(other.heightMm))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lengthMm == null) {
            if (other.lengthMm != null)
                return false;
        } else if (!lengthMm.equals(other.lengthMm))
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
        if (weightKg == null) {
            if (other.weightKg != null)
                return false;
        } else if (!weightKg.equals(other.weightKg))
            return false;
        if (widthMm == null) {
            if (other.widthMm != null)
                return false;
        } else if (!widthMm.equals(other.widthMm))
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
