package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class RouteSegmentTest {

	@Test
	public void enforceDepartureArrivalOrder() {
		try {
			InternalTestUtil.buildRouteSegment(InternalTestUtil.arrivalTime, InternalTestUtil.departureTime);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime);
	}

	@Test
	public void enforceCorrectDurationTest() {
		try {
			InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, "2007-12-03T10:16:31+01:00");
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime);
	}

	@Test
	public void enforceCorrectTimestampsForIntermediateStopsTest() {
		IntermediateStop start = IntermediateStop.builder().withStop(InternalTestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:15:30+01:00").build();
		IntermediateStop middle = IntermediateStop.builder().withStop(InternalTestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:16:00+01:00").build();
		IntermediateStop end = IntermediateStop.builder().withStop(InternalTestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:16:30+01:00").build();
		IntermediateStop afterEnd = IntermediateStop.builder().withStop(InternalTestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:17:00+01:00").build();

		try {
			InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime, afterEnd);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime, start);
		InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime, middle);
		InternalTestUtil.buildRouteSegment(InternalTestUtil.departureTime, InternalTestUtil.arrivalTime, end);
	}

}
