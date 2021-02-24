package at.ac.ait.ariadne.routeformat;

import static at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType.PUBLIC_TRANSPORT;

import at.ac.ait.ariadne.routeformat.features.Site;
import at.ac.ait.ariadne.routeformat.instruction.AreaInstruction;

/**
 * {@link Constants} holds all {@link Enum}s of the route format
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Constants {

    public enum Accessibility {
        STAIRS_DOWN, STAIRS_UP, ESCALATOR_DOWN, ESCALATOR_UP, ELEVATOR_DOWN, ELEVATOR_UP
    }

    public enum VehicleAccessibility {
        LOW_FLOOR_VEHICLE, HIGH_FLOOR_VEHICLE
    }

    public enum AccessibilityRestriction {
        NO_STAIRS, NO_ESCALATOR, NO_ELEVATOR, ONLY_LOW_FLOOR_VEHICLE
    }

    public enum GeneralizedModeOfTransportType {
        /** walking */
        FOOT,
        /**
         * riding a bicycle, e-bike, or other vehicles classified as bicycles
         * (e.g. segways)
         */
        BICYCLE,
        /**
         * single-tracked motorized transport, e.g. mofa, moped, motorcycle
         */
        MOTORCYCLE,
        /**
         * double-tracked motorized transport (that is not public transport),
         * e.g. cars, trucks, motorhomes,..
         */
        CAR,
        /**
         * all forms of public transport such as busses, trams, trains,..
         */
        PUBLIC_TRANSPORT
    }

    public enum DetailedModeOfTransportType {
        // Note: maybe switch to TPEG-HVT in the future?
        // https://support.google.com/transitpartners/answer/3520902?hl=en

        // "classic" public transport
        /** trains (in general) */
        RAILWAY(PUBLIC_TRANSPORT),
        /** high-speed / long distance trains such as the Austrian RailJet */
        LONG_DISTANCE_RAILWAY(PUBLIC_TRANSPORT), SUBURBAN_RAILWAY(PUBLIC_TRANSPORT), URBAN_RAILWAY(PUBLIC_TRANSPORT),
        /** metro */
        SUBWAY(PUBLIC_TRANSPORT),
        //
        TRAM(PUBLIC_TRANSPORT),
        /**
         * ground-based vehicles that are pulled by a cable (such as the popular
         * cable cars in San Francisco). For gondolas and chairlift (as known
         * from skiing resorts) are described with
         * BasePublicTransportMot#AERIALWAY
         */
        CABLE_CAR(PUBLIC_TRANSPORT),
        /**
         * a special type of BasePublicTransportMot#CABLE_CAR, see
         * https://en.wikipedia.org/wiki/Funicular
         */
        FUNICULAR(PUBLIC_TRANSPORT),
        //
        BUS(PUBLIC_TRANSPORT), TROLLEYBUS(PUBLIC_TRANSPORT),
        //
        /**
         * e.g. gondolas, cable cars, chair lifts,.. see
         * http://wiki.openstreetmap.org/wiki/Key:aerialway
         */
        AERIALWAY(PUBLIC_TRANSPORT),
        /** water-bound transport */
        SHIP(PUBLIC_TRANSPORT),
        /** air-bound transport */
        AIRPLANE(PUBLIC_TRANSPORT),
        // individual transport
        /** walking */
        FOOT(GeneralizedModeOfTransportType.FOOT),
        KICK_SCOOTER(GeneralizedModeOfTransportType.FOOT),
        /**
         * for more recent / obscure types not covered, see https://en.wikipedia.org/wiki/Personal_transporter
         */
        PERSONAL_TRANSPORTER(GeneralizedModeOfTransportType.FOOT),
        WHEELCHAIR(GeneralizedModeOfTransportType.FOOT),
        /** motorized wheelchair: https://en.wikipedia.org/wiki/Mobility_scooter */
        MOBILITY_SCOOTER(GeneralizedModeOfTransportType.FOOT),
        /**
         * walking or taking elevators, escalators,.. within public transport
         * stations
         */
        TRANSFER(GeneralizedModeOfTransportType.FOOT),
        //
        BICYCLE(GeneralizedModeOfTransportType.BICYCLE),
        TRICYCLE(GeneralizedModeOfTransportType.BICYCLE),
        CARGO_BIKE(GeneralizedModeOfTransportType.BICYCLE),
        CARGO_TRICYCLE(GeneralizedModeOfTransportType.BICYCLE),
        /** e.g. e-scooters https://en.wikipedia.org/wiki/Motorized_scooter */
        MOTORIZED_SCOOTER(GeneralizedModeOfTransportType.BICYCLE),
        VELOMOBILE(GeneralizedModeOfTransportType.BICYCLE),
        BICYCLE_RICKSHAW(GeneralizedModeOfTransportType.BICYCLE),
        //
        MOTORCYCLE(GeneralizedModeOfTransportType.MOTORCYCLE),
        MOPED(GeneralizedModeOfTransportType.MOTORCYCLE),
        //
        CAR(GeneralizedModeOfTransportType.CAR),
        /**
         * motor home / recreational vehicle (RV), see
         * https://en.wikipedia.org/wiki/Motorhome
         */
        MOTORHOME(GeneralizedModeOfTransportType.CAR),
        /**
         * heavy goods vehicle / truck / lorry, see
         * https://en.wikipedia.org/wiki/Large_goods_vehicle
         */
        HGV(GeneralizedModeOfTransportType.CAR),
        /**
         * 'classic' taxis / cabs (for modern ride-sharing such as Uber use
         * {@link DetailedModeOfTransportType#CAR} and a proper {@link Sharing}.
         */
        TAXI(GeneralizedModeOfTransportType.CAR);

        private final GeneralizedModeOfTransportType mot;

        private DetailedModeOfTransportType(GeneralizedModeOfTransportType mot) {
            this.mot = mot;
        }

        public GeneralizedModeOfTransportType getGeneralizedType() {
            return mot;
        }
    }

    /**
     * Detailed classification of the parking type
     */
    public enum ParkingType {
        /** surface parking with no or little protection from the elements */
        SURFACE,
        /** underground car park (in a building) */
        UNDERGROUND,
        /** car park in a building (DE: Parkhaus) */
        CARPARK
    }

    /**
     * Detailed classification of the sharing type
     */
    public enum Sharing {
        /** e.g. Citybike Vienna, Citibike New York, Zipcar */
        STATION_BOUND_VEHICLE_SHARING,
        /** e.g. Car2Go */
        FREE_FLOATING_VEHICLE_SHARING,
        /** e.g. shared taxi or flinc */
        RIDE_SHARING, RIDE_SOURCING
    }

    public enum Speed {
        SLOW, NORMAL, FAST
    }

    public enum FormOfWay {
        MOTORWAY,

        /** everything below motorways and down to residential roads */
        ROAD,

        /** road crossing for cyclists / pedestrians */
        ROAD_CROSSING,

        /** */
        LIVING_STREET,

        /** dedicated cyclepath (no pedestrians or other vehicles allowed) */
        CYCLEPATH,

        /** dedicated mixed foot and cyclepath */
        FOOT_CYCLEPATH,

        /** dedicated footpath (no vehicles allowed) */
        FOOTPATH,

        /** sidewalk that runs along a street, typically only for pedestrians */
        SIDEWALK,

        /** */
        PEDESTRIAN_ZONE,

        /** */
        STAIRS,

        /** small path, typically foot-only and/or unpaved */
        PATH
    }

    /**
     * Types of areas used in {@link AreaInstruction}
     */
    public enum Area {
        /** open area or plaza */
        SQUARE, PARK, DISTRICT
    }

    /**
     * Detailed specification if a street is covered or a tunnel. See
     * http://wiki.openstreetmap.org/wiki/Key:tunnel and
     * http://wiki.openstreetmap.org/wiki/Key:covered for more details.
     */
    public enum Tunnel {
        /** neither a tunnel nor covered */
        NO,
        /** classic tunnel */
        TUNNEL,
        /** a more specific variant of {@link Tunnel#TUNNEL} */
        BUILDING_PASSAGE,
        /** a more specific variant of {@link Tunnel#TUNNEL} */
        AVALANCHE_PROTECTOR,
        /** only covered, not a real tunnel */
        COVERED,
        /** a more specific variant of {@link Tunnel#COVERED} */
        ARCADE,
        /** a more specific variant of {@link Tunnel#COVERED} */
        COLONNADE
    }

    public enum CompassDirection {
        N, NE, E, SE, S, SW, W, NW
    }

    public enum TurnDirection {
        STRAIGHT, SLIGHT_LEFT, SLIGHT_RIGHT, LEFT, RIGHT, SHARP_LEFT, SHARP_RIGHT, U_TURN;
    }

    public enum RelativeDirection {
        FRONT, FRONT_LEFT, FRONT_RIGHT, LEFT, RIGHT, BACK_LEFT, BACK_RIGHT, BACK;
    }

    public enum Preposition {
        BEFORE, AT, AFTER, TOWARDS, THROUGH, ALONG, PAST
    }

    public enum ContinueDirection {
        SAME, OPPOSITE
    }

    public enum RoadCrossing {
        /** not a crossing */
        NO,
        /** generic crossing, no detailed information */
        CROSSING,
        /**
         * neither marked nor controlled via traffic signals, but it is possible
         * to cross the road here
         */
        UNMARKED,
        /**
         * marked (e.g. zebra crossing) but not controlled via traffic signals
         */
        UNCONTROLLED,
        /** controlled with traffic signals and most probably marked */
        TRAFFIC_SIGNALS
    }

    public enum Status {
        /**
         * Everything OK, route(s) are available.
         */
        OK,
        /**
         * Problems occurred when routing request parameters were parsed -
         * neither request nor routes are available.
         */
        INVALID_REQUEST,
        /**
         * Error while routing (or general error). No routes are available.
         */
        ERROR;
    }

    /**
     * Features a routing service supports for a {@link Site}. A
     * {@link RoutingRequest} should be built accordingly.
     */
    public enum SiteFeature {
        /**
         * more finegrained control over modes of transport, see
         * {@link RequestModeOfTransport#getUserAffinity()}
         */
        MOT_USER_AFFINITY,
        /**
         * route calculation can respect departure times
         */
        DEPARTURE_TIME,
        /**
         * route calculation can respect arrival times
         */
        ARRIVAL_TIME
    }

    public enum OutputFormat {
        /**
         * provides {@link RouteSegment#getGeometryEncodedPolyLine()}
         */
        ENCODED_POLYLINE,
        /**
         * provides {@link RouteSegment#getGeometryGeoJson()}
         */
        GEOJSON,
        /**
         * provides {@link RouteSegment#getGeometryGeoJsonEdges()}
         */
        GEOJSON_EDGES,
        /**
         * provides {@link Route#getSimplifiedGeometryEncodedPolyLine()}
         */
        SIMPLIFIED_ENCODED_POLYLINE,
        /**
         * provides {@link Route#getSimplifiedGeometryGeoJson()}
         */
        SIMPLIFIED_GEOJSON
    }

}
