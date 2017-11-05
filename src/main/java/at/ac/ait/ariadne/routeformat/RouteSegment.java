package at.ac.ait.ariadne.routeformat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.Accessibility;
import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.instruction.Instruction;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * A {@link RouteSegment} is a part of a route that is traveled with a single
 * {@link ModeOfTransport}.
 * <p>
 * In its minimal form {@link #getNr()}, {@link #getFrom()}, {@link #getTo()},
 * {@link #getDistanceMeters()}, {@link #getDurationSeconds()},
 * {@link #getModeOfTransport()}, {@link #getStartTime()},
 * {@link #getEndTime()}, and at least one of the geometry types are present.
 * Since this is a lot no <code>createMinimal</code> method is offered as its
 * invocations would be hardly readable.
 * <p>
 * <b>A note on public transport:</b> transfers between two lines (e.g. from bus
 * 28A to subway U6) can be represented in two variants.
 * <ol>
 * <li><b>PT_DETAILED:</b> in this (preferred) variant a segment with
 * {@link DetailedModeOfTransportType#TRANSFER} is inserted before, between, and
 * after public transport segments. Such a transfer segment represents the
 * transfer within a (logical) public transport station including waiting times
 * for the next line. It contains the walking distance in the station (
 * {@link #getDistanceMeters()}), the walking time (
 * {@link #getDurationSeconds()} minus alighting time), and the waiting time for
 * the next public transport line ({@link #getAlightingSeconds()}). The public
 * transport segments themselves only contain the ride time (without any
 * boarding or alighting time). When using this variant it is much easier to
 * differentiate between transfer and ride, e.g. regarding accessibility.</li>
 * <li><b>PT_CONDENSED:</b> this variant is more compact but less detailed. Due
 * to the missing transfer segments transfer and waiting time must be added to
 * the public transport segment itself. It then contains the waiting time and
 * optionally the the transfer time to the segment (
 * {@link #getBoardingSeconds()}), the transfer time to the next segment (
 * {@link #getAlightingSeconds()}), and of course the ride time (
 * {@link #getDurationSeconds()} minus boarding and alighting time).</li>
 * </ol>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class RouteSegment implements Validatable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteSegment.class);

    private Integer nr;
    private Location<?> from;
    private Location<?> to;
    private Integer distanceMeters;
    private Integer durationSeconds;
    private ModeOfTransport modeOfTransport;
    private Optional<Integer> boardingSeconds = Optional.absent();
    private Optional<Integer> alightingSeconds = Optional.absent();
    private Date startTime = null;
    private Date endTime = null;
    private List<IntermediateStop> intermediateStops = new ArrayList<>();
    private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.absent();
    private Optional<String> geometryEncodedPolyLine = Optional.absent();
    private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.absent();
    private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.absent();
    private List<Instruction<?>> navigationInstructions = new ArrayList<>();
    private List<Accessibility> accessibility = new ArrayList<>();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

    /** number of the segment in the route (starts with 1) */
    @JsonProperty(required = true)
    public int getNr() {
        return nr;
    }

    @JsonProperty(required = true)
    public Location<?> getFrom() {
        return from;
    }

    @JsonProperty(required = true)
    public Location<?> getTo() {
        return to;
    }

    @JsonProperty(required = true)
    public int getDistanceMeters() {
        return distanceMeters;
    }

    /**
     * the total duration for this {@link RouteSegment} including
     * {@link #getBoardingSeconds()} and {@link #getAlightingSeconds()}
     */
    @JsonProperty(required = true)
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * the pure travel time for this {@link RouteSegment} excluding
     * {@link #getBoardingSeconds()} and {@link #getAlightingSeconds()}
     * <p>
     * Note: not exported to JSON since it can be inferred
     */
    @JsonIgnore
    public int getTravelTimeSeconds() {
        return durationSeconds - boardingSeconds.or(0) - alightingSeconds.or(0);
    }

    @JsonProperty(required = true)
    public ModeOfTransport getModeOfTransport() {
        return modeOfTransport;
    }

    /**
     * the number of seconds it takes to board (or wait for) the mode of
     * transport, e.g. estimated time it takes to walk to your bicycle or car
     * and unlock it, average time to hail a taxi, waiting time in case this is
     * a public transport segment (PT_CONDENSED),..
     */
    public Optional<Integer> getBoardingSeconds() {
        return boardingSeconds;
    }

    /**
     * the number of seconds it takes to alight the mode of transport, e.g.
     * estimated time it takes to look for a parking spot for your bicycle or
     * car and lock/park it, average time to pay and leave a taxi, waiting time
     * for the next public transport line in case this is a
     * {@link DetailedModeOfTransportType#TRANSFER} segment (PT_DETAILED),
     * transfer (walking) time in case this is a public transport segment
     * (PT_CONDENSED),..
     */
    public Optional<Integer> getAlightingSeconds() {
        return alightingSeconds;
    }

    /**
     * the start time of this {@link RouteSegment}, i.e. when boarding starts
     */
    public String getStartTime() {
        return Utils.getAsZonedDateTimeString(startTime);
    }

    @JsonIgnore
    public Date getStartTimeAsZonedDateTime() {
        return startTime;
    }

    /**
     * the departure time of this {@link RouteSegment}, i.e. after boarding is
     * finished
     * <p>
     * Note: not exported to JSON since it can be inferred
     */
    @JsonIgnore
    public Date getDepartureTimeAsZonedDateTime() {
        return Utils.addSeconds(startTime, boardingSeconds.or(0));
    }

    /**
     * the arrival time of this {@link RouteSegment}, i.e. when alighting starts
     * <p>
     * Note: not exported to JSON since it can be inferred
     */
    @JsonIgnore
    public Date getArrivalTimeAsZonedDateTime() {
        return Utils.subtractSeconds(endTime, alightingSeconds.or(0));
    }

    /**
     * @return the end time of this {@link RouteSegment}, i.e. when alighting is
     *         finished
     */
    public String getEndTime() {
        return Utils.getAsZonedDateTimeString(endTime);
    }

    @JsonIgnore
    public Date getEndTimeAsZonedDateTime() {
        return endTime;
    }

    /**
     * intermediate stops on the way (mostly useful for public transport routes)
     */
    public List<IntermediateStop> getIntermediateStops() {
        return intermediateStops;
    }

    public Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingBox() {
        return boundingBox;
    }

    /**
     * segment geometry in "Encoded Polyline Algorithm Format"
     * 
     * @see "https://developers.google.com/maps/documentation/utilities/polylinealgorithm"
     */
    public Optional<String> getGeometryEncodedPolyLine() {
        return geometryEncodedPolyLine;
    }

    /** segment geometry as a single LineString-Feature */
    public Optional<GeoJSONFeature<GeoJSONLineString>> getGeometryGeoJson() {
        return geometryGeoJson;
    }

    /**
     * segment geometry as a collection of LineStrings (one for each edge in the
     * routing graph) with debugging information for each edge
     */
    public Optional<GeoJSONFeatureCollection<GeoJSONLineString>> getGeometryGeoJsonEdges() {
        return geometryGeoJsonEdges;
    }

    public List<Instruction<?>> getNavigationInstructions() {
        return navigationInstructions;
    }

    /**
     * @return the ordered list of potential obstacles for mobility impaired
     *         persons (e.g. first up the elevator, then up the stairs,..)
     */
    public List<Constants.Accessibility> getAccessibility() {
        return accessibility;
    }

    /**
     * @return additional information, e.g. other weights for the segment
     *         (energy,..)
     */
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public RouteSegment setNr(int nr) {
        this.nr = nr;
        return this;
    }

    public RouteSegment setFrom(Location<?> from) {
        this.from = from;
        return this;
    }

    public RouteSegment setTo(Location<?> to) {
        this.to = to;
        return this;
    }

    public RouteSegment setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
        return this;
    }

    public RouteSegment setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    public RouteSegment setModeOfTransport(ModeOfTransport modeOfTransport) {
        this.modeOfTransport = modeOfTransport;
        return this;
    }

    public RouteSegment setBoardingSeconds(Integer boardingSeconds) {
        this.boardingSeconds = Optional.fromNullable(boardingSeconds);
        return this;
    }

    public RouteSegment setAlightingSeconds(Integer alightingSeconds) {
        this.alightingSeconds = Optional.fromNullable(alightingSeconds);
        return this;
    }

    @JsonIgnore
    public RouteSegment setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    @JsonProperty
    public RouteSegment setStartTime(String startTime) {
        this.startTime = Utils.parseZonedDateTime(startTime, "startTime");
        return this;
    }

    @JsonIgnore
    public RouteSegment setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    @JsonProperty
    public RouteSegment setEndTime(String endTime) {
        this.endTime = Utils.parseZonedDateTime(endTime, "endTime");
        return this;
    }

    public RouteSegment setIntermediateStops(List<IntermediateStop> intermediateStops) {
        this.intermediateStops = new ArrayList<>(intermediateStops);
        return this;
    }

    public RouteSegment setBoundingBox(GeoJSONFeature<GeoJSONPolygon> boundingBox) {
        this.boundingBox = Optional.fromNullable(boundingBox);
        return this;
    }

    public RouteSegment setGeometryEncodedPolyLine(String geometryEncodedPolyLine) {
        this.geometryEncodedPolyLine = Optional.fromNullable(geometryEncodedPolyLine);
        return this;
    }

    public RouteSegment setGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
        this.geometryGeoJson = Optional.fromNullable(geometryGeoJson);
        return this;
    }

    public RouteSegment setGeometryGeoJsonEdges(GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges) {
        this.geometryGeoJsonEdges = Optional.fromNullable(geometryGeoJsonEdges);
        return this;
    }

    public RouteSegment setNavigationInstructions(List<Instruction<?>> navigationInstructions) {
        this.navigationInstructions = new ArrayList<>(navigationInstructions);
        return this;
    }

    public RouteSegment setAccessibility(List<Accessibility> accessibility) {
        this.accessibility = new ArrayList<>(accessibility);
        return this;
    }

    public RouteSegment setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    /**
     * Shifts the segment in time by adjusting start and end time (if they are
     * set)
     */
    public RouteSegment shiftBySeconds(int amountToAdd) {
        if (startTime != null)
            startTime = Utils.addSeconds(startTime, amountToAdd);
        if (endTime != null)
            endTime = Utils.addSeconds(endTime, amountToAdd);
        return this;
    }

    // --

    // no createMinimal! see javadoc for explanation.

    public static RouteSegment createShallowCopy(RouteSegment s) {
        RouteSegment copy = new RouteSegment().setNr(s.getNr()).setFrom(s.getFrom()).setTo(s.getTo())
                .setDistanceMeters(s.getDistanceMeters()).setDurationSeconds(s.getDurationSeconds())
                .setModeOfTransport(s.getModeOfTransport());
        copy.setBoardingSeconds(s.getBoardingSeconds().orNull());
        copy.setAlightingSeconds(s.getAlightingSeconds().orNull());
        copy.setStartTime(s.getStartTimeAsZonedDateTime()).setEndTime(s.getEndTimeAsZonedDateTime())
                .setIntermediateStops(s.getIntermediateStops());
        copy.setBoundingBox(s.getBoundingBox().orNull());
        copy.setGeometryEncodedPolyLine(s.getGeometryEncodedPolyLine().orNull());
        copy.setGeometryGeoJson(s.getGeometryGeoJson().orNull());
        copy.setGeometryGeoJsonEdges(s.getGeometryGeoJsonEdges().orNull());
        copy.setNavigationInstructions(s.getNavigationInstructions()).setAccessibility(s.getAccessibility())
                .setAdditionalInfo(s.getAdditionalInfo());
        return copy;
    }

    @Override
    public void validate() {
        validate(false);
    }

    /**
     * @param strongValidation
     *            with strong validation even for minor errors an
     *            {@link IllegalArgumentException} is thrown (instead of a
     *            logged warning)
     */
    public void validate(boolean strongValidation) {
        Preconditions.checkArgument(nr != null, "nr is mandatory but missing");
        Preconditions.checkArgument(from != null, "from is mandatory but missing for segment #" + nr);
        from.validate();
        Preconditions.checkArgument(to != null, "to is mandatory but missing for segment #" + nr);
        to.validate();
        Preconditions.checkArgument(distanceMeters != null,
                "distanceMeters is mandatory but missing for segment #" + nr);
        Preconditions.checkArgument(durationSeconds != null,
                "durationSeconds is mandatory but missing for segment #" + nr);
        Preconditions.checkArgument(modeOfTransport != null,
                "modeOfTransport is mandatory but missing for segment #" + nr);
        Preconditions.checkArgument(startTime != null, "startTime is mandatory but missing for segment #" + nr);
        Preconditions.checkArgument(endTime != null, "endTime is mandatory but missing for segment #" + nr);
        for(IntermediateStop s : intermediateStops)
            s.validate();
        if(boundingBox.isPresent())
            boundingBox.get().validate();
        if(geometryGeoJson.isPresent())
            geometryGeoJson.get().validate();
        if(geometryGeoJsonEdges.isPresent())
            geometryGeoJsonEdges.get().validate();
        for(Instruction<?> i : navigationInstructions)
            i.validate();

        try {
            Preconditions.checkArgument(nr > 0, "nr must be > 0, but was %s", nr);
            Preconditions.checkArgument(distanceMeters >= 0, "distanceMeters must be >= 0, but was %s for segment #%s",
                    distanceMeters, nr);
            Preconditions.checkArgument(durationSeconds >= 0,
                    "durationSeconds must be >= 0, but was %s for segment #%s", durationSeconds, nr);

            Preconditions.checkArgument(alightingSeconds.or(0) + boardingSeconds.or(0) <= durationSeconds,
                    "boarding+alighting seconds must be equal to or smaller than the total duration for segment #%s",
                    nr);

            Preconditions.checkArgument(startTime.getTime() <= endTime.getTime(), "startTime must be <= endTime for segment #%s",
                    nr);

            long durationBetweenTimestamps = Utils.getSecondsBetween(startTime, endTime);
            Preconditions.checkArgument(durationSeconds == durationBetweenTimestamps,
                    "durationSeconds does not match seconds between start & end time: %s!=%s for segment #%s",
                    durationSeconds, durationBetweenTimestamps, nr);

            String error = "timestamps of intermediate stops must fall in interval between start & end for segment #"
                    + nr;
            for (IntermediateStop stop : intermediateStops) {
                Preconditions.checkArgument(
                        Utils.isBetween(startTime, stop.getPlannedArrivalTimeAsZonedDateTime(), endTime), error);
                Preconditions.checkArgument(
                        Utils.isBetween(startTime, stop.getPlannedDepartureTimeAsZonedDateTime(), endTime), error);
                Preconditions.checkArgument(
                        Utils.isBetween(startTime, stop.getEstimatedArrivalTimeAsZonedDateTime(), endTime), error);
                Preconditions.checkArgument(
                        Utils.isBetween(startTime, stop.getEstimatedDepartureTimeAsZonedDateTime(), endTime), error);
            }

            boolean geometryPresent = geometryEncodedPolyLine.isPresent() || geometryGeoJson.isPresent()
                    || geometryGeoJsonEdges.isPresent();
            Preconditions.checkArgument(geometryPresent, "at least one geometry must be present for segment #%s", nr);
        } catch (IllegalArgumentException e) {
            if (strongValidation)
                throw e;
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accessibility == null) ? 0 : accessibility.hashCode());
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((alightingSeconds == null) ? 0 : alightingSeconds.hashCode());
        result = prime * result + ((boardingSeconds == null) ? 0 : boardingSeconds.hashCode());
        result = prime * result + ((boundingBox == null) ? 0 : boundingBox.hashCode());
        result = prime * result + ((distanceMeters == null) ? 0 : distanceMeters.hashCode());
        result = prime * result + ((durationSeconds == null) ? 0 : durationSeconds.hashCode());
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((geometryEncodedPolyLine == null) ? 0 : geometryEncodedPolyLine.hashCode());
        result = prime * result + ((geometryGeoJson == null) ? 0 : geometryGeoJson.hashCode());
        result = prime * result + ((geometryGeoJsonEdges == null) ? 0 : geometryGeoJsonEdges.hashCode());
        result = prime * result + ((intermediateStops == null) ? 0 : intermediateStops.hashCode());
        result = prime * result + ((modeOfTransport == null) ? 0 : modeOfTransport.hashCode());
        result = prime * result + ((navigationInstructions == null) ? 0 : navigationInstructions.hashCode());
        result = prime * result + ((nr == null) ? 0 : nr.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
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
        RouteSegment other = (RouteSegment) obj;
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
        if (alightingSeconds == null) {
            if (other.alightingSeconds != null)
                return false;
        } else if (!alightingSeconds.equals(other.alightingSeconds))
            return false;
        if (boardingSeconds == null) {
            if (other.boardingSeconds != null)
                return false;
        } else if (!boardingSeconds.equals(other.boardingSeconds))
            return false;
        if (boundingBox == null) {
            if (other.boundingBox != null)
                return false;
        } else if (!boundingBox.equals(other.boundingBox))
            return false;
        if (distanceMeters == null) {
            if (other.distanceMeters != null)
                return false;
        } else if (!distanceMeters.equals(other.distanceMeters))
            return false;
        if (durationSeconds == null) {
            if (other.durationSeconds != null)
                return false;
        } else if (!durationSeconds.equals(other.durationSeconds))
            return false;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (geometryEncodedPolyLine == null) {
            if (other.geometryEncodedPolyLine != null)
                return false;
        } else if (!geometryEncodedPolyLine.equals(other.geometryEncodedPolyLine))
            return false;
        if (geometryGeoJson == null) {
            if (other.geometryGeoJson != null)
                return false;
        } else if (!geometryGeoJson.equals(other.geometryGeoJson))
            return false;
        if (geometryGeoJsonEdges == null) {
            if (other.geometryGeoJsonEdges != null)
                return false;
        } else if (!geometryGeoJsonEdges.equals(other.geometryGeoJsonEdges))
            return false;
        if (intermediateStops == null) {
            if (other.intermediateStops != null)
                return false;
        } else if (!intermediateStops.equals(other.intermediateStops))
            return false;
        if (modeOfTransport == null) {
            if (other.modeOfTransport != null)
                return false;
        } else if (!modeOfTransport.equals(other.modeOfTransport))
            return false;
        if (navigationInstructions == null) {
            if (other.navigationInstructions != null)
                return false;
        } else if (!navigationInstructions.equals(other.navigationInstructions))
            return false;
        if (nr == null) {
            if (other.nr != null)
                return false;
        } else if (!nr.equals(other.nr))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%d: %s %dm %ds (%s ", nr, modeOfTransport.toString(), distanceMeters,
                durationSeconds, Utils.getShortStringDate(startTime)));
        if (boardingSeconds.isPresent() && boardingSeconds.get() > 0) {
            builder.append(String.format("start: %s, departure: %s, ", Utils.getShortStringTime(startTime),
                    Utils.getShortStringTime(Utils.addSeconds(startTime, boardingSeconds.get()))));
        } else {
            builder.append(String.format("start: %s, ", Utils.getShortStringTime(startTime)));
        }
        if (alightingSeconds.isPresent() && alightingSeconds.get() > 0) {
            builder.append(String.format("alighting: %s, end: %s",
                    Utils.getShortStringTime(Utils.subtractSeconds(endTime, alightingSeconds.get())),
                    Utils.getShortStringTime(endTime)));
        } else {
            builder.append(String.format("end: %s", Utils.getShortStringTime(endTime)));
        }
        builder.append(")");
        return builder.toString();
    }

}
