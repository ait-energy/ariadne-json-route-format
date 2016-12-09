package at.ac.ait.ariadne.routeformat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;

public class RoutingRequestTest {

    private RoutingRequest request;

    @Before
    public void createMinimalRequest() {
        List<RequestModeOfTransport<?>> modesOfTransport = ImmutableList
                .of(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_BICYCLE));
        request = RoutingRequest.createMinimal(TestUtil.FROM, TestUtil.TO, modesOfTransport);
    }

    @Test
    public void testDefaultAndMutexDepartureAndArrivalTime() {
        request.validate();
        Assert.assertTrue(request.getDepartureTime().isPresent());
        Assert.assertFalse(request.getArrivalTime().isPresent());

        request.setArrivalTime(RoutingRequest.NOW);
        request.validate();
        Assert.assertFalse(request.getDepartureTime().isPresent());
        Assert.assertTrue(request.getArrivalTime().isPresent());
    }

    @Test
    public void testMaximumTransfers() {
        Assert.assertFalse(request.getMaximumTransfers().isPresent());

        request.setMaximumTransfers(10);
        Assert.assertEquals(10, (int) request.getMaximumTransfers().get());
        request.validate();

        try {
            request.setMaximumTransfers(-1);
            request.validate();
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void motsTest() {
        Assert.assertEquals("set MOT must be present", 1, request.getModesOfTransport().size());
        Assert.assertEquals("set MOT must be present", GeneralizedModeOfTransportType.BICYCLE,
                request.getModesOfTransport().iterator().next().getModeOfTransport().getGeneralizedType());

        request.setModesOfTransport(
                Arrays.asList(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_BICYCLE),
                        RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_CAR)));
        request.validate();
        Assert.assertEquals("FOOT must be added for two or more mots", 3, request.getModesOfTransport().size());
        Assert.assertTrue("FOOT must be added for two or more mots",
                request.getModesOfTransport().stream().map(s -> s.getModeOfTransport().getGeneralizedType())
                        .collect(Collectors.toSet()).contains(GeneralizedModeOfTransportType.FOOT));

        try {
            request.setModesOfTransport(Collections.emptyList());
            request.validate();
            Assert.fail("at least one mot must be set");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void startEndMotsTest() {
        RequestModeOfTransport<?> bicycle = RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_BICYCLE);
        RequestModeOfTransport<?> car = RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_CAR);
        RequestModeOfTransport<?> pt = RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_PUBLIC_TRANSPORT);
        request.setModesOfTransport(Arrays.asList(bicycle, car));
        request.validate();

        try {
            request.setStartModeOfTransport(pt);
            request.validate();
            Assert.fail("unknown start mot pt");
        } catch (IllegalArgumentException e) {
        }

        request.setStartModeOfTransport(bicycle);
        request.validate();

        // same MOT must be accepted
        request.setStartModeOfTransport(RequestModeOfTransport.createMinimal(ModeOfTransport.STANDARD_BICYCLE));
        request.validate();
    }

}
