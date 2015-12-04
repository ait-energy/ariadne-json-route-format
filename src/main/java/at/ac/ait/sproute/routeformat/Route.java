package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.Route.Builder;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.sproute.routeformat.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
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
	private final Optional<ZonedDateTime> departureTime;
	private final Optional<ZonedDateTime> arrivalTime;
	private final Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox;
	private final Optional<String> simplifiedGeometryEncodedPolyLine;
	private final Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson;
	private final List<RouteSegment> segments;
	private final Map<String, String> additionalInfo;

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

	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	public Optional<GeoJSONFeature<GeoJSONPolygon>> getBoundingBox() {
		return boundingBox;
	}

	/**
	 * geometry in "Encoded Polyline Algorithm Format"
	 * 
	 * @see https://developers.google.com/maps/documentation/utilities/polylinealgorithm
	 */
	public Optional<String> getSimplifiedGeometryEncodedPolyLine() {
		return simplifiedGeometryEncodedPolyLine;
	}

	public Optional<GeoJSONFeature<GeoJSONLineString>> getSimplifiedGeometryGeoJson() {
		return simplifiedGeometryGeoJson;
	}

	public Map<String, String> getAdditionalInfo() {
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
		this.boundingBox = builder.boundingBox;
		this.simplifiedGeometryEncodedPolyLine = builder.simplifiedGeometryEncodedPolyLine;
		this.simplifiedGeometryGeoJson = builder.simplifiedGeometryGeoJson;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Location from;
		private Location to;
		private int lengthMeters;
		private int durationSeconds;
		private List<RouteSegment> segments = Collections.emptyList();
		private Optional<String> id = Optional.empty();
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
		private Optional<String> simplifiedGeometryEncodedPolyLine = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONLineString>> simplifiedGeometryGeoJson = Optional.empty();
		private Map<String, String> additionalInfo = Collections.emptyMap();

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

		public Builder withId(String id) {
			this.id = Optional.ofNullable(id);
			return this;
		}

		@JsonIgnore
		public Builder withDepartureTime(ZonedDateTime departureTime) {
			this.departureTime = Optional.ofNullable(departureTime);
			return this;
		}

		@JsonProperty
		public Builder withDepartureTime(String departureTime) {
			this.departureTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(departureTime, "departureTime"));
			return this;
		}

		@JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = Optional.ofNullable(arrivalTime);
			return this;
		}

		@JsonProperty
		public Builder withArrivalTime(String arrivalTime) {
			this.arrivalTime = Optional.ofNullable(SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime"));
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

		public Builder withAdditionalInfo(Map<String, String> additionalInfo) {
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

			Preconditions.checkArgument(lengthMeters >= 0, "lengthMeters must be >= 0, but was %s", lengthMeters);
			Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s",
					durationSeconds);

			Preconditions.checkArgument(!segments.isEmpty(), "segments must not be empty");
		}
	}

}
