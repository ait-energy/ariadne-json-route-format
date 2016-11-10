package at.ac.ait.ariadne.routeformat.instruction;

// other missing instructions: SquareCrossingInstruction, HierarchicalEnterInstruction, HierarchicalExitInstruction

///**
// * A {@link ModeChangeInstruction} contains details about alighting and boarding modes of transport. This includes
// * changes from/to walking and changes within public transport, i.e. changing the line.
// * 
// * <p>
// * Exemplary EBNF of how this instruction can be transformed into human-readable text and what's mandatory / optional.
// * Elements ending with STRING are terminal (not defined any further).
// * 
// * <pre>
// * {@code
// * MODE_CHANGE_INSTRUCTION = [PREVIOUS_VEHICLE | PREVIOUS_PUBLIC_TRANSPORT], ENTER_NEW_MODE;
// * 
// * PREVIOUS_VEHICLE = "Park your", VEHICLE_STRING;
// * PREVIOUS_PUBLIC_TRANSPORT = ...
// * 
// * ENTER_NEW_MODE = "Take your bicycle" | "Take your car" | "Open the Car2Go with the license plate W-23456" | ...;
// * </pre>
// * 
// * @author AIT Austrian Institute of Technology GmbH
// */
////@JsonDeserialize(builder = Builder.class)
//@JsonInclude(Include.NON_EMPTY)
//public class ModeChangeInstruction extends Instruction {
//
//	ModeChangeInstruction(GeoJSONFeature<GeoJSONPoint> position,
//			Optional<GeoJSONFeature<GeoJSONPoint>> previewTriggerPosition,
//			Optional<GeoJSONFeature<GeoJSONPoint>> confirmationTriggerPosition) {
//		super(position, previewTriggerPosition, confirmationTriggerPosition);
//	}
//
//}
