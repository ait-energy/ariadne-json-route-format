package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class RouteSegmentTest {

	@Test
	public void enforceDepartureArrivalOrder() {
		try {
			TestUtil.buildRouteSegment(TestUtil.arrivalTime, TestUtil.departureTime);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime);
	}

	@Test
	public void enforceCorrectDurationTest() {
		try {
			TestUtil.buildRouteSegment(TestUtil.departureTime, "2007-12-03T10:16:31+01:00");
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime);
	}

	@Test
	public void enforceCorrectTimestampsForIntermediateStopsTest() {
		IntermediateStop start = IntermediateStop.builder().withStop(TestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:15:30+01:00").build();
		IntermediateStop middle = IntermediateStop.builder().withStop(TestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:16:00+01:00").build();
		IntermediateStop end = IntermediateStop.builder().withStop(TestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:16:30+01:00").build();
		IntermediateStop afterEnd = IntermediateStop.builder().withStop(TestUtil.from)
				.withPlannedArrivalTime("2007-12-03T10:17:00+01:00").build();

		try {
			TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime, afterEnd);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime, start);
		TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime, middle);
		TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime, end);
	}

}
