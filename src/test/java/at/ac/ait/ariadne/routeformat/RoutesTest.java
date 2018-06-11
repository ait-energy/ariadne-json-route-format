package at.ac.ait.ariadne.routeformat;

import java.io.IOException;
import java.time.LocalDateTime;
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
        intermodalRoute = intermodalRouteExample.getRoutingResponse().getRoutes().get(0);
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
        Assert.assertTrue(Routes.featuresMot(intermodalRoute, GeneralizedModeOfTransportType.CAR));
        Assert.assertTrue(Routes.featuresMot(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE));
        Assert.assertTrue(Routes.featuresMot(carSharingRoute, GeneralizedModeOfTransportType.CAR));
        Assert.assertFalse(Routes.featuresMot(carSharingRoute, GeneralizedModeOfTransportType.BICYCLE));
        Assert.assertFalse(Routes.featuresMot(unimodalFootRoute, GeneralizedModeOfTransportType.CAR));
    }

    @Test
    public void testCountModesOfTransport() {
        Assert.assertEquals(8, Routes.countUniqueModesOfTransport(intermodalRoute));
    }

    @Test
    public void testGetSegmentsWithMot() {
        List<RouteSegment> segmentsWithMot = Routes
                .getSegmentsWithMot(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE);
        Assert.assertEquals(2, segmentsWithMot.size());
        Assert.assertEquals(
                LocalDateTime.parse("2016-01-01T15:28:10"),
                segmentsWithMot.get(0).getStartTimeAsZonedDateTime().toLocalDateTime());
        Assert.assertEquals(
                LocalDateTime.parse("2016-01-01T15:43:05"),
                segmentsWithMot.get(1).getStartTimeAsZonedDateTime().toLocalDateTime());

        Assert.assertTrue(
                Routes.getSegmentsWithMot(intermodalRoute, GeneralizedModeOfTransportType.MOTORCYCLE).isEmpty());
    }

    @Test
    public void testGetFirstSegmentWithMot() {
        List<RouteSegment> segmentsWithMot = Routes
                .getSegmentsWithMot(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE);

        Assert.assertEquals(
                segmentsWithMot.get(0),
                Routes.getFirstSegmentWithMot(intermodalRoute, GeneralizedModeOfTransportType.BICYCLE).get());
        Assert.assertFalse(
                Routes.getFirstSegmentWithMot(intermodalRoute, GeneralizedModeOfTransportType.MOTORCYCLE).isPresent());
    }

    @Test
    public void testFeaturesExactlyTheseGeneralizedMots() {
        List<GeneralizedModeOfTransportType> types = Lists.newArrayList(
                GeneralizedModeOfTransportType.FOOT,
                GeneralizedModeOfTransportType.BICYCLE,
                GeneralizedModeOfTransportType.CAR);
        Assert.assertFalse("missing pt", Routes.featuresExactlyTheseGeneralizedMots(intermodalRoute, types));
        types.add(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT);
        Assert.assertTrue("exactly the same set", Routes.featuresExactlyTheseGeneralizedMots(intermodalRoute, types));
        types.add(GeneralizedModeOfTransportType.MOTORCYCLE);
        Assert.assertFalse(
                "more mots not OK as well",
                Routes.featuresExactlyTheseGeneralizedMots(intermodalRoute, types));
    }

    @Test
    public void testFeaturesExactlyTheseDetailedMots() {
        List<DetailedModeOfTransportType> types = Lists.newArrayList(
                DetailedModeOfTransportType.FOOT,
                DetailedModeOfTransportType.BICYCLE,
                DetailedModeOfTransportType.CAR,
                DetailedModeOfTransportType.BUS,
                DetailedModeOfTransportType.SUBWAY,
                DetailedModeOfTransportType.HGV);
        Assert.assertFalse("missing TRANSFER", Routes.featuresExactlyTheseDetailedMots(intermodalRoute, types));
        types.add(DetailedModeOfTransportType.TRANSFER);
        Assert.assertTrue("exactly the same set", Routes.featuresExactlyTheseDetailedMots(intermodalRoute, types));
        types.add(DetailedModeOfTransportType.AIRPLANE);
        Assert.assertFalse("more mots not OK as well", Routes.featuresExactlyTheseDetailedMots(intermodalRoute, types));
    }

}
