package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.base.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;
import at.ac.ait.ariadne.routeformat.util.Utils;

/**
 * A {@link Route} represents a way from A to B using one (unimodal route) or
 * several (intermodal route) modes of transport. Its attributes such as the
 * length refer to the whole route, which is further split up into
 * {@link RouteSegment}s for each mode of transport. A route can also consist
 * only of a single {@link Location}, i.e. be a route from A to A, see
 * {@link #createFromLocation(Location, ZonedDateTime)}.
 * <p>
 * In its minimal form nearly all attributes are set. The exceptions are
 * {@link #getId()}, {@link #getOptimizedFor()}, {@link #getAdditionalInfo()}
 * and only one of the geometries is guaranteed to be set. Since this is a lot
 * no <code>createMinimal</code> method is offered as its invocations would be
 * hardly readable. Instead for easy construction
 * {@link #createFromSegments(List)} is recommended.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class Route implements Validatable {
    private final static Logger LOGGER = LoggerFactory.getLogger(Route.class);

    private Location<?> from;
    private Location<?> to;
    private int distanceMeters;
    private int durationSeconds;
    private List<RouteSegment> segments = new ArrayList<>();
    private Optional<String> id = Optional.absent();
    private Date startTime = null;
    private Date endTime = null;
    private Optional<String> optimizedFor = Optional.absent();
    private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.absent();
    private Optional<String> simplifiedGeometryEncodedPolyLine = Optional.absent();
    private Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson = Optional.absent();
    private Map<String, Object> additionalInfo = new TreeMap<>();

    // -- getters

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
    public Date getStartTimeAsZonedDateTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.toString();
    }

    @JsonIgnore
    public Date getEndTimeAsZonedDateTime() {
        return endTime;
    }

    /**
     * @return a description of the optimization criteria for calculating the
     *         route. This field is more specific than the one in
     *         {@link RoutingRequest} (and typically set when a
     *         {@link RoutingResponse} contains several (alternative) routes for
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

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    // -- setters

    public Route setFrom(Location<?> from) {
        this.from = from;
        return this;
    }

    public Route setTo(Location<?> to) {
        this.to = to;
        return this;
    }

    public Route setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
        return this;
    }

    public Route setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    public Route setSegments(List<RouteSegment> segments) {
        this.segments = new ArrayList<>(segments);
        return this;
    }

    public Route setId(String id) {
        this.id = Optional.fromNullable(id);
        return this;
    }

    @JsonIgnore
    public Route setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    @JsonProperty
    public Route setStartTime(String startTime) {
        this.startTime = Utils.parseZonedDateTime(startTime, "startTime");
        return this;
    }

    @JsonIgnore
    public Route setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    @JsonProperty
    public Route setEndTime(String endTime) {
        this.endTime = Utils.parseZonedDateTime(endTime, "endTime");
        return this;
    }

    public Route setOptimizedFor(String optimizedFor) {
        this.optimizedFor = Optional.fromNullable(optimizedFor);
        return this;
    }

    public Route setBoundingBox(GeoJSONFeature<GeoJSONPolygon> boundingBox) {
        this.boundingBox = Optional.fromNullable(boundingBox);
        return this;
    }

    public Route setSimplifiedGeometryEncodedPolyLine(String simplifiedGeometryEncodedPolyLine) {
        this.simplifiedGeometryEncodedPolyLine = Optional.fromNullable(simplifiedGeometryEncodedPolyLine);
        return this;
    }

    public Route setSimplifiedGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> simplifiedGeometryGeoJson) {
        this.simplifiedGeometryGeoJson = Optional.fromNullable(simplifiedGeometryGeoJson);
        return this;
    }

    public Route setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = new TreeMap<>(additionalInfo);
        return this;
    }

    // --

    /**
     * Create a route only consisting of a single location, i.e. start and end
     * point of the route are the same
     */
    public static Route createFromLocation(Location<?> location, Date time) {
        return new Route().setFrom(location).setTo(location).setDistanceMeters(0).setDurationSeconds(0)
                .setStartTime(time).setEndTime(time);
    }

    /**
     * Sets segments and infers from/to location, start/end time, duration,
     * length, and bounding box (from geometryGeoJson)
     */
    public static Route createFromSegments(List<RouteSegment> segments) {
        Route route = new Route().setSegments(segments);
        if (!segments.isEmpty()) {
            RouteSegment first = segments.get(0);
            RouteSegment last = segments.get(segments.size() - 1);
            route.setFrom(first.getFrom());
            route.setTo(last.getTo());
            route.setStartTime(first.getStartTime());
            route.setEndTime(last.getEndTime());
            route.setDistanceMeters(segments.stream().mapToInt(s -> s.getDistanceMeters()).sum());
            route.setDurationSeconds(
                    (int) (Duration.between(route.getStartTimeAsZonedDateTime(), route.getEndTimeAsZonedDateTime())
                            .toMillis() / 1000));
            Utils.getBoundingBoxFromGeometryGeoJson(segments).ifPresent(b -> route.setBoundingBox(b));
        }
        return route;
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
        Preconditions.checkArgument(from != null, "from is mandatory but missing");
        from.validate();
        Preconditions.checkArgument(to != null, "to is mandatory but missing");
        to.validate();
        Preconditions.checkArgument(startTime != null, "startTime is mandatory but missing");
        Preconditions.checkArgument(endTime != null, "endTime is mandatory but missing");
        for(RouteSegment s : segments)
            s.validate();
        if(boundingBox.isPresent())
            boundingBox.get().validate();
        if(simplifiedGeometryGeoJson.isPresent())
            simplifiedGeometryGeoJson.get().validate();

        try {
            Preconditions.checkArgument(distanceMeters >= 0, "distanceMeters must be >= 0, but was %s", distanceMeters);
            Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s",
                    durationSeconds);

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((boundingBox == null) ? 0 : boundingBox.hashCode());
        result = prime * result + distanceMeters;
        result = prime * result + durationSeconds;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((optimizedFor == null) ? 0 : optimizedFor.hashCode());
        result = prime * result + ((segments == null) ? 0 : segments.hashCode());
        result = prime * result
                + ((simplifiedGeometryEncodedPolyLine == null) ? 0 : simplifiedGeometryEncodedPolyLine.hashCode());
        result = prime * result + ((simplifiedGeometryGeoJson == null) ? 0 : simplifiedGeometryGeoJson.hashCode());
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
        Route other = (Route) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null)
                return false;
        } else if (!additionalInfo.equals(other.additionalInfo))
            return false;
        if (boundingBox == null) {
            if (other.boundingBox != null)
                return false;
        } else if (!boundingBox.equals(other.boundingBox))
            return false;
        if (distanceMeters != other.distanceMeters)
            return false;
        if (durationSeconds != other.durationSeconds)
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
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (optimizedFor == null) {
            if (other.optimizedFor != null)
                return false;
        } else if (!optimizedFor.equals(other.optimizedFor))
            return false;
        if (segments == null) {
            if (other.segments != null)
                return false;
        } else if (!segments.equals(other.segments))
            return false;
        if (simplifiedGeometryEncodedPolyLine == null) {
            if (other.simplifiedGeometryEncodedPolyLine != null)
                return false;
        } else if (!simplifiedGeometryEncodedPolyLine.equals(other.simplifiedGeometryEncodedPolyLine))
            return false;
        if (simplifiedGeometryGeoJson == null) {
            if (other.simplifiedGeometryGeoJson != null)
                return false;
        } else if (!simplifiedGeometryGeoJson.equals(other.simplifiedGeometryGeoJson))
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
        return "Route [from=" + from + ", to=" + to + ", distanceMeters=" + distanceMeters + ", durationSeconds="
                + durationSeconds + ", id=" + id + ", startTime=" + startTime + ", endTime=" + endTime
                + ", optimizedFor=" + optimizedFor + ", segments=" + segments.size() + "]";
    }
}
