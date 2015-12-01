package at.ac.ait.sproute.routeformat.instruction;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
public enum FormOfWay {
	MOTORWAY,
	/** everything below motorways and down to residential roads */
	ROAD, LIVING_STREET,
	/** dedicated cyclepath */
	CYCLEPATH,
	/** dedicated footpath */
	FOOTPATH,
	/** dedicated mixed foot & cyclepath */
	FOOT_AND_CYCLEPATH, PEDESTRIAN_ZONE, STAIRS,
	/** small path, typically foot-only and/or unpaved */
	PATH
}
