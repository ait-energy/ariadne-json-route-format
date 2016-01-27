package at.ac.ait.sproute.routeformat;

import static at.ac.ait.sproute.routeformat.Sproute.ModeOfTransport.PUBLIC_TRANSPORT;

public class Sproute {

	public enum AccessibilityRestriction {
		STAIRS, ESCALATOR, ELEVATOR, NOT_WHEELHAIR_ACCESSIBLE
	}

	public enum CompassDirection {
		N, NE, E, SE, S, SW, W, NW
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

	public enum ModeOfTransport {
		/** waiting for e.g. a bus */
		WAIT, FOOT, BICYCLE, MOTORCYCLE, CAR, PUBLIC_TRANSPORT
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

	public enum TurnDirection {
		STRAIGHT, SLIGHT_LEFT, SLIGHT_RIGHT, LEFT, RIGHT, SHARP_LEFT, SHARP_RIGHT, U_TURN;
	}

	public enum VehicleType {
		// "classic" public transport
		TRAIN(PUBLIC_TRANSPORT), LIGHTRAIL(PUBLIC_TRANSPORT), SUBWAY(PUBLIC_TRANSPORT), MONORAIL(PUBLIC_TRANSPORT), TRAM(
				PUBLIC_TRANSPORT), BUS(PUBLIC_TRANSPORT), TROLLEYBUS(PUBLIC_TRANSPORT), AERIALWAY(PUBLIC_TRANSPORT), FERRY(
				PUBLIC_TRANSPORT),
		// individual transport
		BICYCLE(ModeOfTransport.BICYCLE), MOTORCYCLE(ModeOfTransport.MOTORCYCLE), CAR(ModeOfTransport.CAR),
		// taxi
		TAXI(ModeOfTransport.CAR);

		private final ModeOfTransport mot;

		private VehicleType(ModeOfTransport mot) {
			this.mot = mot;
		}

		public ModeOfTransport getModeOfTransport() {
			return mot;
		}
	}

}
