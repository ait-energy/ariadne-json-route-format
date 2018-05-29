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
@JsonInclude(Include.NON_ABSENT)
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((areaName == null) ? 0 : areaName.hashCode());
        result = prime * result + ((areaType == null) ? 0 : areaType.hashCode());
        result = prime * result + ((exitLandmark == null) ? 0 : exitLandmark.hashCode());
        result = prime * result + ((exitStreetName == null) ? 0 : exitStreetName.hashCode());
        result = prime * result + ((subType == null) ? 0 : subType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AreaInstruction other = (AreaInstruction) obj;
        if (areaName == null) {
            if (other.areaName != null)
                return false;
        } else if (!areaName.equals(other.areaName))
            return false;
        if (areaType == null) {
            if (other.areaType != null)
                return false;
        } else if (!areaType.equals(other.areaType))
            return false;
        if (exitLandmark == null) {
            if (other.exitLandmark != null)
                return false;
        } else if (!exitLandmark.equals(other.exitLandmark))
            return false;
        if (exitStreetName == null) {
            if (other.exitStreetName != null)
                return false;
        } else if (!exitStreetName.equals(other.exitStreetName))
            return false;
        if (subType != other.subType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " -> AreaInstruction [subType=" + subType + ", areaName=" + areaName + ", areaType="
                + areaType + ", exitStreetName=" + exitStreetName + ", exitLandmark=" + exitLandmark + "]";
    }

}
