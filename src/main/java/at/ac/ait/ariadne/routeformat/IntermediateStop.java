package at.ac.ait.ariadne.routeformat;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * An {@link IntermediateStop} represents a stop on a {@link RouteSegment} of
 * interest to the user, e.g. a public transport stop where the public transport
 * line the user is traveling with stops or a point of interest along a cycle
 * route.
 * <p>
 * In its minimal form it consists of a {@link Location}.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_ABSENT)
public class IntermediateStop implements Validatable {
    private Location<?> stop;
    private Optional<ZonedDateTime> plannedArrivalTime = Optional.empty();
    private Optional<ZonedDateTime> plannedDepartureTime = Optional.empty();
    private Optional<ZonedDateTime> estimatedArrivalTime = Optional.empty();
    private Optional<ZonedDateTime> estimatedDepartureTime = Optional.empty();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    public Location<?> getStop() {
        return stop;
    }

    /**
     * @return static time according to a time table
     */
    public Optional<String> getPlannedArrivalTime() {
        return plannedArrivalTime.map(time -> Utils.getDateTimeString(time));
    }

    /**
     * @return static time according to a time table
     */
    @JsonIgnore
    public Optional<ZonedDateTime> getPlannedArrivalTimeAsZonedDateTime() {
        return plannedArrivalTime;
    }

    /**
     * @return static time according to a time table
     */
    public Optional<String> getPlannedDepartureTime() {
        return plannedDepartureTime.map(time -> Utils.getDateTimeString(time));
    }

    /**
     * @return static time according to a time table
     */
    @JsonIgnore
    public Optional<ZonedDateTime> getPlannedDepartureTimeAsZonedDateTime() {
        return plannedDepartureTime;
    }

    /**
     * @return time estimated via real-time data
     */
    public Optional<String> getEstimatedArrivalTime() {
        return estimatedArrivalTime.map(time -> Utils.getDateTimeString(time));
    }

    /**
     * @return time estimated via real-time data
     */
    @JsonIgnore
    public Optional<ZonedDateTime> getEstimatedArrivalTimeAsZonedDateTime() {
        return estimatedArrivalTime;
    }

    /**
     * @return time estimated via real-time data
     */
    public Optional<String> getEstimatedDepartureTime() {
        return estimatedDepartureTime.map(time -> Utils.getDateTimeString(time));
    }

    /**
     * @return time estimated via real-time data
     */
    @JsonIgnore
    public Optional<ZonedDateTime> getEstimatedDepartureTimeAsZonedDateTime() {
        return estimatedDepartureTime;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public IntermediateStop setStop(Location<?> stop) {
        this.stop = stop;
        return this;
    }

    @JsonIgnore
    public IntermediateStop setPlannedArrivalTime(ZonedDateTime plannedArrivalTime) {
        this.plannedArrivalTime = Optional.ofNullable(plannedArrivalTime);
        return this;
    }

    @JsonProperty
    public IntermediateStop setPlannedArrivalTime(String plannedArrivalTime) {
        this.plannedArrivalTime = Optional
                .ofNullable(Utils.parseDateTime(plannedArrivalTime, "plannedArrivalTime"));
        return this;
    }

    @JsonIgnore
    public IntermediateStop setPlannedDepartureTime(ZonedDateTime plannedDepartureTime) {
        this.plannedDepartureTime = Optional.ofNullable(plannedDepartureTime);
        return this;
    }

    @JsonProperty
    public IntermediateStop setPlannedDepartureTime(String plannedDepartureTime) {
        this.plannedDepartureTime = Optional
                .ofNullable(Utils.parseDateTime(plannedDepartureTime, "plannedDepartureTime"));
        return this;
    }

    @JsonIgnore
    public IntermediateStop setEstimatedArrivalTime(ZonedDateTime estimatedArrivalTime) {
        this.estimatedArrivalTime = Optional.ofNullable(estimatedArrivalTime);
        return this;
    }

    @JsonProperty
    public IntermediateStop setEstimatedArrivalTime(String estimatedArrivalTime) {
        this.estimatedArrivalTime = Optional
                .ofNullable(Utils.parseDateTime(estimatedArrivalTime, "estimatedArrivalTime"));
        return this;
    }

    @JsonIgnore
    public IntermediateStop setEstimatedDepartureTime(ZonedDateTime estimatedDepartureTime) {
        this.estimatedDepartureTime = Optional.ofNullable(estimatedDepartureTime);
        return this;
    }

    @JsonProperty
    public IntermediateStop setEstimatedDepartureTime(String estimatedDepartureTime) {
        this.estimatedDepartureTime = Optional
                .ofNullable(Utils.parseDateTime(estimatedDepartureTime, "estimatedDepartureTime"));
        return this;
    }

    public IntermediateStop setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    public static IntermediateStop createMinimal(Location<?> stop) {
        return new IntermediateStop().setStop(stop);
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(stop != null, "stop is mandatory but missing");
        stop.validate();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((estimatedArrivalTime == null) ? 0 : estimatedArrivalTime.hashCode());
        result = prime * result + ((estimatedDepartureTime == null) ? 0 : estimatedDepartureTime.hashCode());
        result = prime * result + ((plannedArrivalTime == null) ? 0 : plannedArrivalTime.hashCode());
        result = prime * result + ((plannedDepartureTime == null) ? 0 : plannedDepartureTime.hashCode());
        result = prime * result + ((stop == null) ? 0 : stop.hashCode());
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
        IntermediateStop other = (IntermediateStop) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (estimatedArrivalTime == null) {
            if (other.estimatedArrivalTime != null)
                return false;
        } else if (!estimatedArrivalTime.equals(other.estimatedArrivalTime))
            return false;
        if (estimatedDepartureTime == null) {
            if (other.estimatedDepartureTime != null)
                return false;
        } else if (!estimatedDepartureTime.equals(other.estimatedDepartureTime))
            return false;
        if (plannedArrivalTime == null) {
            if (other.plannedArrivalTime != null)
                return false;
        } else if (!plannedArrivalTime.equals(other.plannedArrivalTime))
            return false;
        if (plannedDepartureTime == null) {
            if (other.plannedDepartureTime != null)
                return false;
        } else if (!plannedDepartureTime.equals(other.plannedDepartureTime))
            return false;
        if (stop == null) {
            if (other.stop != null)
                return false;
        } else if (!stop.equals(other.stop))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "IntermediateStop [stop=" + stop + ", plannedArrivalTime=" + plannedArrivalTime
                + ", plannedDepartureTime=" + plannedDepartureTime + ", estimatedArrivalTime=" + estimatedArrivalTime
                + ", estimatedDepartureTime=" + estimatedDepartureTime + ", additionalInfo=" + additionalInfo + "]";
    }

}
