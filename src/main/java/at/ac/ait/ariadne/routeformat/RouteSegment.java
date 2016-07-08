package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.RouteSegment.Builder;
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
 * It is guaranteed that at least one of the geometry types (encoded polyline or
 * GeoJSON) is provided.
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
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RouteSegment {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteSegment.class);

    private final int nr;
    private final Location from;
    private final Location to;
    private final int distanceMeters;
    private final int durationSeconds;
    private final ModeOfTransport modeOfTransport;
    private final Optional<Integer> boardingSeconds;
    private final Optional<Integer> alightingSeconds;
    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;
    private final List<IntermediateStop> intermediateStops;
    private final Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox;
    private final Optional<String> geometryEncodedPolyLine;
    private final Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson;
    private final Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges;
    private final List<Instruction> navigationInstructions;
    private final List<Constants.Accessibility> accessibility;
    private final Map<String, Object> additionalInfo;

    /** number of the segment in the route (starts with 1) */
    @JsonProperty(required = true)
    public int getNr() {
        return nr;
    }

    @JsonProperty(required = true)
    public Location getFrom() {
        return from;
    }

    @JsonProperty(required = true)
    public Location getTo() {
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
        return durationSeconds - boardingSeconds.orElse(0) - alightingSeconds.orElse(0);
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
        return startTime.toString();
    }

    @JsonIgnore
    public ZonedDateTime getStartTimeAsZonedDateTime() {
        return startTime;
    }

    /**
     * the departure time of this {@link RouteSegment}, i.e. after boarding is
     * finished
     * <p>
     * Note: not exported to JSON since it can be inferred
     */
    @JsonIgnore
    public ZonedDateTime getDepartureTimeAsZonedDateTime() {
        return startTime.plusSeconds(boardingSeconds.orElse(0));
    }

    /**
     * the arrival time of this {@link RouteSegment}, i.e. when alighting starts
     * <p>
     * Note: not exported to JSON since it can be inferred
     */
    @JsonIgnore
    public ZonedDateTime getArrivalTimeAsZonedDateTime() {
        return endTime.minusSeconds(alightingSeconds.orElse(0));
    }

    /**
     * @return the end time of this {@link RouteSegment}, i.e. when alighting is
     *         finished
     */
    public String getEndTime() {
        return endTime.toString();
    }

    @JsonIgnore
    public ZonedDateTime getEndTimeAsZonedDateTime() {
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

    public List<Instruction> getNavigationInstructions() {
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

    private RouteSegment(Builder builder) {
        this.nr = builder.nr;
        this.from = builder.from;
        this.to = builder.to;
        this.distanceMeters = builder.distanceMeters;
        this.durationSeconds = builder.durationSeconds;
        this.modeOfTransport = builder.modeOfTransport;
        this.boardingSeconds = builder.boardingSeconds;
        this.alightingSeconds = builder.alightingSeconds;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.intermediateStops = builder.intermediateStops;
        this.boundingBox = builder.boundingBox;
        this.geometryEncodedPolyLine = builder.geometryEncodedPolyLine;
        this.geometryGeoJson = builder.geometryGeoJson;
        this.geometryGeoJsonEdges = builder.geometryGeoJsonEdges;
        this.navigationInstructions = builder.navigationInstructions;
        this.accessibility = builder.accessibility;
        this.additionalInfo = builder.additionalInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(RouteSegment segment) {
        return new Builder(segment);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%d: %s %dm %ds (%s ", nr, modeOfTransport.toString(), distanceMeters,
                durationSeconds, Utils.getShortStringDate(startTime)));
        if (boardingSeconds.isPresent() && boardingSeconds.get() > 0) {
            builder.append(String.format("start: %s, departure: %s, ", Utils.getShortStringTime(startTime),
                    Utils.getShortStringTime(startTime.plus(boardingSeconds.get(), ChronoUnit.SECONDS))));
        } else {
            builder.append(String.format("start: %s, ", Utils.getShortStringTime(startTime)));
        }
        if (alightingSeconds.isPresent() && alightingSeconds.get() > 0) {
            builder.append(String.format("alighting: %s, end: %s",
                    Utils.getShortStringTime(endTime.minus(alightingSeconds.get(), ChronoUnit.SECONDS)),
                    Utils.getShortStringTime(endTime)));
        } else {
            builder.append(String.format("end: %s", Utils.getShortStringTime(endTime)));
        }
        builder.append(")");
        return builder.toString();
    }

    public static class Builder {
        private Integer nr;
        private Location from;
        private Location to;
        private Integer distanceMeters;
        private Integer durationSeconds;
        private ModeOfTransport modeOfTransport;
        private Optional<Integer> boardingSeconds = Optional.empty();
        private Optional<Integer> alightingSeconds = Optional.empty();
        private ZonedDateTime startTime = null;
        private ZonedDateTime endTime = null;
        private List<IntermediateStop> intermediateStops = Collections.emptyList();
        private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
        private Optional<String> geometryEncodedPolyLine = Optional.empty();
        private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.empty();
        private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.empty();
        private List<Instruction> navigationInstructions = Collections.emptyList();
        private List<Constants.Accessibility> accessibility = Collections.emptyList();
        private Map<String, Object> additionalInfo = Collections.emptyMap();

        public Builder() {
        }

        public Builder(RouteSegment segment) {
            this.nr = segment.getNr();
            this.from = segment.getFrom();
            this.to = segment.getTo();
            this.distanceMeters = segment.getDistanceMeters();
            this.durationSeconds = segment.getDurationSeconds();
            this.modeOfTransport = segment.getModeOfTransport();
            this.boardingSeconds = segment.getBoardingSeconds();
            this.alightingSeconds = segment.getAlightingSeconds();
            this.startTime = segment.getStartTimeAsZonedDateTime();
            this.endTime = segment.getEndTimeAsZonedDateTime();
            this.intermediateStops = segment.getIntermediateStops();
            this.boundingBox = segment.getBoundingBox();
            this.geometryEncodedPolyLine = segment.getGeometryEncodedPolyLine();
            this.geometryGeoJson = segment.getGeometryGeoJson();
            this.geometryGeoJsonEdges = segment.geometryGeoJsonEdges;
            this.navigationInstructions = segment.getNavigationInstructions();
            this.accessibility = segment.getAccessibility();
            this.additionalInfo = segment.getAdditionalInfo();
        }

        public Builder withNr(int nr) {
            this.nr = nr;
            return this;
        }

        public Builder withFrom(Location from) {
            this.from = from;
            return this;
        }

        public Builder withTo(Location to) {
            this.to = to;
            return this;
        }

        public Builder withDistanceMeters(int distanceMeters) {
            this.distanceMeters = distanceMeters;
            return this;
        }

        public Builder withDurationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
            return this;
        }

        public Builder withModeOfTransport(ModeOfTransport modeOfTransport) {
            this.modeOfTransport = modeOfTransport;
            return this;
        }

        public Builder withBoardingSeconds(Integer boardingSeconds) {
            this.boardingSeconds = Optional.ofNullable(boardingSeconds);
            return this;
        }

        public Builder withAlightingSeconds(Integer alightingSeconds) {
            this.alightingSeconds = Optional.ofNullable(alightingSeconds);
            return this;
        }

        @JsonIgnore
        public Builder withStartTime(ZonedDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @JsonProperty
        public Builder withStartTime(String startTime) {
            this.startTime = Utils.parseZonedDateTime(startTime, "startTime");
            return this;
        }

        @JsonIgnore
        public Builder withEndTime(ZonedDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        @JsonProperty
        public Builder withEndTime(String endTime) {
            this.endTime = Utils.parseZonedDateTime(endTime, "endTime");
            return this;
        }

        public Builder withIntermediateStops(List<IntermediateStop> intermediateStops) {
            this.intermediateStops = ImmutableList.copyOf(intermediateStops);
            return this;
        }

        public Builder withBoundingBox(GeoJSONFeature<GeoJSONPolygon> boundingBox) {
            this.boundingBox = Optional.ofNullable(boundingBox);
            return this;
        }

        public Builder withGeometryEncodedPolyLine(String geometryEncodedPolyLine) {
            this.geometryEncodedPolyLine = Optional.ofNullable(geometryEncodedPolyLine);
            return this;
        }

        public Builder withGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
            this.geometryGeoJson = Optional.ofNullable(geometryGeoJson);
            return this;
        }

        public Builder withGeometryGeoJsonEdges(GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges) {
            this.geometryGeoJsonEdges = Optional.ofNullable(geometryGeoJsonEdges);
            return this;
        }

        public Builder withNavigationInstructions(List<Instruction> navigationInstructions) {
            this.navigationInstructions = ImmutableList.copyOf(navigationInstructions);
            return this;
        }

        public Builder withAccessibility(List<Constants.Accessibility> accessibility) {
            this.accessibility = ImmutableList.copyOf(accessibility);
            return this;
        }

        public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
            this.additionalInfo = ImmutableSortedMap.copyOf(additionalInfo);
            return this;
        }

        /**
         * Shifts the segment in time by adjusting start and end time (if they
         * are set)
         */
        public Builder shiftInTime(long amountToAdd, ChronoUnit unit) {
            if (startTime != null)
                startTime = startTime.plus(amountToAdd, unit);
            if (endTime != null)
                endTime = endTime.plus(amountToAdd, unit);
            return this;
        }

        public RouteSegment build() {
            validate(false);
            return new RouteSegment(this);
        }

        /**
         * @param strongValidation
         *            with strong validation even for minor errors an
         *            {@link IllegalArgumentException} is thrown (instead of a
         *            logged warning)
         */
        public RouteSegment build(boolean strongValidation) {
            validate(strongValidation);
            return new RouteSegment(this);
        }

        private void validate(boolean strongValidation) {
            Preconditions.checkArgument(nr != null, "nr is mandatory but missing");
            Preconditions.checkArgument(from != null, "from is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(to != null, "to is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(distanceMeters != null,
                    "distanceMeters is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(durationSeconds != null,
                    "durationSeconds is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(modeOfTransport != null,
                    "modeOfTransport is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(startTime != null, "startTime is mandatory but missing for segment #" + nr);
            Preconditions.checkArgument(endTime != null, "endTime is mandatory but missing for segment #" + nr);

            try {
                Preconditions.checkArgument(nr > 0, "nr must be > 0, but was %s", nr);
                Preconditions.checkArgument(distanceMeters >= 0,
                        "distanceMeters must be >= 0, but was %s for segment #%s", distanceMeters, nr);
                Preconditions.checkArgument(durationSeconds >= 0,
                        "durationSeconds must be >= 0, but was %s for segment #%s", durationSeconds, nr);

                Preconditions.checkArgument(alightingSeconds.orElse(0) + boardingSeconds.orElse(0) <= durationSeconds,
                        "boarding+alighting seconds must be equal to or smaller than the total duration for segment #%s",
                        nr);

                Preconditions.checkArgument(!endTime.isBefore(startTime),
                        "startTime must be <= endTime for segment #%s", nr);

                long durationBetweenTimestamps = Duration.between(startTime, endTime).getSeconds();
                Preconditions.checkArgument(durationSeconds == durationBetweenTimestamps,
                        "durationSeconds does not match seconds between start & end time: %s!=%s for segment #%s",
                        durationSeconds, durationBetweenTimestamps, nr);

                String error = "timestamps of intermediate stops must fall in interval between start & end for segment #"
                        + nr;
                for (IntermediateStop stop : intermediateStops) {
                    Preconditions.checkArgument(
                            isBetween(startTime, stop.getPlannedArrivalTimeAsZonedDateTime(), endTime), error);
                    Preconditions.checkArgument(
                            isBetween(startTime, stop.getPlannedDepartureTimeAsZonedDateTime(), endTime), error);
                    Preconditions.checkArgument(
                            isBetween(startTime, stop.getEstimatedArrivalTimeAsZonedDateTime(), endTime), error);
                    Preconditions.checkArgument(
                            isBetween(startTime, stop.getEstimatedDepartureTimeAsZonedDateTime(), endTime), error);
                }

                boolean geometryPresent = geometryEncodedPolyLine.isPresent() || geometryGeoJson.isPresent()
                        || geometryGeoJsonEdges.isPresent();
                Preconditions.checkArgument(geometryPresent, "at least one geometry must be present for segment #%s",
                        nr);
            } catch (IllegalArgumentException e) {
                if (strongValidation)
                    throw e;
                LOGGER.warn(e.getMessage());
            }
        }

        /**
         * @return <code>true</code> if 'between' is really between (or equal)
         *         to start and end
         */
        private boolean isBetween(ZonedDateTime start, Optional<ZonedDateTime> between, ZonedDateTime end) {
            if (between.isPresent()) {
                if (start.isAfter(between.get()) || end.isBefore(between.get()))
                    return false;
            }
            return true;
        }
    }

}
