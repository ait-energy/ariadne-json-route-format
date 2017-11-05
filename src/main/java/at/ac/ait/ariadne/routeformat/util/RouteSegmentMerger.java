package at.ac.ait.ariadne.routeformat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BoundType;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.ModeOfTransport;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;

/**
 * Merges a list of a list of consecutive segments (i.e. roughly without jumps
 * such as the end of one segment is 1km away from the start of the next segment
 * ), where the latter is considered a Route.
 * <p>
 * Between two routes time gaps are handled as follows:
 * <ul>
 * <li>for positive gaps the second route will get the gap added as boarding
 * time in the first segment not included in
 * {@link #getWriteWaitingTimePreferableNotInto()} (or as alighting time if the
 * segment is {@link DetailedModeOfTransportType#TRANSFER}).</li>
 * <li>for negative gaps (=overlaps) the second route will be shifted so that
 * the gap is zero (and a warning will be logged if the shifted routes contain
 * non-foot segments because this may lead to wrong routes, i.e. shifting public
 * transport routes is probably not useful)</li>
 * </ul>
 * <p>
 * With {@link #setMergeSegmentsWithSameMot(boolean)} merging of adjacent
 * segments with (exactly!) the same mode of transport, which is activated by
 * default, can be (de)activated. <b>Note</b>, that for merged segments boarding
 * and alighting time is simply summed up and the geometry is simply
 * concatenated without recalculating the distance.
 * <p>
 * With {@link #setAdditionalAlightingSecondsBetweenRoutes(List)} additional
 * alighting seconds can be added.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class RouteSegmentMerger {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteSegmentMerger.class);

    private final List<LinkedList<RouteSegment>> routes;
    private List<Integer> additionalAlightingSecondsBetweenRoutes;
    private boolean mergeSegmentsWithSameMot = true;
    private Set<ModeOfTransport> writeWaitingTimePreferableNotInto;

    /**
     * @param routes
     *            the routes to be merged into one route. lists must not be
     *            empty.
     */
    public RouteSegmentMerger(List<List<RouteSegment>> routes) {
        this.routes = new ArrayList<>();
        for(List<RouteSegment> segments : routes)
            this.routes.add(new LinkedList<>(segments));
        this.additionalAlightingSecondsBetweenRoutes = new ArrayList<>();
        for (int i = 0; i < routes.size() - 1; i++)
            additionalAlightingSecondsBetweenRoutes.add(0);
        this.writeWaitingTimePreferableNotInto = new HashSet<>();
        this.writeWaitingTimePreferableNotInto.add(ModeOfTransport.STANDARD_FOOT);
    }

    public boolean isMergeSegmentsWithSameMot() {
        return mergeSegmentsWithSameMot;
    }

    public void setMergeSegmentsWithSameMot(boolean mergeSegmentsWithSameMot) {
        this.mergeSegmentsWithSameMot = mergeSegmentsWithSameMot;
    }

    /**
     * @return a mutable copy of the internal set
     */
    public Set<ModeOfTransport> getWriteWaitingTimePreferableNotInto() {
        return new HashSet<>(writeWaitingTimePreferableNotInto);
    }

    public void setWriteWaitingTimePreferableNotInto(Set<ModeOfTransport> writeWaitingTimePreferableNotInto) {
        this.writeWaitingTimePreferableNotInto = writeWaitingTimePreferableNotInto;
    }

    /**
     * @return a mutable copy of the internal list
     */
    public List<Integer> getAdditionalAlightingSecondsBetweenRoutes() {
        return new ArrayList<>(additionalAlightingSecondsBetweenRoutes);
    }

    public void setAdditionalAlightingSecondsBetweenRoutes(List<Integer> additionalAlightingSecondsBetweenRoutes) {
        if (additionalAlightingSecondsBetweenRoutes.size() != routes.size() - 1)
            throw new IllegalArgumentException(
                    "alighting seconds must be given for exactly each change between routes");
        this.additionalAlightingSecondsBetweenRoutes = additionalAlightingSecondsBetweenRoutes;
    }

    public List<RouteSegment> createMergedSegments() {
        return mergeAllSegments();
    }

    List<RouteSegment> mergeAllSegments() {
        List<RouteSegment> mergedSegments = new ArrayList<>();

        for (int i = 0; i < routes.size() - 1; i++) {
            int alightingSeconds = additionalAlightingSecondsBetweenRoutes.get(i);
            if (alightingSeconds > 0) {
                RouteSegment segmentToProlong = routes.get(i).removeLast();
                RouteSegment prolongedSegment = RouteSegment.createShallowCopy(segmentToProlong)
                        .setAlightingSeconds(segmentToProlong.getAlightingSeconds().or(0) + alightingSeconds)
                        .setDurationSeconds(segmentToProlong.getDurationSeconds() + alightingSeconds)
                        .setEndTime(Utils.addSeconds(segmentToProlong.getEndTimeAsZonedDateTime(), alightingSeconds));
                routes.get(i).addLast(prolongedSegment);
            }
        }

        Map<Integer, Integer> index2waitingSeconds = new HashMap<>();
        index2waitingSeconds.put(0, 0);
        Date endOfLastRoute = routes.get(0).getLast().getEndTimeAsZonedDateTime();
        for (int i = 1; i < routes.size(); i++) {
            int waitingSeconds = Utils.getSecondsBetween(endOfLastRoute, routes.get(i).getFirst().getStartTimeAsZonedDateTime());
            index2waitingSeconds.put(i, waitingSeconds);
            int routeSeconds = 0;
            for(RouteSegment s : routes.get(i))
                routeSeconds += s.getDurationSeconds();
            endOfLastRoute = Utils.addSeconds(endOfLastRoute, routeSeconds + (waitingSeconds > 0 ? waitingSeconds : 0));
        }

        for (int i = 0; i < routes.size(); i++) {
            List<RouteSegment> segmentsToAdd;

            int waitingSeconds = index2waitingSeconds.get(i);

            if (waitingSeconds > 0)
                segmentsToAdd = prependWaitingTime(routes.get(i), waitingSeconds);
            else if (waitingSeconds < 0)
                segmentsToAdd = shiftInTime(routes.get(i), -waitingSeconds);
            else
                segmentsToAdd = routes.get(i);

            mergedSegments.addAll(segmentsToAdd);
        }

        if (mergeSegmentsWithSameMot)
            mergedSegments = mergeSegmentsWithSameMot(mergedSegments);

        fixConsecutiveSegmentNrs(mergedSegments);

        return mergedSegments;
    }

    /**
     * Prepends waiting time (boarding time) to the first segment that is not in
     * the black list (and use the last segment if all are on the black list)
     */
    private List<RouteSegment> prependWaitingTime(List<RouteSegment> segments, int waitingSeconds) {
        int firstMatchingSegmentIndex = 0;
        while (firstMatchingSegmentIndex < segments.size()) {
            ModeOfTransport mot = segments.get(firstMatchingSegmentIndex).getModeOfTransport();
            if (writeWaitingTimePreferableNotInto.contains(mot)) {
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
        RouteSegment modifiedCopy = RouteSegment.createShallowCopy(old);
        if (old.getModeOfTransport().getDetailedType().isPresent()
                && old.getModeOfTransport().getDetailedType().get().equals(DetailedModeOfTransportType.TRANSFER)) {
            modifiedCopy.setAlightingSeconds(old.getAlightingSeconds().or(0) + waitingSeconds);
        } else {
            modifiedCopy.setBoardingSeconds(old.getBoardingSeconds().or(0) + waitingSeconds);
        }
        modifiedCopy.setDurationSeconds(old.getDurationSeconds() + waitingSeconds);
        modifiedCopy.setStartTime(Utils.subtractSeconds(old.getStartTimeAsZonedDateTime(), waitingSeconds));
        modifiedSegments.set(firstMatchingSegmentIndex, modifiedCopy);

        // shift start/end times for segments
        // before the modified segment
        for (int i = 0; i < firstMatchingSegmentIndex; i++) {
            old = modifiedSegments.get(i);
            modifiedCopy = RouteSegment.createShallowCopy(old);
            modifiedCopy.setStartTime(Utils.subtractSeconds(old.getStartTimeAsZonedDateTime(), waitingSeconds));
            modifiedCopy.setEndTime(Utils.subtractSeconds(old.getEndTimeAsZonedDateTime(), waitingSeconds));
            modifiedSegments.set(i, modifiedCopy);
        }

        return modifiedSegments;
    }

    /**
     * @param shiftSeconds
     *            seconds the segments should be shifted in time
     */
    private static List<RouteSegment> shiftInTime(List<RouteSegment> segments, int shiftSeconds) {
        List<RouteSegment> modifiedSegments = new ArrayList<>();
        for (RouteSegment segment : segments) {
            RouteSegment modifiedCopy = RouteSegment.createShallowCopy(segment);
            modifiedCopy.setStartTime(Utils.addSeconds(segment.getStartTimeAsZonedDateTime(), shiftSeconds));
            modifiedCopy.setEndTime(Utils.addSeconds(segment.getEndTimeAsZonedDateTime(), shiftSeconds));
            modifiedSegments.add(modifiedCopy);
            if (!segment.getModeOfTransport().equals(ModeOfTransport.STANDARD_FOOT))
                LOGGER.warn(shiftSeconds + "s shift for mot " + segment.getModeOfTransport());
        }
        return modifiedSegments;
    }

    private static List<RouteSegment> mergeSegmentsWithSameMot(List<RouteSegment> segments) {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        for (int i = 0; i < segments.size() - 1; i++) {
            if (segments.get(i).getModeOfTransport().equals(segments.get(i + 1).getModeOfTransport())) {
                rangeSet.add(Range.closed(i, i + 1).canonical(DiscreteDomain.integers()));
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
    private static RouteSegment mergeTwoSegments(RouteSegment a, RouteSegment b) {
        // mot, start & end time e.g. from a
        RouteSegment merged = RouteSegment.createShallowCopy(a);

        // adapt time
        int totalSeconds = a.getDurationSeconds() + b.getDurationSeconds();
        merged.setBoardingSeconds(a.getBoardingSeconds().or(0) + b.getBoardingSeconds().or(0));
        merged.setAlightingSeconds(a.getAlightingSeconds().or(0) + b.getAlightingSeconds().or(0));
        merged.setDurationSeconds(totalSeconds);
        merged.setEndTime(Utils.addSeconds(a.getStartTimeAsZonedDateTime(), totalSeconds));

        // adapt geometry & length
        merged.setTo(b.getTo());
        GeoJSONFeature<GeoJSONLineString> newlineString = GeoJSONFeature.createLineStringFeature(new ArrayList<GeoJSONCoordinate>());
        List<GeoJSONFeature<GeoJSONLineString>> newGeometryGeoJsonEdges = new ArrayList<>();
        for (RouteSegment routeSegment : new RouteSegment[] { a, b }) {
            if(routeSegment.getGeometryGeoJson().isPresent()) {
                newlineString.getGeometry().getCoordinates().addAll(routeSegment.getGeometryGeoJson().get().getGeometry().getCoordinates());
            }
            if(routeSegment.getGeometryGeoJsonEdges().isPresent()) {
                newGeometryGeoJsonEdges.addAll(routeSegment.getGeometryGeoJsonEdges().get().getFeatures());
            }
        }
        merged.setGeometryGeoJson(newlineString);
        if (!newGeometryGeoJsonEdges.isEmpty()) {
            merged.setGeometryGeoJsonEdges(GeoJSONFeatureCollection.create(newGeometryGeoJsonEdges));
        }
        merged.setDistanceMeters(a.getDistanceMeters() + b.getDistanceMeters());

        return merged;
    }

    private static void fixConsecutiveSegmentNrs(List<RouteSegment> segments) {
        for (int i = 0; i < segments.size(); i++) {
            segments.get(i).setNr(i + 1);
        }
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
