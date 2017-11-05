package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.util.Utils;

public class RouteSegmentTest {

    @Test
    public void enforceStartEndOrder() {
        try {
            TestUtil.createTestRouteSegment(TestUtil.END_TIME, TestUtil.START_TIME).validate(true);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME).validate(true);
    }

    @Test
    public void enforceCorrectDurationTest() {
        try {
            TestUtil.createTestRouteSegment(TestUtil.START_TIME, "2007-12-03T10:16:31+01:00").validate(true);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME).validate(true);
    }

    @Test
    public void timeTest() {
        int boarding = 3;
        int alighting = 10;
        RouteSegment segment = TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME)
                .setBoardingSeconds(boarding).setAlightingSeconds(alighting);
        segment.validate(true);
        TestUtil.checkBoardingSeconds(segment, boarding);
        TestUtil.checkAlightingSeconds(segment, alighting);
        Assert.assertEquals(60, segment.getDurationSeconds());
        Assert.assertEquals(47, segment.getTravelTimeSeconds());
        Assert.assertEquals("2007-12-03T10:15:30+01:00", segment.getStartTime());
        Assert.assertEquals("2007-12-03T10:15:33+01:00", Utils.getDateTimeString(segment.getDepartureTimeAsZonedDateTime()));
        Assert.assertEquals("2007-12-03T10:16:20+01:00", Utils.getDateTimeString(segment.getArrivalTimeAsZonedDateTime()));
        Assert.assertEquals("2007-12-03T10:16:30+01:00", segment.getEndTime());
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
            TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, afterEnd).validate(true);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, start).validate(true);
        TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, middle).validate(true);
        TestUtil.createTestRouteSegment(TestUtil.START_TIME, TestUtil.END_TIME, end).validate(true);
    }

}
