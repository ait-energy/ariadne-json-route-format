package at.ac.ait.ariadne.routeformat;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import at.ac.ait.ariadne.routeformat.Route.Builder;

public class RouteTest {

	@Test
	public void enforceDepartureArrivalOrder() {
		LinkedList<RouteSegment> segments = new LinkedList<>();
		segments.add(TestUtil.buildRouteSegment(TestUtil.departureTime, TestUtil.arrivalTime));
		Builder routeBuilder = Route.builder().withSegmentsAndSetAutomaticallyInferredFields(segments);

		routeBuilder.build();

		try {
			routeBuilder.withArrivalTime(TestUtil.departureTime).withDepartureTime(TestUtil.arrivalTime).build();
			Assert.fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

}
