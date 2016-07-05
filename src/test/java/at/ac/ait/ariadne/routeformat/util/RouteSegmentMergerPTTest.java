package at.ac.ait.ariadne.routeformat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.TestUtil;
import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;

public class RouteSegmentMergerPTTest {

	private static List<List<RouteSegment>> segmentsToMerge;

	@BeforeClass
	public static void readRoutes() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new Jdk8Module());
		InputStream inStream = RouteSegmentMergerPTTest.class.getResourceAsStream("/test-merge.json");
		segmentsToMerge = mapper.readValue(inStream, DataFormat.class).segmentsToMerge;
	}
	
	// TODO write another test that checks if more complex waiting times are merged correctly?

	@Test
	public void mergeTest() {
		RouteSegmentMerger merger = new RouteSegmentMerger(segmentsToMerge);
		merger.setMergeSegmentsWithSameMot(true);
		List<RouteSegment> mergedSegments = merger.createMergedSegments();

		/* @formatter:off */
//		1: FOOT-FOOT 347m 289s (2016-04-12 departure: 16:26:36, arrival: 16:31:25)
//		2: PUBLIC_TRANSPORT-TRAM 6 -> Wien Burggasse-Stadthalle 435m 275s (2016-04-12 boarding: 16:31:25, departure: 16:34:00, arrival: 16:36:00)
//		3: FOOT-TRANSFER 31m 60s (2016-04-12 departure: 16:36:00, arrival: 16:37:00)
//		4: FOOT-FOOT 310m 414s (2016-04-12 departure: 16:37:00, arrival: 16:43:54)
//		5: FOOT-TRANSFER 48m 186s (2016-04-12 departure: 16:43:54, alighting: 16:45:54, arrival: 16:47:00)
//		6: PUBLIC_TRANSPORT-SUBWAY U3 -> Wien Ottakring 5602m 600s (2016-04-12 departure: 16:47:00, arrival: 16:57:00)
//		7: FOOT-TRANSFER 130m 240s (2016-04-12 departure: 16:57:00, arrival: 17:01:00)
//		8: PUBLIC_TRANSPORT-RAILWAY R 2251 -> Wiener Neustadt Hbf. 3442m 360s (2016-04-12 departure: 17:01:00, arrival: 17:07:00)
//		9: FOOT-TRANSFER 60m 60s (2016-04-12 departure: 17:07:00, arrival: 17:08:00)
//		10: FOOT-FOOT 144m 178s (2016-04-12 departure: 17:08:00, arrival: 17:10:58)
		
		TestUtil.checkMot(mergedSegments.get(1), GeneralizedModeOfTransportType.PUBLIC_TRANSPORT, DetailedModeOfTransportType.TRAM);
		TestUtil.checkDeparture(mergedSegments.get(1), "2016-04-12T16:31:25");
		TestUtil.checkBoardingSeconds(mergedSegments.get(1), 155); // waiting time!
		TestUtil.checkAlightingSeconds(mergedSegments.get(1), 0);
		TestUtil.checkArrival(mergedSegments.get(1), "2016-04-12T16:36:00");

		TestUtil.checkAlightingSeconds(mergedSegments.get(4), 66); // waiting time!

		TestUtil.checkMot(mergedSegments.get(5), GeneralizedModeOfTransportType.PUBLIC_TRANSPORT, DetailedModeOfTransportType.SUBWAY);
		TestUtil.checkDeparture(mergedSegments.get(5), "2016-04-12T16:47:00");
		TestUtil.checkArrival(mergedSegments.get(5), "2016-04-12T16:57:00");

		TestUtil.checkMot(mergedSegments.get(7), GeneralizedModeOfTransportType.PUBLIC_TRANSPORT,
				DetailedModeOfTransportType.RAILWAY);
		TestUtil.checkDeparture(mergedSegments.get(7), "2016-04-12T17:01:00");
		TestUtil.checkArrival(mergedSegments.get(7), "2016-04-12T17:07:00");
		/* @formatter:on */
	}

	public static class DataFormat {
		public List<List<RouteSegment>> segmentsToMerge;
	}

}
