package at.ac.ait.ariadne.routeformat.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * A sharing station, e.g. bike- or car-sharing.
 * <p>
 * In its minimal form it does not contain any additional attributes, but just
 * the information that this {@link Location} is a sharing station.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class SharingStation extends Location<SharingStation> {

    private Optional<String> name = Optional.empty();
    private Optional<String> id = Optional.empty();
    private List<GeneralizedModeOfTransportType> modesOfTransport = new ArrayList<>();
    private Optional<Operator> operator = Optional.empty();

    // -- getters

    public Optional<String> getName() {
        return name;
    }

    /**
     * @return an ID that should be visible to / of interest for the user
     */
    public Optional<String> getId() {
        return id;
    }

    /**
     * @return at least (and typically exactly) one mode of transport
     */
    public List<GeneralizedModeOfTransportType> getModesOfTransport() {
        return modesOfTransport;
    }

    public Optional<Operator> getOperator() {
        return operator;
    }

    // -- setters

    public SharingStation setName(String name) {
        this.name = Optional.ofNullable(name);
        return this;
    }

    public SharingStation setId(String id) {
        this.id = Optional.ofNullable(id);
        return this;
    }

    public SharingStation setModesOfTransport(List<GeneralizedModeOfTransportType> modesOfTransport) {
        this.modesOfTransport = Lists.newArrayList(modesOfTransport);
        return this;
    }

    public SharingStation setOperator(Operator operator) {
        this.operator = Optional.ofNullable(operator);
        return this;
    }

    // --

    public static SharingStation createMinimal(GeoJSONCoordinate position) {
        return new SharingStation().setCoordinate(position);
    }

    @Override
    public void validate() {
        super.validate();
        // no other requirements
    }

    @Override
    public String toString() {
        return super.toString() + " -> SharingStation [name=" + name + ", id=" + id + ", modesOfTransport="
                + modesOfTransport + ", operator=" + operator + "]";
    }

}
