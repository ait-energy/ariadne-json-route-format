package at.ac.ait.ariadne.routeformat;

import java.util.Collections;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Sets;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;

public class RoutingRequestTest {

    @Test
    public void testMutexDepartureAndArrivalTime() {
        getBuilder().build();

        try {
            getBuilder().withArrivalTime(TestUtil.ARRIVAL_TIME).build();
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testMaximumTransfers() {
        Assert.assertFalse(getBuilder().build().getMaximumTransfers().isPresent());
        Assert.assertEquals(10, (int) getBuilder().withMaximumTransfers(10).build().getMaximumTransfers().get());
        try {
            getBuilder().withMaximumTransfers(-1).build();
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void motsTest() {
        RoutingRequest request = getBuilder().build();
        Assert.assertEquals("set MOT must be present", 1, request.getModesOfTransport().size());
        Assert.assertEquals("set MOT must be present", GeneralizedModeOfTransportType.BICYCLE,
                request.getModesOfTransport().iterator().next().getModeOfTransport().getGeneralizedType());

        request = getBuilder().withModesOfTransport(Sets.newHashSet(
                RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).build(),
                RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_CAR).build())).build();
        Assert.assertEquals("FOOT must be added for two or more mots", 3, request.getModesOfTransport().size());
        Assert.assertTrue("FOOT must be added for two or more mots",
                request.getModesOfTransport().stream().map(s -> s.getModeOfTransport().getGeneralizedType())
                        .collect(Collectors.toSet()).contains(GeneralizedModeOfTransportType.FOOT));

        try {
            getBuilder().withModesOfTransport(Collections.emptySet()).build();
            Assert.fail("at least one mot must be set");
        } catch (IllegalArgumentException e) {
        }
    }

    public RoutingRequest.Builder getBuilder() {
        return RoutingRequest.builder().withServiceId(TestUtil.SERVICE_ID).withFrom(TestUtil.FROM).withTo(TestUtil.TO)
                .withModesOfTransport(Sets.newHashSet(
                        RequestModeOfTransport.builder().withModeOfTransport(ModeOfTransport.STANDARD_BICYCLE).build()))
                .withDepartureTime(TestUtil.DEPARTURE_TIME);
    }

}
