package at.ac.ait.ariadne.routeformat.instruction;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.Preconditions;

import at.ac.ait.ariadne.routeformat.Constants.Area;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * Instructions for entering / exiting an area such as a square, a park, or a
 * district.
 * <p>
 * In its minimal form it consists of a position, defines if it refers to
 * entering or leaving, and one of area type and area name.
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into human-readable
 * text and what's mandatory / optional. Elements ending with STRING are
 * terminal (not defined any further).
 * <p>
 * 
 * <pre>
 * {@code
 * AREA_INSTRUCTION = ENTER | EXIT;
 * ENTER = "Cross", AREA, [TOWARDS];
 * TOWARDS = towards [EXIT_LANDMARK_STRING] and [EXIT_STREET_NAME_STRING]; 
 * EXIT = "Now you leave", AREA;
 * AREA = [AREA_TYPE_STRING], [AREA_NAME_STRING]; (* at least one of the two *)
 * }
 * </pre>
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class AreaInstruction extends Instruction<AreaInstruction> {

    public enum SubType {
        ENTER, EXIT
    }

    private SubType subType;
    private Optional<String> areaName = Optional.empty();
    private Optional<Area> areaType = Optional.empty();
    private Optional<String> exitStreetName = Optional.empty();
    private Optional<Landmark> exitLandmark = Optional.empty();

    // -- getters

    public SubType getSubType() {
        return subType;
    }

    public Optional<String> getAreaName() {
        return areaName;
    }

    public Optional<Area> getAreaType() {
        return areaType;
    }

    public Optional<String> getExitStreetName() {
        return exitStreetName;
    }

    public Optional<Landmark> getExitLandmark() {
        return exitLandmark;
    }

    // -- setters

    public AreaInstruction setSubType(SubType subType) {
        this.subType = subType;
        return this;
    }

    public AreaInstruction setAreaName(String areaName) {
        this.areaName = Optional.ofNullable(areaName);
        return this;
    }

    public AreaInstruction setAreaType(Area areaType) {
        this.areaType = Optional.ofNullable(areaType);
        return this;
    }

    public AreaInstruction setExitStreetName(String exitStreetName) {
        this.exitStreetName = Optional.ofNullable(exitStreetName);
        return this;
    }

    public AreaInstruction setExitLandmark(Landmark exitLandmark) {
        this.exitLandmark = Optional.ofNullable(exitLandmark);
        return this;
    }

    // --

    /**
     * Note, that one of {@link #setAreaName(String)} or
     * {@link #setAreaType(Area)} is mandatory as well
     */
    public static AreaInstruction createMinimalEnter(GeoJSONCoordinate position) {
        return new AreaInstruction().setPosition(position).setSubType(SubType.ENTER);
    }

    /**
     * Note, that one of {@link #setAreaName(String)} or
     * {@link #setAreaType(Area)} is mandatory as well
     */
    public static AreaInstruction createMinimalExit(GeoJSONCoordinate position) {
        return new AreaInstruction().setPosition(position).setSubType(SubType.EXIT);
    }

    @Override
    public void validate() {
        super.validate();
        Preconditions.checkArgument(subType != null, "subType is mandatory but missing");
        Preconditions.checkArgument(areaName.isPresent() || areaType.isPresent(),
                "at least one of area name and area type is required");
    }

    @Override
    public String toString() {
        return super.toString() + " -> AreaInstruction [subType=" + subType + ", areaName=" + areaName + ", areaType="
                + areaType + ", exitStreetName=" + exitStreetName + ", exitLandmark=" + exitLandmark + "]";
    }

}
