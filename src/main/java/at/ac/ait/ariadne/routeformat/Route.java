package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	private Optional<String> id = Optional.empty();
	private ZonedDateTime startTime = null;
	private ZonedDateTime endTime = null;
	private Optional<String> optimizedFor = Optional.empty();
	private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
	private Optional<String> simplifiedGeometryEncodedPolyLine = Optional.empty();
	private Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson = Optional.empty();
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
		this.id = Optional.ofNullable(id);
		return this;
	}

	@JsonIgnore
	public Route setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
		return this;
	}

	@JsonProperty
	public Route setStartTime(String startTime) {
		this.startTime = Utils.parseZonedDateTime(startTime, "startTime");
		return this;
	}

	@JsonIgnore
	public Route setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
		return this;
	}

	@JsonProperty
	public Route setEndTime(String endTime) {
		this.endTime = Utils.parseZonedDateTime(endTime, "endTime");
		return this;
	}

	public Route setOptimizedFor(String optimizedFor) {
		this.optimizedFor = Optional.ofNullable(optimizedFor);
		return this;
	}

	public Route setBoundingBox(GeoJSONFeature<GeoJSONPolygon> boundingBox) {
		this.boundingBox = Optional.ofNullable(boundingBox);
		return this;
	}

	public Route setSimplifiedGeometryEncodedPolyLine(String simplifiedGeometryEncodedPolyLine) {
		this.simplifiedGeometryEncodedPolyLine = Optional.ofNullable(simplifiedGeometryEncodedPolyLine);
		return this;
	}

	public Route setSimplifiedGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> simplifiedGeometryGeoJson) {
		this.simplifiedGeometryGeoJson = Optional.ofNullable(simplifiedGeometryGeoJson);
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
	public static Route createFromLocation(Location<?> location, ZonedDateTime time) {
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
		segments.forEach(s -> s.validate());
		boundingBox.ifPresent(b -> b.validate());
		simplifiedGeometryGeoJson.ifPresent(g -> g.validate());

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
	public String toString() {
		return "Route [from=" + from + ", to=" + to + ", distanceMeters=" + distanceMeters + ", durationSeconds="
				+ durationSeconds + ", id=" + id + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", optimizedFor=" + optimizedFor + ", segments=" + segments.size() + "]";
	}
}
