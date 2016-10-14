package at.ac.ait.ariadne.routeformat.newstylexample;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Validatable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = MutableJsonClass.class, name = "MutableJsonClass"),
		@JsonSubTypes.Type(value = DetailedMutableJsonClass.class, name = "DetailedMutableJsonClass") })
@JsonInclude(Include.NON_EMPTY)
public class MutableJsonClass implements Validatable {

	public enum SomeEnum {
		SAME, OPPOSITE
	}

	private int myInteger;
	private Optional<String> myOptionalString = Optional.empty();
	private Map<String, SomeEnum> myMap = new HashMap<>();

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
		this.myInteger = myInteger;
		return this;
	}

	public Optional<String> getMyOptionalString() {
		return myOptionalString;
	}

	public MutableJsonClass setMyOptionalString(String myOptionalString) {
		this.myOptionalString = Optional.ofNullable(myOptionalString);
		return this;
	}

	public Map<String, SomeEnum> getMyMap() {
		return myMap;
	}

	public MutableJsonClass setMyMap(Map<String, SomeEnum> myMap) {
		this.myMap = ImmutableMap.copyOf(myMap);
		return this;
	}

	@Override
	public String toString() {
		return "MinimizedMutableClass [myInteger=" + myInteger + ", myOptionalString=" + myOptionalString + ", myMap="
				+ myMap + "]";
	}

	@Override
	public void validate() {
		Preconditions.checkArgument(myInteger > 0);
	}

}
