package at.ac.ait.sproute.routeformat.instruction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Navigation instruction
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = BasicRoadInstruction.class, name = "BasicRoadInstruction"),
		@JsonSubTypes.Type(value = RoundaboutInstruction.class, name = "RoundaboutInstruction") })
public interface Instruction {

}
