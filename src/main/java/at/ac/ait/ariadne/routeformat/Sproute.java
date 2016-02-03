package at.ac.ait.ariadne.routeformat;

import static at.ac.ait.ariadne.routeformat.Sproute.GeneralizedModeOfTransportType.PUBLIC_TRANSPORT;

/**
 * {@link Sproute} holds all {@link Enum}s of the sproute route format
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
public class Sproute {

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
		FOOT, BICYCLE, MOTORCYCLE, CAR, PUBLIC_TRANSPORT
	}

	public enum DetailedModeOfTransportType {
		// Note: maybe switch to TPEG-HVT in the future?
		// https://support.google.com/transitpartners/answer/3520902?hl=en

		// "classic" public transport
		TRAIN(PUBLIC_TRANSPORT), LIGHTRAIL(PUBLIC_TRANSPORT), SUBWAY(PUBLIC_TRANSPORT), TRAM(PUBLIC_TRANSPORT), BUS(
				PUBLIC_TRANSPORT), TROLLEYBUS(PUBLIC_TRANSPORT),
		/** e.g. gondolas, cable cars, chair lifts,.. see http://wiki.openstreetmap.org/wiki/Key:aerialway */
		AERIALWAY(PUBLIC_TRANSPORT),
		/** water-bound transport */
		SHIP(PUBLIC_TRANSPORT),
		/** air-bound transport */
		AIRPLANE(PUBLIC_TRANSPORT),
		// individual transport
		BICYCLE(GeneralizedModeOfTransportType.BICYCLE), MOTORCYCLE(GeneralizedModeOfTransportType.MOTORCYCLE), CAR(
				GeneralizedModeOfTransportType.CAR),
		// taxi
		TAXI(GeneralizedModeOfTransportType.CAR), FOOT(GeneralizedModeOfTransportType.FOOT),
		/** walking or taking elevators, escalators,.. within public transport stations */
		TRANSFER(GeneralizedModeOfTransportType.FOOT);

		private final GeneralizedModeOfTransportType mot;

		private DetailedModeOfTransportType(GeneralizedModeOfTransportType mot) {
			this.mot = mot;
		}

		public GeneralizedModeOfTransportType getGeneralizedType() {
			return mot;
		}
	}

	/**
	 * Detailed classification of the sharing type
	 */
	public enum Sharing {
		/** e.g. Citybike Vienna, Citibike New York, Zipcar */
		STATION_BOUND_VEHICLE_SHARING,
		/** e.g. Car2Go */
		FREE_FLOATING_VEHICLE_SHARING,
		/** e.g. flinc */
		RIDE_SHARING, RIDE_SOURCING
	}

	public enum FormOfWay {
		MOTORWAY,
		/** everything below motorways and down to residential roads */
		ROAD, LIVING_STREET,
		/** dedicated cyclepath */
		CYCLEPATH,
		/** dedicated footpath */
		FOOTPATH,
		/** dedicated mixed foot and cyclepath */
		FOOT_AND_CYCLEPATH, PEDESTRIAN_ZONE, STAIRS,
		/** small path, typically foot-only and/or unpaved */
		PATH
	}

	public enum CompassDirection {
		N, NE, E, SE, S, SW, W, NW
	}

	public enum TurnDirection {
		STRAIGHT, SLIGHT_LEFT, SLIGHT_RIGHT, LEFT, RIGHT, SHARP_LEFT, SHARP_RIGHT, U_TURN;
	}

	public enum Status {
		/**
		 * Everything OK, route(s) are available.
		 */
		OK,
		/**
		 * Problems occurred when routing request parameters were parsed - neither request nor routes are available.
		 */
		INVALID_REQUEST,
		/**
		 * Error while routing (or general error). No routes are available.
		 */
		ERROR;
	}

	/**
	 * @deprecated maybe this will be removed in the future.. client software should figure this out itself
	 */
	@Deprecated
	public enum RouteType {
		// unimodal routes
		/** only walking */
		FOOT,
		/** only cycling */
		BICYCLE,
		/** only driving (motorcycle) */
		MOTORCYCLE,
		/** only driving (car) */
		CAR,

		// limited intermodality (walking + public transport)
		/** only walking + public transport */
		PUBLIC_TRANSPORT,
		/** only walking + taxi */
		TAXI,
		/** only walking + taxi sharing */
		TAXI_SHARED,
		/** only walking + car sharing */
		CAR_SHARED,
		/** only walking + bicycle sharing */
		BICYCLE_SHARED,

		// full intermodal route
		/** "park and ride": walking + car + public transport */
		CAR_AND_PUBLIC_TRANSPORT,
		/** "bike and ride": cycling + car + public transport */
		BICYCLE_AND_PUBLIC_TRANSPORT,
		/** "park and ride" using car sharing: walking + shared car + public transport */
		SHARED_CAR_AND_PUBLIC_TRANSPORT,
		/** "bike and ride" using car sharing: walking + shared bicycle + public transport */
		SHARED_BICYCLE_AND_PUBLIC_TRANSPORT,
		/** walking + taxi + public transport */
		TAXI_AND_PUBLIC_TRANSPORT,
		/** walking + shared taxi + public transport */
		SHARED_TAXI_AND_PUBLIC_TRANSPORT,
		/** walking + car and bike sharing */
		SHARED_CAR_AND_SHARED_BICYCLE,

		/** generic intermodal route type not covered by the other types */
		INTERMODAL_OTHER
	}

}
