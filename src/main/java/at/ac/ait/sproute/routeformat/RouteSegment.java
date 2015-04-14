package at.ac.ait.sproute.routeformat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.RouteSegment.Builder;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONLineString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * A RouteSegment containing at least one of the geometry types (encoded
 * polyline or GeoJSON).
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
public class RouteSegment {
	
	// TODO How to store attributes??

	public enum ModeOfTransport {
		FOOT, BICYCLE, MOTORCYCLE, CAR, PUBLIC_TRANSPORT;
	};

	private int nr;
	private Location from;
	private Location to;
	private List<Location> intermediateStops; 
	private Optional<ZonedDateTime> departureTime;
	private Optional<ZonedDateTime> arrivalTime;
	private int lengthMeters;
	private int durationSeconds;
	private ModeOfTransport modeOfTransport;
	private Optional<Vehicle> vehicle;
	private Optional<Operator> operator;
	private Optional<String> geometryEncodedPolyLine;
	private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson;
	private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges;
	private List<NavigationInstruction> navigationInstructions = new ArrayList<>();

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

	/** intermediate stops on the way (mostly useful for public transport routes) */
	public List<Location> getIntermediateStops() {
		return intermediateStops;
	}

	public Optional<String> getDepartureTime() {
		return departureTime.map(time -> time.toString());
	}

	public Optional<String> getArrivalTime() {
		return arrivalTime.map(time -> time.toString());
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

	public Optional<Vehicle> getVehicle() {
		return vehicle;
	}

	public Optional<Operator> getOperator() {
		return operator;
	}

	/**
	 * segment geometry in "Encoded Polyline Algorithm Format":
	 * https://developers
	 * .google.com/maps/documentation/utilities/polylinealgorithm
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

	public List<NavigationInstruction> getNavigationInstructions() {
		return navigationInstructions;
	}

	private RouteSegment(Builder builder) {
		this.nr = builder.nr.get();
		this.from = builder.from.get();
		this.to = builder.to.get();
		this.intermediateStops = builder.intermediateStops;
		this.departureTime = builder.departureTime;
		this.arrivalTime = builder.arrivalTime;
		this.lengthMeters = builder.lengthMeters.get();
		this.durationSeconds = builder.durationSeconds.get();
		this.modeOfTransport = builder.modeOfTransport.get();
		this.vehicle = builder.vehicle;
		this.operator = builder.operator;
		this.geometryEncodedPolyLine = builder.geometryEncodedPolyLine;
		this.geometryGeoJson = builder.geometryGeoJson;
		this.geometryGeoJsonEdges = builder.geometryGeoJsonEdges;
		this.navigationInstructions = builder.navigationInstructions;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Optional<Integer> nr = Optional.empty();
		private Optional<Location> from = Optional.empty();
		private Optional<Location> to = Optional.empty();
		private List<Location> intermediateStops = new ArrayList<>();
		private Optional<ZonedDateTime> departureTime = Optional.empty();
		private Optional<ZonedDateTime> arrivalTime = Optional.empty();
		private Optional<Integer> lengthMeters = Optional.empty();
		private Optional<Integer> durationSeconds = Optional.empty();
		private Optional<ModeOfTransport> modeOfTransport = Optional.empty();
		private Optional<Vehicle> vehicle = Optional.empty();
		private Optional<Operator> operator = Optional.empty();
		private Optional<String> geometryEncodedPolyLine = Optional.empty();
		private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.empty();
		private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.empty();
		private List<NavigationInstruction> navigationInstructions = new ArrayList<>();

		public Builder withNr(int nr) {
			this.nr = Optional.of(nr);
			return this;
		}

		public Builder withFrom(Location from) {
			this.from = Optional.of(from);
			return this;
		}

		public Builder withTo(Location to) {
			this.to = Optional.of(to);
			return this;
		}

		public Builder withIntermediateStops(List<Location> intermediateStops) {
			this.intermediateStops = new ArrayList<>(intermediateStops);
			return this;
		}

		@JsonIgnore
		public Builder withDepartureTime(ZonedDateTime departureTime) {
			this.departureTime = Optional.of(departureTime);
			return this;
		}
		
		@JsonProperty
        public Builder withDepartureTime(String departureTime) {
        	this.departureTime = Optional.of(SprouteUtils.parseZonedDateTime(departureTime, "departureTime"));
            return this;
        }

        @JsonIgnore
		public Builder withArrivalTime(ZonedDateTime arrivalTime) {
			this.arrivalTime = Optional.of(arrivalTime);
			return this;
		}
		
        @JsonProperty
        public Builder withArrivalTime(String arrivalTime) {
        	this.arrivalTime = Optional.of(SprouteUtils.parseZonedDateTime(arrivalTime, "arrivalTime"));
            return this;
        }

		public Builder withLengthMeters(int lengthMeters) {
			this.lengthMeters = Optional.of(lengthMeters);
			return this;
		}

		public Builder withDurationSeconds(int durationSeconds) {
			this.durationSeconds = Optional.of(durationSeconds);
			return this;
		}

		public Builder withModeOfTransport(ModeOfTransport modeOfTransport) {
			this.modeOfTransport = Optional.of(modeOfTransport);
			return this;
		}

		public Builder withVehicle(Vehicle vehicle) {
			this.vehicle = Optional.of(vehicle);
			return this;
		}

		public Builder withOperator(Operator operator) {
			this.operator = Optional.of(operator);
			return this;
		}

		public Builder withGeometryEncodedPolyLine(String geometryEncodedPolyLine) {
			this.geometryEncodedPolyLine = Optional.of(geometryEncodedPolyLine);
			return this;
		}

		public Builder withGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
			this.geometryGeoJson = Optional.of(geometryGeoJson);
			return this;
		}

		public Builder withGeometryGeoJsonEdges(
				GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges) {
			this.geometryGeoJsonEdges = Optional.of(geometryGeoJsonEdges);
			return this;
		}

		public Builder withNavigationInstructions(List<NavigationInstruction> navigationInstructions) {
			this.navigationInstructions = new ArrayList<>(navigationInstructions);
			return this;
		}

		public RouteSegment build() {
			validate();
			return new RouteSegment(this);
		}

		private void validate() {
			Preconditions.checkArgument(nr.isPresent(), "nr is mandatory but missing");
			Preconditions.checkArgument(from.isPresent(), "from is mandatory but missing");
			Preconditions.checkArgument(to.isPresent(), "to is mandatory but missing");
			Preconditions.checkArgument(lengthMeters.isPresent(), "lengthMeters is mandatory but missing");
			Preconditions.checkArgument(durationSeconds.isPresent(), "durationSeconds is mandatory but missing");
			Preconditions.checkArgument(modeOfTransport.isPresent(), "modeOfTransport is mandatory but missing");
			
			Preconditions.checkArgument(nr.get() > 0, "nr must be > 0, but was %s", nr.get());
			Preconditions.checkArgument(lengthMeters.get() >= 0, "lengthMeters must be >= 0, but was %s", lengthMeters.get());
			Preconditions.checkArgument(durationSeconds.get() >= 0, "durationSeconds must be >= 0, but was %s", durationSeconds.get());
			
			boolean geometryPresent = geometryEncodedPolyLine.isPresent() || geometryGeoJson.isPresent() || geometryGeoJsonEdges.isPresent();
			Preconditions.checkArgument(geometryPresent, "at least one geometry must be present");
		}

	}

}
