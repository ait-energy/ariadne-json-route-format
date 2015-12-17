package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.RouteSegment.Builder;
import at.ac.ait.sproute.routeformat.Sproute.ModeOfTransport;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONLineString;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPolygon;
import at.ac.ait.sproute.routeformat.instruction.Instruction;
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
 * A RouteSegment containing at least one of the geometry types (encoded polyline or GeoJSON).
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
	private final Optional<ZonedDateTime> departureTime;
	private final Optional<ZonedDateTime> arrivalTime;
	private final Optional<Vehicle> vehicle;
	private final Optional<ServiceProvider> operator;
	private final List<IntermediateStop> intermediateStops;
	private final Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox;
	private final Optional<String> geometryEncodedPolyLine;
	private final Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson;
	private final Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges;
	private final List<Instruction> navigationInstructions;
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

	@JsonProperty(required = true)
	public int getDurationSeconds() {
		return durationSeconds;
	}

	@JsonProperty(required = true)
	public ModeOfTransport getModeOfTransport() {
		return modeOfTransport;
	}

	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
	}

	public Optional<Vehicle> getVehicle() {
		return vehicle;
	}

	public Optional<ServiceProvider> getOperator() {
		return operator;
	}

	/** intermediate stops on the way (mostly useful for public transport routes) */
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
	 * segment geometry as a collection of LineStrings (one for each edge in the routing graph) with debugging
	 * information for each edge
	 */
	public Optional<GeoJSONFeatureCollection<GeoJSONLineString>> getGeometryGeoJsonEdges() {
		return geometryGeoJsonEdges;
	}

	public List<Instruction> getNavigationInstructions() {
		return navigationInstructions;
	}

	/**
	 * @return additional information, e.g. other weights for the segment (energy,..)
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
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.vehicle = builder.vehicle;
		this.operator = builder.operator;
		this.intermediateStops = builder.intermediateStops;
		this.boundingBox = builder.boundingBox;
		this.geometryEncodedPolyLine = builder.geometryEncodedPolyLine;
		this.geometryGeoJson = builder.geometryGeoJson;
		this.geometryGeoJsonEdges = builder.geometryGeoJsonEdges;
		this.navigationInstructions = builder.navigationInstructions;
		this.additionalInfo = builder.additionalInfo;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Integer nr;
		private Location from;
		private Location to;
		private Integer lengthMeters;
		private Integer durationSeconds;
		private ModeOfTransport modeOfTransport;
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private Optional<Vehicle> vehicle = Optional.empty();
		private Optional<ServiceProvider> operator = Optional.empty();
		private List<IntermediateStop> intermediateStops = Collections.emptyList();
		private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
		private Optional<String> geometryEncodedPolyLine = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.empty();
		private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.empty();
		private List<Instruction> navigationInstructions = Collections.emptyList();
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

		public Builder withVehicle(Vehicle vehicle) {
			this.vehicle = Optional.ofNullable(vehicle);
			return this;
		}

		public Builder withOperator(ServiceProvider operator) {
			this.operator = Optional.ofNullable(operator);
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

			boolean geometryPresent = geometryEncodedPolyLine.isPresent() || geometryGeoJson.isPresent()
					|| geometryGeoJsonEdges.isPresent();
			Preconditions.checkArgument(geometryPresent, "at least one geometry must be present");
		}

	}

}
