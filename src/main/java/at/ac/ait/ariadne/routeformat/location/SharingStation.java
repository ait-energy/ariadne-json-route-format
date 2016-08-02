package at.ac.ait.ariadne.routeformat.location;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Operator;
import at.ac.ait.ariadne.routeformat.location.SharingStation.Builder2;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class SharingStation extends Location {

    private final Optional<String> name;
    private final Optional<String> id;
    private final List<GeneralizedModeOfTransportType> modesOfTransport;
    private final Optional<Operator> operator;

    public SharingStation(Builder<?> builder) {
        super(builder);
        this.name = builder.name;
        this.id = builder.id;
        this.modesOfTransport = builder.modesOfTransport;
        this.operator = builder.operator;
    }

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

    public static Builder<?> builder() {
        return new Builder2();
    }

    @Override
    public String toString() {
        return "SharingStation [name=" + name + ", id=" + id + ", modesOfTransport=" + modesOfTransport + ", operator="
                + operator + "]";
    }

    public static abstract class Builder<T extends Builder<T>> extends Location.Builder<T> {
        private Optional<String> name = Optional.empty();
        private Optional<String> id = Optional.empty();
        private List<GeneralizedModeOfTransportType> modesOfTransport = Collections.emptyList();
        private Optional<Operator> operator = Optional.empty();

        public T withName(String name) {
            this.name = Optional.ofNullable(name);
            return self();
        }

        public T withId(String id) {
            this.id = Optional.ofNullable(id);
            return self();
        }

        public T withModesOfTransport(List<GeneralizedModeOfTransportType> modesOfTransport) {
            this.modesOfTransport = ImmutableList.copyOf(modesOfTransport);
            return self();
        }

        public T withOperator(Operator operator) {
            this.operator = Optional.ofNullable(operator);
            return self();
        }

        public SharingStation build() {
            validate();
            return new SharingStation(this);
        }

        void validate() {
            super.validate();
            Preconditions.checkArgument(modesOfTransport.size() >= 1, "at least one mode of transport is required");
        }

    }

    static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
