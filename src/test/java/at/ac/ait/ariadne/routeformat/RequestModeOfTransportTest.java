package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.CoordinatePoint;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RequestModeOfTransportTest {

	private static Location<?> location = Location.createMinimum(new CoordinatePoint("16.40073", "48.25625"));

	@SuppressWarnings("unused")
	@Test
	public void testRequestBuilding() {
		Map<String, Object> additionalInfo;

		additionalInfo = ImmutableMap.of("preferParks", true);
		RequestModeOfTransport foot = RequestModeOfTransport.builder()
				.withModeOfTransport(ModeOfTransport.STANDARD_FOOT).withAdditionalInfo(additionalInfo).build();

		additionalInfo = ImmutableMap.of("preferParks", true);
		ModeOfTransport bicycleMot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.BICYCLE)
				.setElectric(true).setId("My fast Rotwild bicycle");
		RequestModeOfTransport bicycle = RequestModeOfTransport.builder().withModeOfTransport(bicycleMot)
				.withLocations(Arrays.asList(location)).withAdditionalInfo(additionalInfo).build();

		RequestPTModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_PUBLIC_TRANSPORT)
				.withExcludedPublicTransportModes(Arrays.asList(DetailedModeOfTransportType.CABLE_CAR)).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMandatoryField() {
		RequestPTModeOfTransport.builder()
				.withExcludedPublicTransportModes(Arrays.asList(DetailedModeOfTransportType.CABLE_CAR)).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonPublicTransportMot() {
		RequestPTModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_CAR).build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonPublicTransportExclusionMots() {
		RequestPTModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_PUBLIC_TRANSPORT)
				.withExcludedPublicTransportModes(Arrays.asList(DetailedModeOfTransportType.FOOT)).build();
	}

}
