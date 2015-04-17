package at.ac.ait.sproute.routeformat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.ac.ait.sproute.routeformat.Location.Builder;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.sproute.routeformat.geojson.GeoJSONPoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;

/**
 * @author AIT Austrian Institute of Technology GmbH
 */
@JsonDeserialize(builder = Builder.class)
public class Location {

	public enum LocationType {
		/** simple coordinate without additional information */
		COORDINATE,
		/** house number */
		ADDRESS,
		/** point of interest (probably with house number) */
		POI, PUBLIC_TRANSPORT_STOP // TODO more? e.g. Bike-Sharing-Station,...
	};

	private LocationType type;
	private GeoJSONFeature<GeoJSONPoint> coordinate;
	// typical fields for: ADDRESS & POI & PUBLIC_TRANSPORT_STOP
	private Optional<String> country;
	private Optional<String> city;
	private Optional<String> postcode;
	private Optional<String> streetname;
	private Optional<String> housenumber;
	// typical fields for: POI & PUBLIC_TRANSPORT_STOP
	private Optional<String> name;
	// typical fields for: POI
	private Optional<String> poiType;
	// typical fields for: PUBLIC_TRANSPORT_STOP
	private List<String> relatedLines;
	private Optional<Boolean> wheelchairAccessible;

	@JsonProperty(required = true)
	public LocationType getType() {
		return type;
	}

	@JsonProperty(required = true)
	public GeoJSONFeature<GeoJSONPoint> getCoordinate() {
		return coordinate;
	}

	public Optional<String> getCountry() {
		return country;
	}

	public Optional<String> getCity() {
		return city;
	}

	public Optional<String> getPostcode() {
		return postcode;
	}

	public Optional<String> getStreetname() {
		return streetname;
	}

	public Optional<String> getHousenumber() {
		return housenumber;
	}

	public Optional<String> getName() {
		return name;
	}

	public Optional<String> getPoiType() {
		return poiType;
	}

	/** public transport lines the user can change to at this stop */
	public List<String> getRelatedLines() {
		return relatedLines;
	}

	public Optional<Boolean> getWheelchairAccessible() {
		return wheelchairAccessible;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	private Location(Builder builder) {
		this.type = builder.type;
		this.coordinate = builder.coordinate;
		this.country = builder.country;
		this.city = builder.city;
		this.postcode = builder.postcode;
		this.streetname = builder.streetname;
		this.housenumber = builder.housenumber;
		this.name = builder.name;
		this.poiType = builder.poiType;
		this.relatedLines = builder.relatedLines;
		this.wheelchairAccessible = builder.wheelchairAccessible;
	}

	public static class Builder {

		private LocationType type;
		private GeoJSONFeature<GeoJSONPoint> coordinate;
		private Optional<String> country;
		private Optional<String> city;
		private Optional<String> postcode;
		private Optional<String> streetname;
		private Optional<String> housenumber;
		private Optional<String> name;
		private Optional<String> poiType;
		private List<String> relatedLines;
		private Optional<Boolean> wheelchairAccessible;

		public Builder withType(LocationType type) {
			this.type = type;
			return this;
		}

		public Builder withCoordinate(GeoJSONFeature<GeoJSONPoint> coordinate) {
			this.coordinate = coordinate;
			return this;
		}

		public Builder withCountry(String country) {
			this.country = Optional.of(country);
			return this;
		}

		public Builder withCity(String city) {
			this.city = Optional.of(city);
			return this;
		}

		public Builder withPostcode(String postcode) {
			this.postcode = Optional.of(postcode);
			return this;
		}

		public Builder withStreetname(String streetname) {
			this.streetname = Optional.of(streetname);
			return this;
		}

		public Builder withHousenumber(String housenumber) {
			this.housenumber = Optional.of(housenumber);
			return this;
		}

		public Builder withName(String name) {
			this.name = Optional.of(name);
			return this;
		}

		public Builder withPoiType(String poiType) {
			this.poiType = Optional.of(poiType);
			return this;
		}

		public Builder withRelatedLines(List<String> relatedLines) {
			this.relatedLines = new ArrayList<>(relatedLines);
			return this;
		}

		public Builder withWheelchairAccessible(boolean wheelchairAccessible) {
			this.wheelchairAccessible = Optional.of(wheelchairAccessible);
			return this;
		}

		public Location build() {
			validate();
			return new Location(this);
		}

		private void validate() {
			Preconditions.checkArgument(type != null, "type is mandatory but missing");
			Preconditions.checkArgument(coordinate != null, "coordinate is mandatory but missing");
			
			if(type == LocationType.ADDRESS) {
				Preconditions.checkArgument(housenumber.isPresent(), "housenumber is mandatory for addresses but missing");
				Preconditions.checkArgument(streetname.isPresent(), "streetname is mandatory for addresses but missing");
			} else if(type == LocationType.POI) {
				Preconditions.checkArgument(name.isPresent(), "name is mandatory for POIs but missing");
				Preconditions.checkArgument(poiType.isPresent(), "poiType is mandatory for POIs but missing");
			} else if(type == LocationType.PUBLIC_TRANSPORT_STOP) {
				Preconditions.checkArgument(name.isPresent(), "name is mandatory for public transport stops but missing");
			}
		}
	}

}
