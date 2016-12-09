package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;
import at.ac.ait.ariadne.routeformat.location.Location;

public class RequestModeOfTransportTest {

    private static Location<?> location = Location.createMinimal(GeoJSONCoordinate.create("16.40073", "48.25625"));

    @Test
    public void testRequestBuilding() {
        Map<String, Object> additionalInfo = ImmutableMap.of("preferParks", true);

        RequestModeOfTransport<?> foot = RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_FOOT)
                .setAdditionalInfo(additionalInfo);
        foot.validate();

        ModeOfTransport bicycleMot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.BICYCLE)
                .setElectric(true).setId("My fast Rotwild bicycle");
        RequestModeOfTransport<?> bicycle = RequestModeOfTransport.createMinimal(bicycleMot)
                .setLocations(Arrays.asList(location)).setAdditionalInfo(additionalInfo);
        bicycle.validate();

        RequestPTModeOfTransport pt = RequestPTModeOfTransport.createMinimal(ModeOfTransport.STANDARD_PUBLIC_TRANSPORT)
                .setExcludedPublicTransportModes(ImmutableSet.of(DetailedModeOfTransportType.CABLE_CAR));
        pt.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMandatoryField() {
        RequestPTModeOfTransport ptWithoutMandatoryField = new RequestPTModeOfTransport()
                .setExcludedPublicTransportModes(ImmutableSet.of(DetailedModeOfTransportType.CABLE_CAR));
        ptWithoutMandatoryField.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPublicTransportMot() {
        RequestPTModeOfTransport nonPt = RequestPTModeOfTransport.createMinimal(ModeOfTransport.STANDARD_CAR);
        nonPt.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPublicTransportExclusionMots() {
        RequestPTModeOfTransport nonPt = RequestPTModeOfTransport
                .createMinimal(ModeOfTransport.STANDARD_PUBLIC_TRANSPORT)
                .setExcludedPublicTransportModes(ImmutableSet.of(DetailedModeOfTransportType.BICYCLE));
        nonPt.validate();
    }

}
