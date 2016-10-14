package at.ac.ait.ariadne.routeformat;

public interface Validatable {
	/**
	 * Checks if the instance is in a valid / useful state. If this is not the
	 * case an {@link IllegalArgumentException} including a description of the
	 * invalid state is thrown.
	 */
	void validate();
}
