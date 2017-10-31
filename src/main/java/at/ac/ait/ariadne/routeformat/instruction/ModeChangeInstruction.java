package at.ac.ait.ariadne.routeformat.instruction;

import com.google.common.base.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.ModeOfTransport;

/**
 * A {@link ModeChangeInstruction} contains details about alighting and /
 * boarding modes of transport. This includes changes from/to walking and
 * changes within public transport, i.e. changing / the line.
 * <p>
 * Simple examples are:
 * <ul>
 * <li>Park your bicycle and board bus 76A.</li>
 * <li>At the restaurant park your bicycle and go to the Car2Go with the license
 * plate W-23456.</li>
 * </ul>
 * <p>
 * Exemplary EBNF of how this instruction can be transformed into /
 * human-readable text and what's mandatory / optional. Elements ending with
 * STRING are terminal (not defined any further).
 *
 * <pre>
 * {@code
 * MODE_CHANGE_INSTRUCTION = [LANDMARK_PART], [EXIT_PART], [ENTER_PART];
 * 
 * LANDMARK_PART = PREPOSITION, "the", LANDMARK_STRING;
 * PREPOSITION = "before" | "at" | "after";
 *
 * EXIT_PART = EXIT_INDIVIDUAL | EXIT_PUBLIC;
 * EXIT_INDIVIDUAL = "Park your", VEHICLE_STRING;
 * EXIT_PUBLIC = "Leave", PUBLIC_TRANSPORT_STRING;
 * 
 * ENTER_PART = ENTER_INDIVIDUAL | ENTER_PUBLIC;
 * ENTER_INDIVIDUAL = "Take your", VEHICLE_STRING;
 * ENTER_INDIVIDUAL = "Enter", VEHICLE_STRING;
 * }
 * </pre>
 *
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class ModeChangeInstruction extends Instruction<ModeChangeInstruction> {

    private Optional<ModeOfTransport> previousModeOfTransport, nextModeOfTransport;
    private Optional<Landmark> landmark = Optional.absent();

    // -- getters

    public Optional<ModeOfTransport> getPreviousModeOfTransport() {
        return previousModeOfTransport;
    }

    public Optional<ModeOfTransport> getNextModeOfTransport() {
        return nextModeOfTransport;
    }

    public Optional<Landmark> getLandmark() {
        return landmark;
    }

    // -- setters

    public ModeChangeInstruction setPreviousModeOfTransport(ModeOfTransport previousModeOfTransport) {
        this.previousModeOfTransport = Optional.fromNullable(previousModeOfTransport);
        return this;
    }

    public ModeChangeInstruction setNextModeOfTransport(ModeOfTransport nextModeOfTransport) {
        this.nextModeOfTransport = Optional.fromNullable(nextModeOfTransport);
        return this;
    }

    public ModeChangeInstruction setLandmark(Landmark landmark) {
        this.landmark = Optional.fromNullable(landmark);
        return this;
    }

    // --

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((landmark == null) ? 0 : landmark.hashCode());
        result = prime * result + ((nextModeOfTransport == null) ? 0 : nextModeOfTransport.hashCode());
        result = prime * result + ((previousModeOfTransport == null) ? 0 : previousModeOfTransport.hashCode());
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
        ModeChangeInstruction other = (ModeChangeInstruction) obj;
        if (landmark == null) {
            if (other.landmark != null)
                return false;
        } else if (!landmark.equals(other.landmark))
            return false;
        if (nextModeOfTransport == null) {
            if (other.nextModeOfTransport != null)
                return false;
        } else if (!nextModeOfTransport.equals(other.nextModeOfTransport))
            return false;
        if (previousModeOfTransport == null) {
            if (other.previousModeOfTransport != null)
                return false;
        } else if (!previousModeOfTransport.equals(other.previousModeOfTransport))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModeChangeInstruction [previousModeOfTransport=" + previousModeOfTransport + ", nextModeOfTransport="
                + nextModeOfTransport + ", landmark=" + landmark + "]";
    }

}
