package at.ac.ait.ariadne.routeformat.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.collect.BoundType;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.RouteSegment.Builder;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;

/**
 * Merges a list of a list of segments, where the latter is basically considered
 * a consecutive Route. Between two routes time gaps are handled as follows: for
 * positive gaps the second route will get the gap added as boarding time in the
 * first non-foot/transfer segment; for negative gaps (=overlaps) the second
 * route will be shifted (and a warning will be logged if the shifted routes
 * contain non-foot segments because this may lead to wrong routes, i.e.
 * shifting public transport routes is probably not useful)
 */
public class RouteSegmentMerger {

	private static final Logger LOGGER = Logger.getLogger(RouteSegmentMerger.class.getName());

	private final List<LinkedList<RouteSegment>> listOfSegmentList;
	private boolean mergeAdjacentFootSegments = true;

	/**
	 * @param listOfSegmentList
	 *            the segments to be merged into one route. lists must not be
	 *            empty.
	 */
	public RouteSegmentMerger(List<List<RouteSegment>> listOfSegmentList) {
		super();
		this.listOfSegmentList = listOfSegmentList.stream().map(l -> new LinkedList<>(l)).collect(Collectors.toList());
	}

	public boolean isMergeAdjacentFootSegments() {
		return mergeAdjacentFootSegments;
	}

	public void setMergeAdjacentFootSegments(boolean mergeAdjacentFootSegments) {
		this.mergeAdjacentFootSegments = mergeAdjacentFootSegments;
	}

	public List<RouteSegment> createMergedSegments() {
		return mergeAllSegments();
	}

	List<RouteSegment> mergeAllSegments() {
		List<RouteSegment> mergedSegments = new ArrayList<>();

		Map<Integer, Integer> index2waitingSeconds = new HashMap<>();
		for (int i = 0; i < listOfSegmentList.size() - 1; i++) {
			int waitingTime = (int) Math.round(Duration
					.between(listOfSegmentList.get(i).getLast().getArrivalTimeAsZonedDateTime(),
							listOfSegmentList.get(i + 1).getFirst().getDepartureTimeAsZonedDateTime())
					.toMillis() / 1000);
			if (waitingTime != 0) {
				index2waitingSeconds.put(i + 1, waitingTime);
			}
		}

		for (int i = 0; i < listOfSegmentList.size(); i++) {
			List<RouteSegment> segmentsToAdd;
			if (index2waitingSeconds.containsKey(i)) {
				int waitingSeconds = index2waitingSeconds.get(i);
				if (waitingSeconds > 0)
					segmentsToAdd = prependWaitingTime(listOfSegmentList.get(i), waitingSeconds);
				else
					segmentsToAdd = shiftInTime(listOfSegmentList.get(i), -waitingSeconds);
			} else {
				segmentsToAdd = listOfSegmentList.get(i);
			}
			mergedSegments.addAll(segmentsToAdd);
		}

		if (mergeAdjacentFootSegments)
			mergedSegments = mergeAdjacentFootSegments(mergedSegments);

		return getNewSegmentsWithFixedNr(mergedSegments);
	}

	/**
	 * Prepends waiting time (boarding time) to the first segment that is not a
	 * FOOT or PT transfer segment
	 */
	private List<RouteSegment> prependWaitingTime(List<RouteSegment> segments, int waitingSeconds) {
		int firstMatchingSegmentIndex = 0;
		while (firstMatchingSegmentIndex < segments.size()) {
			ModeOfTransport mot = segments.get(firstMatchingSegmentIndex).getModeOfTransport();
			if (ModeOfTransport.STANDARD_FOOT.equals(mot) || ModeOfTransport.STANDARD_TRANSFER.equals(mot)) {
				firstMatchingSegmentIndex++;
			} else {
				break;
			}
		}
		if (firstMatchingSegmentIndex >= segments.size())
			firstMatchingSegmentIndex = segments.size() - 1;

		List<RouteSegment> modifiedSegments = new ArrayList<>(segments);
		RouteSegment old = modifiedSegments.get(firstMatchingSegmentIndex);

		// add waiting time to chosen segment
		Builder builder = RouteSegment.builder(old);
		builder.withBoardingSeconds(waitingSeconds);
		builder.withDurationSeconds(old.getDurationSeconds() + waitingSeconds);
		builder.withDepartureTime(old.getDepartureTimeAsZonedDateTime().minus(waitingSeconds, ChronoUnit.SECONDS));
		modifiedSegments.set(firstMatchingSegmentIndex, builder.build());

		// shift arrival/departure times for segments
		// before the modified segment
		for (int i = 0; i < firstMatchingSegmentIndex; i++) {
			old = modifiedSegments.get(i);
			builder = RouteSegment.builder(old);
			builder.withDepartureTime(old.getDepartureTimeAsZonedDateTime().minus(waitingSeconds, ChronoUnit.SECONDS));
			builder.withArrivalTime(old.getArrivalTimeAsZonedDateTime().minus(waitingSeconds, ChronoUnit.SECONDS));
			modifiedSegments.set(i, builder.build());
		}

		return modifiedSegments;
	}

	/**
	 * @param shiftSeconds
	 *            seconds the segments should be shifted in time
	 */
	private List<RouteSegment> shiftInTime(List<RouteSegment> segments, int shiftSeconds) {
		List<RouteSegment> modifiedSegments = new ArrayList<>();
		for (RouteSegment segment : segments) {
			Builder builder = RouteSegment.builder(segment);
			builder.withDepartureTime(segment.getDepartureTimeAsZonedDateTime().plus(shiftSeconds, ChronoUnit.SECONDS));
			builder.withArrivalTime(segment.getArrivalTimeAsZonedDateTime().plus(shiftSeconds, ChronoUnit.SECONDS));
			modifiedSegments.add(builder.build());
			if (!ModeOfTransport.STANDARD_FOOT.equals(segment.getModeOfTransport()))
				LOGGER.warning("shifted segment for " + shiftSeconds + " for mot " + segment.getModeOfTransport());
		}
		return modifiedSegments;
	}

	private List<RouteSegment> mergeAdjacentFootSegments(List<RouteSegment> segments) {
		RangeSet<Integer> rangeSet = TreeRangeSet.create();
		for (int i = 0; i < segments.size(); i++) {
			if (ModeOfTransport.STANDARD_FOOT.equals(segments.get(i).getModeOfTransport())) {
				rangeSet.add(Range.closed(i, i).canonical(DiscreteDomain.integers()));
			}
		}

		Map<Integer, Range<Integer>> start2Range = new HashMap<>();
		for (Range<Integer> range : rangeSet.asRanges())
			start2Range.put(lowerBound(range), range);

		List<RouteSegment> mergedSegments = new ArrayList<>();
		for (int i = 0; i < segments.size(); i++) {
			if (start2Range.containsKey(i)) {
				Range<Integer> range = start2Range.get(i);
				RouteSegment prev = segments.get(i);
				while (i < upperBound(range)) {
					prev = mergeTwoSegments(prev, segments.get(++i));
				}
				mergedSegments.add(prev);
			} else {
				mergedSegments.add(segments.get(i));
			}
		}
		return mergedSegments;
	}

	/**
	 * @return a single merged {@link RouteSegment} with the main attributes
	 *         from the first segment but the combined duration, distance and
	 *         geometry
	 */
	private RouteSegment mergeTwoSegments(RouteSegment a, RouteSegment b) {
		// mot, start & departure time e.g. from a
		Builder builder = RouteSegment.builder(a);

		// adapt time
		int totalSeconds = a.getDurationSeconds() + b.getDurationSeconds();
		builder.withBoardingSeconds(a.getBoardingSeconds().orElse(0) + b.getBoardingSeconds().orElse(0));
		builder.withAlightingSeconds(a.getAlightingSeconds().orElse(0) + b.getAlightingSeconds().orElse(0));
		builder.withDurationSeconds(totalSeconds);
		builder.withArrivalTime(b.getArrivalTime());

		// adapt geometry & length
		builder.withTo(b.getTo());
		GeoJSONFeature<GeoJSONLineString> newlineString = GeoJSONFeature.newLineStringFeature(new ArrayList<>());
		a.getGeometryGeoJson().ifPresent(g -> newlineString.geometry.coordinates.addAll(g.geometry.coordinates));
		b.getGeometryGeoJson().ifPresent(g -> newlineString.geometry.coordinates.addAll(g.geometry.coordinates));
		builder.withGeometryGeoJson(newlineString);
		builder.withLengthMeters(a.getLengthMeters() + b.getLengthMeters()); // TODO
																				// recalculate
																				// length?

		return builder.build();
	}

	/**
	 * fix segment nrs
	 */
	private List<RouteSegment> getNewSegmentsWithFixedNr(List<RouteSegment> segments) {
		List<RouteSegment> fixedSegments = new ArrayList<>();
		for (int i = 0; i < segments.size(); i++) {
			RouteSegment segment = segments.get(i);
			RouteSegment.Builder segmentBuilder = RouteSegment.builder(segment);
			segmentBuilder.withNr(i + 1);
			fixedSegments.add(segmentBuilder.build());
		}
		return fixedSegments;
	}

	static int lowerBound(Range<Integer> range) {
		int lower = range.lowerEndpoint();
		if (range.lowerBoundType().equals(BoundType.OPEN))
			++lower;
		return lower;
	}

	static int upperBound(Range<Integer> range) {
		int upper = range.upperEndpoint();
		if (range.upperBoundType().equals(BoundType.OPEN))
			--upper;
		return upper;
	}

}
