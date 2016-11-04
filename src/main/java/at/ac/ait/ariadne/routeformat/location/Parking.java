package at.ac.ait.ariadne.routeformat.location;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import at.ac.ait.ariadne.routeformat.Constants.GeneralizedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.Constants.ParkingType;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONCoordinate;

/**
 * A parking facility.
 * <p>
 * In its minimal form it does not contain any additional attributes, but just
 * the information that this {@link Location} is a parking facility.
 * 
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonInclude(Include.NON_EMPTY)
public class Parking extends Location<Parking> {
	private Optional<ParkingType> parkingType = Optional.empty();
	private Set<GeneralizedModeOfTransportType> modesOfTransport = new TreeSet<>();
	private Optional<String> name = Optional.empty();
	private Optional<Boolean> fee = Optional.empty();
	private Optional<Boolean> parkAndRide = Optional.empty();
	private Optional<String> openingHours = Optional.empty();

	// -- getters

	public Optional<ParkingType> getParkingType() {
		return parkingType;
	}

	public Set<GeneralizedModeOfTransportType> getModesOfTransport() {
		return modesOfTransport;
	}

	public Optional<String> getName() {
		return name;
	}

	public Optional<Boolean> getFee() {
		return fee;
	}

	/**
	 * @return <code>true</code> if this is a park and ride facility
	 */
	public Optional<Boolean> getParkAndRide() {
		return parkAndRide;
	}

	/**
	 * @return a String in the <a href=
	 *         "https://wiki.openstreetmap.org/wiki/Key:opening_hours">opening_hours
	 *         format of OpenStreetMap</a>
	 */
	public Optional<String> getOpeningHours() {
		return openingHours;
	}

	// -- setters

	public Parking setParkingType(ParkingType parkingType) {
		this.parkingType = Optional.ofNullable(parkingType);
		return this;
	}

	public Parking setModesOfTransport(Set<GeneralizedModeOfTransportType> modesOfTransport) {
		this.modesOfTransport = modesOfTransport;
		return this;
	}

	public Parking setName(String name) {
		this.name = Optional.ofNullable(name);
		return this;
	}

	public Parking setFee(Boolean fee) {
		this.fee = Optional.ofNullable(fee);
		return this;
	}

	public Parking setParkAndRide(Boolean parkAndRide) {
		this.parkAndRide = Optional.ofNullable(parkAndRide);
		return this;
	}

	public Parking setOpeningHours(String openingHours) {
		this.openingHours = Optional.ofNullable(openingHours);
		return this;
	}

	// --

	public static Parking createMinimal(GeoJSONCoordinate position) {
		return new Parking().setCoordinate(position);
	}

	@Override
	public void validate() {
		super.validate();
		// no other requirements
	}

	@Override
	public String toString() {
		return super.toString() + " -> Parking [parkingType=" + parkingType + ", modesOfTransport=" + modesOfTransport
				+ ", name=" + name + ", fee=" + fee + ", parkAndRide=" + parkAndRide + ", openingHours=" + openingHours
				+ "]";
	}

}
