package at.ac.ait.ariadne.routeformat;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
	private Optional<Integer> boardingSeconds = Optional.empty();
	private Optional<Integer> alightingSeconds = Optional.empty();
	private ZonedDateTime startTime = null;
	private ZonedDateTime endTime = null;
	private List<IntermediateStop> intermediateStops = new ArrayList<>();
	private Optional<GeoJSONFeature<GeoJSONPolygon>> boundingBox = Optional.empty();
	private Optional<String> geometryEncodedPolyLine = Optional.empty();
	private Optional<GeoJSONFeature<GeoJSONLineString>> geometryGeoJson = Optional.empty();
	private Optional<GeoJSONFeatureCollection<GeoJSONLineString>> geometryGeoJsonEdges = Optional.empty();
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
		this.boardingSeconds = Optional.ofNullable(boardingSeconds);
		return this;
	}

	public RouteSegment setAlightingSeconds(Integer alightingSeconds) {
		this.alightingSeconds = Optional.ofNullable(alightingSeconds);
		return this;
	}

	@JsonIgnore
	public RouteSegment setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
		return this;
	}

	@JsonProperty
	public RouteSegment setStartTime(String startTime) {
		this.startTime = Utils.parseZonedDateTime(startTime, "startTime");
		return this;
	}

	@JsonIgnore
	public RouteSegment setEndTime(ZonedDateTime endTime) {
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
		this.boundingBox = Optional.ofNullable(boundingBox);
		return this;
	}

	public RouteSegment setGeometryEncodedPolyLine(String geometryEncodedPolyLine) {
		this.geometryEncodedPolyLine = Optional.ofNullable(geometryEncodedPolyLine);
		return this;
	}

	public RouteSegment setGeometryGeoJson(GeoJSONFeature<GeoJSONLineString> geometryGeoJson) {
		this.geometryGeoJson = Optional.ofNullable(geometryGeoJson);
		return this;
	}

	public RouteSegment setGeometryGeoJsonEdges(GeoJSONFeatureCollection<GeoJSONLineString> geometryGeoJsonEdges) {
		this.geometryGeoJsonEdges = Optional.ofNullable(geometryGeoJsonEdges);
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
	public RouteSegment shiftInTime(long amountToAdd, ChronoUnit unit) {
		if (startTime != null)
			startTime = startTime.plus(amountToAdd, unit);
		if (endTime != null)
			endTime = endTime.plus(amountToAdd, unit);
		return this;
	}

	// --

	// no createMinimal! see javadoc for explanation.

	public static RouteSegment createShallowCopy(RouteSegment s) {
		RouteSegment copy = new RouteSegment().setNr(s.getNr()).setFrom(s.getFrom()).setTo(s.getTo())
				.setDistanceMeters(s.getDistanceMeters()).setDurationSeconds(s.getDurationSeconds())
				.setModeOfTransport(s.getModeOfTransport());
		s.getBoardingSeconds().ifPresent(b -> copy.setBoardingSeconds(b));
		s.getAlightingSeconds().ifPresent(a -> copy.setAlightingSeconds(a));
		copy.setStartTime(s.getStartTimeAsZonedDateTime()).setEndTime(s.getEndTimeAsZonedDateTime())
				.setIntermediateStops(s.getIntermediateStops());
		s.getBoundingBox().ifPresent(b -> copy.setBoundingBox(b));
		s.getGeometryEncodedPolyLine().ifPresent(g -> copy.setGeometryEncodedPolyLine(g));
		s.getGeometryGeoJson().ifPresent(g -> copy.setGeometryGeoJson(g));
		s.getGeometryGeoJsonEdges().ifPresent(g -> copy.setGeometryGeoJsonEdges(g));
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
		intermediateStops.forEach(s -> s.validate());
		boundingBox.ifPresent(b -> b.validate());
		geometryGeoJson.ifPresent(g -> g.validate());
		geometryGeoJsonEdges.ifPresent(g -> g.validate());
		navigationInstructions.forEach(i -> i.validate());

		try {
			Preconditions.checkArgument(nr > 0, "nr must be > 0, but was %s", nr);
			Preconditions.checkArgument(distanceMeters >= 0, "distanceMeters must be >= 0, but was %s for segment #%s",
					distanceMeters, nr);
			Preconditions.checkArgument(durationSeconds >= 0,
					"durationSeconds must be >= 0, but was %s for segment #%s", durationSeconds, nr);

			Preconditions.checkArgument(alightingSeconds.orElse(0) + boardingSeconds.orElse(0) <= durationSeconds,
					"boarding+alighting seconds must be equal to or smaller than the total duration for segment #%s",
					nr);

			Preconditions.checkArgument(!endTime.isBefore(startTime), "startTime must be <= endTime for segment #%s",
					nr);

			long durationBetweenTimestamps = Duration.between(startTime, endTime).getSeconds();
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

}
