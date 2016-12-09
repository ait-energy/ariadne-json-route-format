package at.ac.ait.ariadne.routeformat;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.example.IntermodalRouteExample;

public class RoutesTest {

    private static IntermodalRouteExample intermodalRouteExample;
    private static Route intermodalRoute, unimodalFootRoute;

    @BeforeClass
    public static void setup() throws JsonGenerationException, JsonMappingException, IOException {
        intermodalRouteExample = new IntermodalRouteExample();
        intermodalRoute = intermodalRouteExample.getRouteFormatRoot().getRoutes().get(0);
        unimodalFootRoute = Route.createFromSegments(Arrays.asList(intermodalRouteExample.getFootSegment()));
        unimodalFootRoute.validate(true);
    }

    @Test
    public void testIsIntermodal() {
        Assert.assertTrue(Routes.isIntermodal(intermodalRoute));
        Assert.assertFalse(Routes.isIntermodal(unimodalFootRoute));
    }

    @Test
    public void testIsLongerThan() {
        Assert.assertFalse(Routes.isLongerThan(intermodalRoute, GeneralizedModeOfTransportType.FOOT, 1000));
        Assert.assertFalse(Routes.isLongerThan(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE, 692 + 597));
        Assert.assertTrue(Routes.isLongerThan(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE, 692 + 597 - 1));
    }

    @Test
    public void testFeaturesCarSharing() {
        Route carSharingRoute = Route.createFromSegments(Arrays.asList(intermodalRouteExample.getCarSharingSegment()));
        carSharingRoute.validate(true);
        Assert.assertTrue(Routes.featuresGeneralizedMot(intermodalRoute, GeneralizedModeOfTransportType.CAR));
        Assert.assertTrue(Routes.featuresGeneralizedMot(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE));
        Assert.assertTrue(Routes.featuresGeneralizedMot(carSharingRoute, GeneralizedModeOfTransportType.CAR));
        Assert.assertFalse(Routes.featuresGeneralizedMot(carSharingRoute, GeneralizedModeOfTransportType.BICYCLE));
        Assert.assertFalse(Routes.featuresGeneralizedMot(unimodalFootRoute, GeneralizedModeOfTransportType.CAR));
    }

    @Test
    public void testCountModesOfTransport() {
        Assert.assertEquals(7, Routes.countUniqueModesOfTransport(intermodalRoute));
    }

    @Test
    public void testFeaturesOnlyDetailedMots() {
        List<DetailedModeOfTransportType> types = Lists.newArrayList(DetailedModeOfTransportType.FOOT,
                DetailedModeOfTransportType.BICYCLE, DetailedModeOfTransportType.CAR, DetailedModeOfTransportType.BUS,
                DetailedModeOfTransportType.SUBWAY);
        Assert.assertFalse("missing TRANSFER", Routes.featuresOnlyDetailedMots(intermodalRoute, types));
        types.add(DetailedModeOfTransportType.TRANSFER);
        Assert.assertTrue("exactly the same set", Routes.featuresOnlyDetailedMots(intermodalRoute, types));
        types.add(DetailedModeOfTransportType.AIRPLANE);
        Assert.assertTrue("more mots must be OK as well", Routes.featuresOnlyDetailedMots(intermodalRoute, types));
    }

}
