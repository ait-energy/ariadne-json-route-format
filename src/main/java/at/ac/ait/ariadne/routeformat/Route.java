package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
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
import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Constants.RouteType;
import at.ac.ait.ariadne.routeformat.Route.Builder;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * A {@link Route} represents a way from A to B that using only one (unimodal)
 * or several (intermodal) modes of transport. Its attributes such as the length
 * refer to the whole route, which is further split up into {@link RouteSegment}
 * s for each mode of transport.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class Route {
    private final static Logger LOGGER = LoggerFactory.getLogger(Route.class);

    private final Location from;
    private final Location to;
    private final int distanceMeters;
    private final int durationSeconds;
    private final Optional<String> id;
    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;
    private final Optional<String> optimizedFor;
    private final Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox;
    private final Optional<String> simplifiedGeometryEncodedPolyLine;
    private final Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson;
    private final List<RouteSegment> segments;
    private final Optional<RouteType> type;
    private final Map<String, Object> additionalInfo;

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

    @JsonProperty(required = true)
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @JsonProperty(required = true)
    public List<RouteSegment> getSegments() {
        return segments;
    }

    public Optional<String> getId() {
        return id;
    }

    public String getStartTime() {
        return startTime.toString();
    }

    @JsonIgnore
    public ZonedDateTime getStartTimeAsZonedDateTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.toString();
    }

    @JsonIgnore
    public ZonedDateTime getEndTimeAsZonedDateTime() {
        return endTime;
    }

    /**
     * @return a description of the optimization criteria for calculating the
     *         route. This field is more specific than the one in
     *         {@link RoutingRequest} (and typically set when a
     *         {@link RouteFormatRoot} contains several (alternative) routes for
     *         one request.
     */
    public Optional<String> getOptimizedFor() {
        return optimizedFor;
    }

    /**
     * @return the bounding box with the points of the polygon beginning in
     *         south-west and then continuing clockwise
     */
    public Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingBox() {
        return boundingBox;
    }

    /**
     * geometry in "Encoded Polyline Algorithm Format"
     * 
     * @see "https://developers.google.com/maps/documentation/utilities/polylinealgorithm"
     */
    public Optional<String> getSimplifiedGeometryEncodedPolyLine() {
        return simplifiedGeometryEncodedPolyLine;
    }

    public Optional<GeoJSONFeature<GeoJSONLineString>> getSimplifiedGeometryGeoJson() {
        return simplifiedGeometryGeoJson;
    }

    public Optional<RouteType> getType() {
        return type;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    private Route(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.distanceMeters = builder.distanceMeters;
        this.durationSeconds = builder.durationSeconds;
        this.segments = builder.segments;
        this.id = builder.id;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.optimizedFor = builder.optimizedFor;
        this.boundingBox = builder.boundingBox;
        this.simplifiedGeometryEncodedPolyLine = builder.simplifiedGeometryEncodedPolyLine;
        this.simplifiedGeometryGeoJson = builder.simplifiedGeometryGeoJson;
        this.type = builder.type;
        this.additionalInfo = builder.additionalInfo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Route route) {
        return new Builder(route);
    }

    @Override
    public String toString() {
        return "Route [from=" + from + ", to=" + to + ", distanceMeters=" + distanceMeters + ", durationSeconds="
                + durationSeconds + ", id=" + id + ", startTime=" + startTime + ", endTime=" + endTime
                + ", optimizedFor=" + optimizedFor + ", segments=" + segments.size() + "]";
    }

    public static class Builder {
        private Location from;
        private Location to;
        private int distanceMeters;
        private int durationSeconds;
        private List<RouteSegment> segments = Collections.emptyList();
        private Optional<String> id = Optional.empty();
        private ZonedDateTime startTime = null;
        private ZonedDateTime endTime = null;
        private Optional<String> optimizedFor = Optional.empty();
        private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
        private Optional<String> simplifiedGeometryEncodedPolyLine = Optional.empty();
        private Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson = Optional.empty();
        private Optional<RouteType> type = Optional.empty();
        private Map<String, Object> additionalInfo = Collections.emptyMap();

        public Builder() {
        }

        public Builder(Route route) {
            this.from = route.getFrom();
            this.to = route.getTo();
            this.distanceMeters = route.getDistanceMeters();
            this.durationSeconds = route.getDurationSeconds();
            this.segments = route.getSegments();
            this.id = route.getId();
            this.startTime = route.getStartTimeAsZonedDateTime();
            this.endTime = route.getEndTimeAsZonedDateTime();
            this.optimizedFor = route.getOptimizedFor();
            this.boundingBox = route.getBoundingBox();
            this.simplifiedGeometryEncodedPolyLine = route.getSimplifiedGeometryEncodedPolyLine();
            this.simplifiedGeometryGeoJson = route.getSimplifiedGeometryGeoJson();
            this.type = route.getType();
            this.additionalInfo = route.getAdditionalInfo();
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

        public Builder withSegments(List<RouteSegment> segments) {
            this.segments = ImmutableList.copyOf(segments);
            return this;
        }

        /**
         * sets segments and infers from/to location, start/end time, duration,
         * length, and bounding box (from geometryGeoJson)
         */
        @JsonIgnore
        public Builder withSegmentsAndAutomaticallyInferredFields(List<RouteSegment> segments) {
            this.segments = ImmutableList.copyOf(segments);
            if (!segments.isEmpty()) {
                RouteSegment first = segments.get(0);
                RouteSegment last = segments.get(segments.size() - 1);
                withFrom(first.getFrom());
                withTo(last.getTo());
                withStartTime(first.getStartTime());
                withEndTime(last.getEndTime());
                withDistanceMeters(segments.stream().mapToInt(s -> s.getDistanceMeters()).sum());
                withDurationSeconds((int) (Duration.between(startTime, endTime).toMillis() / 1000));
                Utils.getBoundingBoxFromGeometryGeoJson(segments).ifPresent(b -> withBoundingBox(b));
            }
            return this;
        }

        public Builder withId(String id) {
            this.id = Optional.ofNullable(id);
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

        public Builder withOptimizedFor(String optimizedFor) {
            this.optimizedFor = Optional.ofNullable(optimizedFor);
            return this;
        }

        public Builder withBoundingBox(GeoJSONFeature<GeoJSONPolygon> boundingBox) {
            this.boundingBox = Optional.ofNullable(boundingBox);
            return this;
        }

        public Builder withSimplifiedGeometryEncodedPolyLine(String simplifiedGeometryEncodedPolyLine) {
            this.simplifiedGeometryEncodedPolyLine = Optional.ofNullable(simplifiedGeometryEncodedPolyLine);
            return this;
        }

        public Builder withSimplifiedGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> simplifiedGeometryGeoJson) {
            this.simplifiedGeometryGeoJson = Optional.ofNullable(simplifiedGeometryGeoJson);
            return this;
        }

        public Builder withType(RouteType type) {
            this.type = Optional.ofNullable(type);
            return this;
        }

        public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
            this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
            return this;
        }

        public Route build() {
            validate(false);
            return new Route(this);
        }

        /**
         * @param strongValidation
         *            with strong validation even for minor errors an
         *            {@link IllegalArgumentException} is thrown (instead of a
         *            logged warning)
         */
        public Route build(boolean strongValidation) {
            validate(strongValidation);
            return new Route(this);
        }

        private void validate(boolean strongValidation) {
            Preconditions.checkArgument(from != null, "from is mandatory but missing");
            Preconditions.checkArgument(to != null, "to is mandatory but missing");
            Preconditions.checkArgument(startTime != null, "startTime is mandatory but missing");
            Preconditions.checkArgument(endTime != null, "endTime is mandatory but missing");

            try {
                Preconditions.checkArgument(distanceMeters >= 0, "distanceMeters must be >= 0, but was %s",
                        distanceMeters);
                Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s",
                        durationSeconds);

                Preconditions.checkArgument(!segments.isEmpty(), "segments must not be empty");

                Preconditions.checkArgument(!endTime.isBefore(startTime), "startTime must be <= endTime");

                long durationBetweenTimestamps = Duration.between(startTime, endTime).getSeconds();
                Preconditions.checkArgument(durationSeconds == durationBetweenTimestamps,
                        "durationSeconds does not match seconds between start & end time: %s!=%s", durationSeconds,
                        durationBetweenTimestamps);
                int durationSecondsSum = segments.stream().mapToInt(s -> s.getDurationSeconds()).sum();
                Preconditions.checkArgument(durationSeconds == durationSecondsSum,
                        "durationSeconds does not match the sum of durationSeconds of all route segments: %s!=%s",
                        durationSeconds, durationSecondsSum);
                int distanceMetersSum = segments.stream().mapToInt(s -> s.getDistanceMeters()).sum();
                Preconditions.checkArgument(distanceMeters == distanceMetersSum,
                        "distanceMeters does not match the sum of distanceMeters of all route segments: %s!=%s",
                        distanceMeters, distanceMetersSum);
            } catch (IllegalArgumentException e) {
                if (strongValidation)
                    throw e;
                LOGGER.warn(e.getMessage());
            }
        }
    }
}
