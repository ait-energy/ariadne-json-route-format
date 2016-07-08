package at.ac.ait.ariadne.routeformat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.RequestPTModeOfTransport.Builder2;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder2.class)
@JsonInclude(Include.NON_EMPTY)
public class RequestPTModeOfTransport extends RequestModeOfTransport {

    private final List<DetailedModeOfTransportType> excludedPublicTransportModes;

    public RequestPTModeOfTransport(Builder<?> builder) {
        super(builder);
        this.excludedPublicTransportModes = builder.excludedPublicTransportModes;
    }

    /**
     * @return a set of public transport types which must not be used for
     *         routing. It is guaranteed that for all returned mots
     *         {@link DetailedModeOfTransportType#getGeneralizedType()} returns
     *         {@link GeneralizedModeOfTransportType#PUBLIC_TRANSPORT}
     */
    @JsonProperty
    public List<DetailedModeOfTransportType> getExcludedPublicTransportModes() {
        return excludedPublicTransportModes;
    }

    public static Builder<?> builder() {
        return new Builder2();
    }

    public static abstract class Builder<T extends Builder<T>> extends RequestModeOfTransport.Builder<T> {
        private List<DetailedModeOfTransportType> excludedPublicTransportModes;

        public T withExcludedPublicTransportModes(List<DetailedModeOfTransportType> excludedPublicTransportModes) {
            this.excludedPublicTransportModes = ImmutableList.copyOf(excludedPublicTransportModes);
            return self();
        }

        public RequestPTModeOfTransport build() {
            validate();
            return new RequestPTModeOfTransport(this);
        }

        void validate() {
            super.validate();
            Preconditions.checkArgument(
                    modeOfTransport.getGeneralizedType().equals(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT),
                    "only public transport allowed");
            for (DetailedModeOfTransportType mot : excludedPublicTransportModes) {
                Preconditions.checkArgument(mot.getGeneralizedType() == GeneralizedModeOfTransportType.PUBLIC_TRANSPORT,
                        "only detailed public transport mots allowed when excluding public transport");
            }
        }
    }

    static class Builder2 extends Builder<Builder2> {
        @Override
        protected Builder2 self() {
            return this;
        }
    }

}
