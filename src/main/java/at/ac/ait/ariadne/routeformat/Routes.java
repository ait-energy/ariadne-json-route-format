package at.ac.ait.ariadne.routeformat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;

/**
 * Helper methods to find out more about properties of a {@link Route}
 */
public class Routes {

    public static boolean isUnimodal(Route route) {
        return !isIntermodal(route);
    }

    public static boolean isIntermodal(Route route) {
        Set<GeneralizedModeOfTransportType> usedModes = route.getSegments().stream()
                .map(s -> s.getModeOfTransport().getGeneralizedType()).collect(Collectors.toSet());
        return usedModes.size() > 1;
    }

    public static boolean isLongerThan(Route route, int meters) {
        return route.getDistanceMeters() > meters;
    }

    public static boolean isLongerThan(Route route, GeneralizedModeOfTransportType mot, int meters) {
        int totalMeters = route.getSegments().stream()
                .filter(s -> s.getModeOfTransport().getGeneralizedType().equals(mot))
                .mapToInt(s -> s.getDistanceMeters()).sum();
        return totalMeters > meters;
    }

    public static boolean featuresGeneralizedMot(Route route, GeneralizedModeOfTransportType type) {
        return route.getSegments().stream().filter(s -> s.getModeOfTransport().getGeneralizedType().equals(type))
                .count() > 0;
    }

    public static boolean featuresOnlyDetailedMots(Route route, Collection<DetailedModeOfTransportType> types) {
        HashSet<DetailedModeOfTransportType> mots = route.getSegments().stream()
                .filter(s -> s.getModeOfTransport().getDetailedType().isPresent())
                .map(s -> s.getModeOfTransport().getDetailedType().get()).collect(HashSet::new, Set::add, Set::addAll);
        mots.removeAll(types);
        return mots.isEmpty();
    }

    public static boolean featuresSharedGeneralizedMot(Route route, GeneralizedModeOfTransportType type) {
        return route.getSegments().stream().filter(s -> s.getModeOfTransport().getGeneralizedType().equals(type))
                .filter(s -> s.getModeOfTransport().getSharingType().isPresent()).count() > 0;
    }

    public static int countChanges(Route route) {
        return (int) route.getSegments().stream()
                .filter(s -> !s.getModeOfTransport().getGeneralizedType().equals(GeneralizedModeOfTransportType.FOOT))
                .count() - 1;
    }

    /**
     * @return the seconds spent waiting for public transport vehicles
     */
    public static int getWaitingSecondsForPublicTransport(Route route) {
        int seconds = route.getSegments().stream()
                .filter(s -> s.getModeOfTransport().getGeneralizedType()
                        .equals(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT))
                .mapToInt(s -> s.getBoardingSeconds().orElse(0)).sum();
        seconds += route.getSegments().stream()
                .filter(s -> s.getModeOfTransport().getDetailedType()
                        .equals(Optional.of(DetailedModeOfTransportType.TRANSFER)))
                .mapToInt(s -> s.getAlightingSeconds().orElse(0)).sum();
        return seconds;
    }

    /**
     * @return the number of unique mots, where 'classic' public transport only
     *         counts as one mot
     */
    public static int countUniqueModesOfTransport(Route route) {
        boolean containsPublicTransport = route.getSegments().stream().filter(s -> s.getModeOfTransport()
                .getGeneralizedType().equals(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT)).count() > 0;

        int nonPublicTransportCount = route.getSegments().stream()
                .filter(s -> !s.getModeOfTransport().getGeneralizedType()
                        .equals(GeneralizedModeOfTransportType.PUBLIC_TRANSPORT))
                .filter(s -> !s.getModeOfTransport().getDetailedType()
                        .equals(Optional.of(DetailedModeOfTransportType.TRANSFER)))
                .map(s -> s.getModeOfTransport()).collect(Collectors.toSet()).size();

        return nonPublicTransportCount + (containsPublicTransport ? 1 : 0);
    }

}
