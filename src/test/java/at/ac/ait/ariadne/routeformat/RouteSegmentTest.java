package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class RouteSegmentTest {

	@Test
	public void enforceDepartureArrivalOrder() {
		try {
			TestUtil.buildTestRouteSegment(TestUtil.ARRIVAL_TIME, TestUtil.DEPARTURE_TIME);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME);
	}

	@Test
	public void enforceCorrectDurationTest() {
		try {
			TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, "2007-12-03T10:16:31+01:00");
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME);
	}

	@Test
	public void enforceCorrectTimestampsForIntermediateStopsTest() {
		IntermediateStop start = IntermediateStop.builder().withStop(TestUtil.FROM)
				.withPlannedArrivalTime("2007-12-03T10:15:30+01:00").build();
		IntermediateStop middle = IntermediateStop.builder().withStop(TestUtil.FROM)
				.withPlannedArrivalTime("2007-12-03T10:16:00+01:00").build();
		IntermediateStop end = IntermediateStop.builder().withStop(TestUtil.FROM)
				.withPlannedArrivalTime("2007-12-03T10:16:30+01:00").build();
		IntermediateStop afterEnd = IntermediateStop.builder().withStop(TestUtil.FROM)
				.withPlannedArrivalTime("2007-12-03T10:17:00+01:00").build();

		try {
			TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME, afterEnd);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME, start);
		TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME, middle);
		TestUtil.buildTestRouteSegment(TestUtil.DEPARTURE_TIME, TestUtil.ARRIVAL_TIME, end);
	}

}
