package at.ac.ait.ariadne.routeformat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.AccessibilityRestriction;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.OutputFormat;
import at.ac.ait.ariadne.routeformat.features.Site;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * A {@link RoutingRequest} encapsulates typically required request parameters
 * for an intermodal routing service.
 * <p>
 * In its minimal form it consists of a service id, from and to locations, at
 * least one mode of transport, {@link #getOptimizedFor()}, and either a
 * departure or arrival time. The default constructor already sets
 * {@link RoutingRequest#DEFAULT_DEPARTURE_TIME} and
 * {@link RoutingRequest#DEFAULT_OPTIMIZED_FOR}.
 * <p>
 * Additional parameters, that will be different for each concrete routing
 * service, can be added via {@link #getAdditionalInfo()}: global parameters in
 * this class, parameters valid only for a certain mode of transport via
 * {@link RequestModeOfTransport#getAdditionalInfo()}. These additional
 * parameters could e.g. include:
 * 
 * <pre>
 * only_use_mots_provided_by = ÖBB
 * exclude_public_transport_line = WL_28A;WL_29A
 * exclude_provider = CAT
 * regional_bus = no
 * accepted_delay_minutes = 10
 * </pre>
 * 
 * <p>
 * For optional fields where no information is provided the routing service may
 * fall back to its specific defaults.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RoutingRequest implements Validatable {
    public static final String NOW = "NOW";
    public static final String DEFAULT_DEPARTURE_TIME = NOW;
    public static final String DEFAULT_OPTIMIZED_FOR = "TRAVELTIME";

    private Location<?> from;
    private List<Location<?>> via = new ArrayList<>();
    private Location<?> to;
    private List<RequestModeOfTransport<?>> modesOfTransport = new ArrayList<>();
    private Optional<RequestModeOfTransport<?>> startModeOfTransport = Optional.absent();
    private Optional<RequestModeOfTransport<?>> endModeOfTransport = Optional.absent();
    private String optimizedFor;
    private Optional<String> siteId = Optional.absent();
    private Optional<Integer> maximumTransfers = Optional.absent();
    private Optional<Date> departureTime = Optional.absent();
    private Optional<Date> arrivalTime = Optional.absent();
    private Set<AccessibilityRestriction> accessibilityRestrictions = new TreeSet<>();
    private List<String> languages = new ArrayList<>();
    private List<OutputFormat> outputFormats = new ArrayList<>();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    public RoutingRequest() {
        setDepartureTime(DEFAULT_DEPARTURE_TIME);
        setOptimizedFor(DEFAULT_OPTIMIZED_FOR);
    }

    // -- getters

    /**
     * replaced by {@link #getSiteId()}
     */
    @Deprecated
    @JsonProperty(required = false)
    public String getServiceId() {
        return getSiteId().or("");
    }

    @JsonProperty(required = true)
    public Location<?> getFrom() {
        return from;
    }

    public List<Location<?>> getVia() {
        return via;
    }

    @JsonProperty(required = true)
    public Location<?> getTo() {
        return to;
    }

    /**
     * One or more modes of transport and their options that will be / were used
     * for routing. In case of a single mode of transport unimodal routing is
     * requested, in case of several modes of transport intermodal routing is
     * requested.
     * <p>
     * In case of intermodal routing it is guaranteed that the returned set
     * contains a mode of transport with
     * {@link GeneralizedModeOfTransportType#FOOT}.
     */
    @JsonProperty(required = true)
    public List<RequestModeOfTransport<?>> getModesOfTransport() {
        return modesOfTransport;
    }

    /**
     * One of the modes of transport in {@link #getModesOfTransport()}, which
     * the route must start with. Only useful for intermodal routing, where
     * routes start and end with with foot by default.
     */
    @JsonProperty
    public Optional<RequestModeOfTransport<?>> getStartModeOfTransport() {
        return startModeOfTransport;
    }

    /**
     * One of the modes of transport in {@link #getModesOfTransport()}, which
     * the route must end with. Only useful for intermodal routing, where routes
     * start and end with with foot by default.
     */
    @JsonProperty
    public Optional<RequestModeOfTransport<?>> getEndModeOfTransport() {
        return endModeOfTransport;
    }

    /**
     * Criteria the route will be / was optimized for, e.g. shortest travel
     * time, which is also the default
     */
    @JsonProperty(required = true)
    public String getOptimizedFor() {
        return optimizedFor;
    }

    /**
     * @return the {@link Site} the routing request should be restricted to. If
     *         it is not set the routing service has to determine itself which
     *         site to use, e.g. through checking the bounding boxes of the
     *         available sites
     */
    public Optional<String> getSiteId() {
        return siteId;
    }

    /**
     * @return maximum number of transfers not including the first and last
     *         'transfer' to walking, i.e. walking to the a bike-sharing
     *         station, riding the bike, walking to the final destination counts
     *         as zero transfers (default = 3)
     */
    @JsonProperty
    public Optional<Integer> getMaximumTransfers() {
        return maximumTransfers;
    }

    /**
     * Requested departure time for the route. Mutual exclusive with
     * {@link #getArrivalTime()}, it is guaranteed that exactly one of the two
     * times is set.
     * <p>
     * If neither departure time nor arrival time were set in the builder a
     * departure time of 'now' is automatically added.
     * <p>
     * The supported formats are defined in Java 8 ZonedDateTime which uses ISO
     * 8601 with time zone. One example is "YYYY-MM-DDTHH:MMZ", where T is the
     * letter T, Z is the time zone (in either HH:MM, HHMM, HH format or the
     * letter Z for UTC). E.g. "2015-01-31T18:05+0100". As output the default
     * toString() of ZonedDateTime is used.
     */
    @JsonProperty
    public Optional<String> getDepartureTime() {
        if(departureTime.isPresent()) {
            return Optional.of(Utils.getAsZonedDateTimeString(departureTime.get()));
        }
        return Optional.absent();
    }

    /**
     * @see #getDepartureTime()
     */
    @JsonIgnore
    public Optional<Date> getDepartureTimeAsZonedDateTime() {
        return departureTime;
    }

    /**
     * Requested arrival time for the route. Mutual exclusive with
     * {@link #getDepartureTime()}, it is guaranteed that exactly one of the two
     * times is set.
     * <p>
     * The format is the same as for {@link #getDepartureTime()}.
     */
    @JsonProperty
    public Optional<String> getArrivalTime() {
        if(arrivalTime.isPresent()) {
            return Optional.of(Utils.getAsZonedDateTimeString(arrivalTime.get()));
        }
        return Optional.absent();
    }

    /**
     * @see #getArrivalTime()
     */
    @JsonIgnore
    public Optional<Date> getArrivalTimeAsZonedDateTime() {
        return arrivalTime;
    }

    public Set<AccessibilityRestriction> getAccessibilityRestrictions() {
        return accessibilityRestrictions;
    }

    /**
     * @return the requested languages of the user in the form of IETF BCP 47
     *         language tag strings (see {@link Locale#forLanguageTag(String)}).
     *         E.g. for navigation instructions or street or POI names. The
     *         first language is regarded the primary language.
     */
    public List<String> getLanguages() {
        return languages;
    }

    @JsonIgnore
    public List<Locale> getLanguagesAsLocales() {
        List<Locale> locales = new ArrayList<>();
        for(String language : languages)
            locales.add(Locale.forLanguageTag(language));
        return locales;
    }

    /**
     * @return the requested output formats
     */
    @JsonProperty
    public List<OutputFormat> getOutputFormats() {
        return outputFormats;
    }

    /**
     * @return a map of parameters to be considered during the routing process
     */
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    /**
     * superseded by {@link #setSiteId(String)}
     */
    @Deprecated
    public RoutingRequest setServiceId(String serviceId) {
        return setSiteId(serviceId);
    }

    public RoutingRequest setFrom(Location<?> from) {
        this.from = from;
        return this;
    }

    public RoutingRequest setVia(List<Location<?>> via) {
        this.via = new ArrayList<>(via);
        return this;
    }

    public RoutingRequest setTo(Location<?> to) {
        this.to = to;
        return this;
    }

    /**
     * Sets modes of transport. Automatically adds
     * {@link ModeOfTransport#STANDARD_FOOT} if it is missing for the intermodal
     * case.
     */
    public RoutingRequest setModesOfTransport(List<RequestModeOfTransport<?>> modesOfTransport) {
        this.modesOfTransport = new ArrayList<>(modesOfTransport);
        if (this.modesOfTransport.size() > 1) {
            Set<GeneralizedModeOfTransportType> types = new HashSet<>();
            for(RequestModeOfTransport<?> m : modesOfTransport)
                types.add(m.getModeOfTransport().getGeneralizedType());
            if (!types.contains(GeneralizedModeOfTransportType.FOOT)) {
                this.modesOfTransport.add(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_FOOT));
            }
        }
        return this;
    }

    public RoutingRequest setStartModeOfTransport(RequestModeOfTransport<?> startModeOfTransport) {
        this.startModeOfTransport = Optional.<RequestModeOfTransport<?>>fromNullable(startModeOfTransport);
        return this;
    }

    public RoutingRequest setEndModeOfTransport(RequestModeOfTransport<?> endModeOfTransport) {
        this.endModeOfTransport = Optional.<RequestModeOfTransport<?>>fromNullable(endModeOfTransport);
        return this;
    }

    public RoutingRequest setOptimizedFor(String optimizedFor) {
        this.optimizedFor = optimizedFor;
        return this;
    }

    public RoutingRequest setSiteId(String siteId) {
        this.siteId = Optional.fromNullable(siteId);
        return this;
    }

    public RoutingRequest setMaximumTransfers(Integer maximumTransfers) {
        this.maximumTransfers = Optional.fromNullable(maximumTransfers);
        return this;
    }

    /**
     * Sets departure time and erases arrival time (if departure time is not
     * <code>null</code>)
     */
    @JsonIgnore
    public RoutingRequest setDepartureTime(Date departureTime) {
        if (departureTime == null) {
            this.departureTime = Optional.absent();
        } else {
            this.departureTime = Optional.fromNullable(Utils.truncateToSeconds(departureTime));
            this.arrivalTime = Optional.absent();
        }
        return this;
    }

    /**
     * Sets departure time and erases arrival time (if departure time is not
     * <code>null</code>)
     * 
     * @param departureTime
     *            either "NOW" or a valid date time string
     */
    @JsonProperty
    public RoutingRequest setDepartureTime(String departureTime) {
        if (departureTime == null) {
            this.departureTime = Optional.absent();
        } else if (departureTime.equalsIgnoreCase(NOW)) {
            this.departureTime = Optional.of(Utils.truncateToSeconds(new Date()));
            this.arrivalTime = Optional.absent();
        } else {
            this.departureTime = Optional.of(Utils.parseZonedDateTime(departureTime, "departureTime"));
            this.arrivalTime = Optional.absent();
        }
        return this;
    }

    /**
     * Sets arrival time and erases departure time (if arrival time is not
     * <code>null</code>)
     */
    @JsonIgnore
    public RoutingRequest setArrivalTime(Date arrivalTime) {
        if (arrivalTime == null) {
            this.arrivalTime = Optional.absent();
        } else {
            this.arrivalTime = Optional.fromNullable(Utils.truncateToSeconds(arrivalTime));
            this.departureTime = Optional.absent();
        }
        return this;
    }

    /**
     * Sets arrival time and erases departure time (if arrival time is not
     * <code>null</code>)
     * 
     * @param arrivalTime
     *            either "NOW" or a valid date time string
     */
    @JsonProperty
    public RoutingRequest setArrivalTime(String arrivalTime) {
        if (arrivalTime == null) {
            this.arrivalTime = Optional.absent();
        } else if (arrivalTime.equalsIgnoreCase(NOW)) {
            this.arrivalTime = Optional.of(Utils.truncateToSeconds(new Date()));
            this.departureTime = Optional.absent();
        } else {
            this.arrivalTime = Optional.of(Utils.parseZonedDateTime(arrivalTime, "arrivalTime"));
            this.departureTime = Optional.absent();
        }
        return this;
    }

    public RoutingRequest setAccessibilityRestrictions(Set<AccessibilityRestriction> accessibilityRestrictions) {
        this.accessibilityRestrictions = new TreeSet<>(accessibilityRestrictions);
        return this;
    }

    public RoutingRequest setLanguages(List<String> languages) {
        this.languages = new ArrayList<>(languages);
        return this;
    }

    public RoutingRequest setOutputFormats(List<OutputFormat> outputFormats) {
        this.outputFormats = new ArrayList<>(outputFormats);
        return this;
    }

    public RoutingRequest setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    /**
     * Creates a {@link RoutingRequest} with default departure time and default
     * optimized for.
     */
    public static RoutingRequest createMinimal(Location<?> from, Location<?> to,
            List<RequestModeOfTransport<?>> modesOfTransport) {
        return new RoutingRequest().setFrom(from).setTo(to).setModesOfTransport(modesOfTransport);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(from != null, "from is mandatory but missing");
        from.validate();
        for(Location<?> v : via)
            v.validate();
        Preconditions.checkArgument(to != null, "to is mandatory but missing");
        to.validate();
        Preconditions.checkArgument(modesOfTransport != null && !modesOfTransport.isEmpty(),
                "modesOfTransport is mandatory but missing/empty");
        if (modesOfTransport.size() > 1) {
            Set<GeneralizedModeOfTransportType> types = new HashSet<>();
            for(RequestModeOfTransport<?> m : modesOfTransport)
                types.add(m.getModeOfTransport().getGeneralizedType());
            Preconditions.checkArgument(types.contains(GeneralizedModeOfTransportType.FOOT),
                    "intermodal routing without walking is not possible");
        }
        for(RequestModeOfTransport<?> m : modesOfTransport)
            m.validate();
        if(startModeOfTransport.isPresent()) {
            RequestModeOfTransport<?> s = startModeOfTransport.get();
            Preconditions.checkArgument(modesOfTransport.contains(s),
                    "startModeOfTransport is not contained in the available modes of transport");
            s.validate();
        }
        if(endModeOfTransport.isPresent()) {
            RequestModeOfTransport<?> e = endModeOfTransport.get();
            Preconditions.checkArgument(modesOfTransport.contains(e),
                    "endModeOfTransport is not contained in the available modes of transport");
            e.validate();
        }
        Preconditions.checkArgument(optimizedFor != null, "optimizedFor is mandatory but missing");
        Utils.checkPositiveIntegerOrEmpty(maximumTransfers, "maximumTransfers");
        Preconditions.checkArgument(
                (departureTime.isPresent() && !arrivalTime.isPresent())
                        || (!departureTime.isPresent() && arrivalTime.isPresent()),
                "either departureTime or arrivalTime must be set");
        Preconditions.checkArgument(!(departureTime.isPresent() && arrivalTime.isPresent()),
                "departureTime and arrivalTime are mutually exclusive, only one can be set at once");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessibilityRestrictions == null) ? 0 : accessibilityRestrictions.hashCode());
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
        result = prime * result + ((departureTime == null) ? 0 : departureTime.hashCode());
        result = prime * result + ((endModeOfTransport == null) ? 0 : endModeOfTransport.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((languages == null) ? 0 : languages.hashCode());
        result = prime * result + ((maximumTransfers == null) ? 0 : maximumTransfers.hashCode());
        result = prime * result + ((modesOfTransport == null) ? 0 : modesOfTransport.hashCode());
        result = prime * result + ((optimizedFor == null) ? 0 : optimizedFor.hashCode());
        result = prime * result + ((outputFormats == null) ? 0 : outputFormats.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startModeOfTransport == null) ? 0 : startModeOfTransport.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        result = prime * result + ((via == null) ? 0 : via.hashCode());
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
        RoutingRequest other = (RoutingRequest) obj;
        if (accessibilityRestrictions == null) {
            if (other.accessibilityRestrictions != null)
                return false;
        } else if (!accessibilityRestrictions.equals(other.accessibilityRestrictions))
            return false;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (arrivalTime == null) {
            if (other.arrivalTime != null)
                return false;
        } else if (!arrivalTime.equals(other.arrivalTime))
            return false;
        if (departureTime == null) {
            if (other.departureTime != null)
                return false;
        } else if (!departureTime.equals(other.departureTime))
            return false;
        if (endModeOfTransport == null) {
            if (other.endModeOfTransport != null)
                return false;
        } else if (!endModeOfTransport.equals(other.endModeOfTransport))
            return false;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (languages == null) {
            if (other.languages != null)
                return false;
        } else if (!languages.equals(other.languages))
            return false;
        if (maximumTransfers == null) {
            if (other.maximumTransfers != null)
                return false;
        } else if (!maximumTransfers.equals(other.maximumTransfers))
            return false;
        if (modesOfTransport == null) {
            if (other.modesOfTransport != null)
                return false;
        } else if (!modesOfTransport.equals(other.modesOfTransport))
            return false;
        if (optimizedFor == null) {
            if (other.optimizedFor != null)
                return false;
        } else if (!optimizedFor.equals(other.optimizedFor))
            return false;
        if (outputFormats == null) {
            if (other.outputFormats != null)
                return false;
        } else if (!outputFormats.equals(other.outputFormats))
            return false;
        if (siteId == null) {
            if (other.siteId != null)
                return false;
        } else if (!siteId.equals(other.siteId))
            return false;
        if (startModeOfTransport == null) {
            if (other.startModeOfTransport != null)
                return false;
        } else if (!startModeOfTransport.equals(other.startModeOfTransport))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        if (via == null) {
            if (other.via != null)
                return false;
        } else if (!via.equals(other.via))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RoutingRequest [from=" + from + ", via=" + via + ", to=" + to + ", modesOfTransport=" + modesOfTransport
                + ", startModeOfTransport=" + startModeOfTransport + ", endModeOfTransport=" + endModeOfTransport
                + ", optimizedFor=" + optimizedFor + ", siteId=" + siteId + ", maximumTransfers=" + maximumTransfers
                + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", accessibilityRestrictions="
                + accessibilityRestrictions + ", languages=" + languages + ", outputFormats=" + outputFormats
                + ", additionalInfo=" + additionalInfo + "]";
    }

}
