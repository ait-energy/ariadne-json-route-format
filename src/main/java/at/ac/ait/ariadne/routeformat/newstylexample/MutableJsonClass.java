package at.ac.ait.ariadne.routeformat.newstylexample;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.ImmutableMap;

@JsonInclude(Include.NON_EMPTY)
public class MutableJsonClass {

	public enum ContinueDirection {
		SAME, OPPOSITE
	}

	private int myInteger;
	private Optional<String> myOptionalString = Optional.empty();
	private Map<String, ContinueDirection> myMap = Collections.emptyMap();

	public static MutableJsonClass createDefault(int myInteger, String myOptionalString) {
		MutableJsonClass x = new MutableJsonClass();
		x.setMyInteger(myInteger);
		x.setMyOptionalString(myOptionalString);
		return x;
	}

	public int getMyInteger() {
		return myInteger;
	}

	public MutableJsonClass setMyInteger(int myInteger) {
		this.myInteger = myInteger + 66;
		return this;
	}

	public Optional<String> getMyOptionalString() {
		return myOptionalString;
	}

	public MutableJsonClass setMyOptionalString(String myOptionalString) {
		this.myOptionalString = Optional.ofNullable("setterwasused_" + myOptionalString);
		return this;
	}

	public Map<String, ContinueDirection> getMyMap() {
		return myMap;
	}

	public MutableJsonClass setMyMap(Map<String, ContinueDirection> myMap) {
		this.myMap = ImmutableMap.copyOf(myMap);
		return this;
	}

	@Override
	public String toString() {
		return "MinimizedMutableClass [myInteger=" + myInteger + ", myOptionalString=" + myOptionalString + ", myMap="
				+ myMap + "]";
	}

}
