package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Route.Builder;
import at.ac.ait.ariadne.routeformat.Sproute.RouteType;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.location.Location;

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
	private final Location from;
	private final Location to;
	private final int lengthMeters;
	private final int durationSeconds;
	private final Optional<String> id;
	private final ZonedDateTime departureTime;
	private final ZonedDateTime arrivalTime;
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
	public int getLengthMeters() {
		return lengthMeters;
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

	public String getDepartureTime() {
		return departureTime.toString();
	}

	@JsonIgnore
	public ZonedDateTime getDepartureTimeAsZonedDateTime() {
		return departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime.toString();
	}

	@JsonIgnore
	public ZonedDateTime getArrivalTimeAsZonedDateTime() {
		return arrivalTime;
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
		this.lengthMeters = builder.lengthMeters;
		this.durationSeconds = builder.durationSeconds;
		this.segments = builder.segments;
		this.id = builder.id;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
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
		return "Route [from=" + from + ", to=" + to + ", lengthMeters=" + lengthMeters + ", durationSeconds="
				+ durationSeconds + ", id=" + id + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime
				+ ", optimizedFor=" + optimizedFor + ", segments=" + segments.size() + "]";
	}

	public static class Builder {
		private Location from;
		private Location to;
		private int lengthMeters;
		private int durationSeconds;
		private List<RouteSegment> segments = Collections.emptyList();
		private Optional<String> id = Optional.empty();
		private ZonedDateTime departureTime = null;
		private ZonedDateTime arrivalTime = null;
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
			this.lengthMeters = route.getLengthMeters();
			this.durationSeconds = route.getDurationSeconds();
			this.segments = route.getSegments();
			this.id = route.getId();
			this.departureTime = route.getDepartureTimeAsZonedDateTime();
			this.arrivalTime = route.getArrivalTimeAsZonedDateTime();
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

		public Builder withLengthMeters(int lengthMeters) {
			this.lengthMeters = lengthMeters;
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
		 * sets segments and infers from/to location, arrival/departure time,
		 * duration, length
		 */
		@JsonIgnore
		public Builder withSegmentsAndSetAutomaticallyInferredFields(List<RouteSegment> segments) {
			this.segments = ImmutableList.copyOf(segments);
			if (!segments.isEmpty()) {
				RouteSegment first = segments.get(0);
				RouteSegment last = segments.get(segments.size() - 1);
				withFrom(first.getFrom());
				withTo(last.getTo());
				withDepartureTime(first.getDepartureTime());
				withArrivalTime(last.getArrivalTime());
				withLengthMeters(segments.stream().mapToInt(s -> s.getLengthMeters()).sum());
				withDurationSeconds((int) (Duration.between(departureTime, arrivalTime).toMillis() / 1000));
			}
			return this;
		}

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
			return this;
		}

		@JsonIgnore
		public Builder withDepartureTime(ZonedDateTime departureTime) {
			this.departureTime = departureTime;
			return this;
		}

		@JsonProperty
		public Builder withDepartureTime(String departureTime) {
			this.departureTime = SprouteUtils.parseZonedDateTime(departureTime, "departureTime");
			return this;
		}

		@JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = arrivalTime;
			return this;
		}

		@JsonProperty
		public Builder withArrivalTime(String arrivalTime) {
			this.arrivalTime = SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime");
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
			validate();
			return new Route(this);
		}

		private void validate() {
			Preconditions.checkArgument(from != null, "from is mandatory but missing");
			Preconditions.checkArgument(to != null, "to is mandatory but missing");
			Preconditions.checkArgument(departureTime != null, "departureTime is mandatory but missing");
			Preconditions.checkArgument(arrivalTime != null, "arrivalTime is mandatory but missing");

			Preconditions.checkArgument(lengthMeters >= 0, "lengthMeters must be >= 0, but was %s", lengthMeters);
			Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s",
					durationSeconds);

			Preconditions.checkArgument(!segments.isEmpty(), "segments must not be empty");

			Preconditions.checkArgument(!arrivalTime.isBefore(departureTime), "arrivalTime must be <= departureTime");

			long durationBetweenTimestamps = Duration.between(departureTime, arrivalTime).getSeconds();
			Preconditions.checkArgument(durationSeconds == durationBetweenTimestamps,
					"durationSeconds does not match seconds between arrival & departure time: %s!=%s", durationSeconds,
					durationBetweenTimestamps);
		}
	}
}
