package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.ariadne.routeformat.RouteSegment.Builder;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.ariadne.routeformat.instruction.Instruction;
import at.ac.ait.ariadne.routeformat.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A {@link RouteSegment} is a part of a route that is traveled with a single
 * {@link ModeOfTransport}.
 * <p>
 * It is guaranteed that at least one of the geometry types (encoded polyline or
 * GeoJSON) is provided.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
@JsonInclude(Include.NON_EMPTY)
public class RouteSegment {

	private final int nr;
	private final Location from;
	private final Location to;
	private final int lengthMeters;
	private final int durationSeconds;
	private final ModeOfTransport modeOfTransport;
	private final Optional<Integer> boardingSeconds;
	private final Optional<Integer> alightingSeconds;
	private final Optional<ZonedDateTime> departureTime;
	private final Optional<ZonedDateTime> arrivalTime;
	private final List<IntermediateStop> intermediateStops;
	private final Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox;
	private final Optional<String> geometryEncodedPolyLine;
	private final Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson;
	private final Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges;
	private final List<Instruction> navigationInstructions;
	private final List<Sproute.Accessibility> accessibility;
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
	public int getLengthMeters() {
		return lengthMeters;
	}

	/**
	 * @return the total duration for this segment including
	 *         {@link #getBoardingSeconds()} and {@link #getAlightingSeconds()}
	 */
	@JsonProperty(required = true)
	public int getDurationSeconds() {
		return durationSeconds;
	}

	@JsonProperty(required = true)
	public ModeOfTransport getModeOfTransport() {
		return modeOfTransport;
	}

	/**
	 * @return the number of seconds it takes to board (or wait for) the mode of
	 *         transport, e.g. estimated time it takes to walk to your bicycle
	 *         or car and unlock it, average time to hail a taxi, waiting time
	 *         for public transport..
	 */
	public Optional<Integer> getBoardingSeconds() {
		return boardingSeconds;
	}

	/**
	 * @return the number of seconds it takes to alight the mode of transport,
	 *         e.g. estimated time it takes to look for a parking spot for your
	 *         bicycle or car and lock/park it, average time to pay and leave a
	 *         taxi,..
	 */
	public Optional<Integer> getAlightingSeconds() {
		return alightingSeconds;
	}

	/**
	 * @return the start time of this {@link RouteSegment}, i.e. when boarding
	 *         starts
	 */
	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	@JsonIgnore
	public Optional<ZonedDateTime> getDepartureTimeAsZonedDateTime() {
		return departureTime;
	}

	/**
	 * @return the end time of this {@link RouteSegment}, i.e. when alighting is
	 *         finished
	 */
	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	@JsonIgnore
	public Optional<ZonedDateTime> getArrivalTimeAsZonedDateTime() {
		return arrivalTime;
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
	public List<Sproute.Accessibility> getAccessibility() {
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
		this.lengthMeters = builder.lengthMeters;
		this.durationSeconds = builder.durationSeconds;
		this.modeOfTransport = builder.modeOfTransport;
		this.boardingSeconds = builder.boardingSeconds;
		this.alightingSeconds = builder.alightingSeconds;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
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

	@Override
	public String toString() {
		return "RouteSegment [nr=" + nr + ", from=" + from + ", to=" + to + ", lengthMeters=" + lengthMeters
				+ ", durationSeconds=" + durationSeconds + ", modeOfTransport=" + modeOfTransport + ", boardingSeconds="
				+ boardingSeconds + ", alightingSeconds=" + alightingSeconds + ", departureTime=" + departureTime
				+ ", arrivalTime=" + arrivalTime + "]";
	}

	public static class Builder {
		private Integer nr;
		private Location from;
		private Location to;
		private Integer lengthMeters;
		private Integer durationSeconds;
		private ModeOfTransport modeOfTransport;
		private Optional<Integer> boardingSeconds = Optional.empty();
		private Optional<Integer> alightingSeconds = Optional.empty();
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private List<IntermediateStop> intermediateStops = Collections.emptyList();
		private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
		private Optional<String> geometryEncodedPolyLine = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.empty();
		private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.empty();
		private List<Instruction> navigationInstructions = Collections.emptyList();
		private List<Sproute.Accessibility> accessibility = Collections.emptyList();
		private Map<String, Object> additionalInfo = Collections.emptyMap();

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

		public Builder withLengthMeters(int lengthMeters) {
			this.lengthMeters = lengthMeters;
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

		public Builder withAccessibility(List<Sproute.Accessibility> accessibility) {
			this.accessibility = ImmutableList.copyOf(accessibility);
			return this;
		}

		public Builder withAdditionalInfo(Map<String, Object> additionalInfo) {
			this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
			return this;
		}

		public RouteSegment build() {
			validate();
			return new RouteSegment(this);
		}

		private void validate() {
			Preconditions.checkArgument(nr != null, "nr is mandatory but missing");
			Preconditions.checkArgument(from != null, "from is mandatory but missing");
			Preconditions.checkArgument(to != null, "to is mandatory but missing");
			Preconditions.checkArgument(lengthMeters != null, "lengthMeters is mandatory but missing");
			Preconditions.checkArgument(durationSeconds != null, "durationSeconds is mandatory but missing");
			Preconditions.checkArgument(modeOfTransport != null, "modeOfTransport is mandatory but missing");

			Preconditions.checkArgument(nr > 0, "nr must be > 0, but was %s", nr);
			Preconditions.checkArgument(lengthMeters >= 0, "lengthMeters must be >= 0, but was %s", lengthMeters);
			Preconditions.checkArgument(durationSeconds >= 0, "durationSeconds must be >= 0, but was %s",
					durationSeconds);
			Preconditions.checkArgument(alightingSeconds.orElse(0) + boardingSeconds.orElse(0) <= durationSeconds,
					"boarding+alighting seconds must be equal to or smaller than the total duration");

			if (departureTime.isPresent() && arrivalTime.isPresent()) {
				Preconditions.checkArgument(!arrivalTime.get().isBefore(departureTime.get()),
						"departureTime must be <= arrivalTime");

				long durationBetweenTimestamps = Duration.between(departureTime.get(), arrivalTime.get()).getSeconds();
				Preconditions.checkArgument(durationSeconds == durationBetweenTimestamps,
						"durationSeconds does not match seconds between arrival & departure time: %s!=%s",
						durationSeconds, durationBetweenTimestamps);

				String error = "timestamps of intermediate stops must fall in interval between departure & arrival of this segment";
				for (IntermediateStop stop : intermediateStops) {
					Preconditions.checkArgument(isBetween(departureTime.get(),
							stop.getPlannedArrivalTimeAsZonedDateTime(), arrivalTime.get()), error);
					Preconditions.checkArgument(isBetween(departureTime.get(),
							stop.getPlannedDepartureTimeAsZonedDateTime(), arrivalTime.get()), error);
					Preconditions.checkArgument(isBetween(departureTime.get(),
							stop.getEstimatedArrivalTimeAsZonedDateTime(), arrivalTime.get()), error);
					Preconditions.checkArgument(isBetween(departureTime.get(),
							stop.getEstimatedDepartureTimeAsZonedDateTime(), arrivalTime.get()), error);
				}
			}

			boolean geometryPresent = geometryEncodedPolyLine.isPresent() || geometryGeoJson.isPresent()
					|| geometryGeoJsonEdges.isPresent();
			Preconditions.checkArgument(geometryPresent, "at least one geometry must be present");
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
