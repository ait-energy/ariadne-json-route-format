package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class RouteSegmentTest {

	@Test
	public void enforceStartEndOrder() {
		try {
			TestUtil.buildTestRouteSegment(TestUtil.END_TIME, TestUtil.START_TIME).build(true);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME).build(true);
	}

	@Test
	public void enforceCorrectDurationTest() {
		try {
			TestUtil.buildTestRouteSegment(TestUtil.START_TIME, "2007-12-03T10:16:31+01:00").build(true);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME).build(true);
	}

	@Test
	public void timeTest() {
		int boarding = 3;
		int alighting = 10;
		RouteSegment segment = TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME)
				.withBoardingSeconds(boarding).withAlightingSeconds(alighting).build(true);
		TestUtil.checkBoardingSeconds(segment, boarding);
		TestUtil.checkAlightingSeconds(segment, alighting);
		Assert.assertEquals(60, segment.getDurationSeconds());
		Assert.assertEquals(47, segment.getTravelTimeSeconds());
		Assert.assertEquals("2007-12-03T10:15:30+01:00", segment.getStartTimeAsZonedDateTime().toString());
		Assert.assertEquals("2007-12-03T10:15:33+01:00", segment.getDepartureTimeAsZonedDateTime().toString());
		Assert.assertEquals("2007-12-03T10:16:20+01:00", segment.getArrivalTimeAsZonedDateTime().toString());
		Assert.assertEquals("2007-12-03T10:16:30+01:00", segment.getEndTimeAsZonedDateTime().toString());
	}

	@Test
	public void enforceCorrectTimestampsForIntermediateStopsTest() {
		IntermediateStop start = IntermediateStop.createMinimal(TestUtil.FROM)
				.setPlannedArrivalTime("2007-12-03T10:15:30+01:00");
		IntermediateStop middle = IntermediateStop.createMinimal(TestUtil.FROM)
				.setPlannedArrivalTime("2007-12-03T10:16:00+01:00");
		IntermediateStop end = IntermediateStop.createMinimal(TestUtil.FROM)
				.setPlannedArrivalTime("2007-12-03T10:16:30+01:00");
		IntermediateStop afterEnd = IntermediateStop.createMinimal(TestUtil.FROM)
				.setPlannedArrivalTime("2007-12-03T10:17:00+01:00");

		try {
			TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, afterEnd).build(true);
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, start).build(true);
		TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, middle).build(true);
		TestUtil.buildTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, end).build(true);
	}

}
