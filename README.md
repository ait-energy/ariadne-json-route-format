# Ariadne JSON Route Format
This repository holds the JSON exchange format for intermodal routes developed by the business unit Dynamic Transportation Systems of the [AIT Austrian Institute of Technology](http://dts.ait.ac.at). It is the default format used by the AIT routing framework Ariadne.

It specifies the format of a routing request as well as the format of the returned route(s). A route does not only consist of the geometry but also optional information such as detailed specification of the used mode of transports or navigation instructions.
Geometries are represented in the [GeoJSON](http://geojson.org) format as specified in RFC 7946.

## Usage
We release to the [Maven Central Repository](https://search.maven.org).
To use the current release version just add this dependency to your `pom.xml`: 

	<dependency>
		<groupId>at.ac.ait</groupId>
		<artifactId>ariadne-json-route-format</artifactId>
		<version>0.18</version>
	</dependency>

To use SNAPSHOT versions you must also add the following repository in your `pom.xml` under `<project> <repositories>`:

	<repository>
	    <id>oss-sonatype</id>
	    <name>oss-sonatype</name>
	    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
	    <snapshots>
	        <enabled>true</enabled>
	    </snapshots>
	</repository> 

The route format is defined through the Java classes in the package `at.ac.ait.ariadne.routeformat`, most noteworthy `RouteFormatRoot.java`.

Reading and writing of a route and exporting the JSON schema with Jackson is demonstrated in `JacksonExample.java`.

A simple example how to display a route in a browser with Leaflet is shown in `src/main/resources/ariadne-json-route-format_example_leaflet.html`.

### Further Hints

- Compatibility issues: when parsing a route .json with an older version of the route format it can happen that new and therefore unknown attributes are present in the .json file. In that case Jackson by default throws an `UnrecognizedPropertyException`. Configure your `ObjectMapper` with `mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)` to avoid the exception.

## Coding Style
- member variables are private and mutable
    - initialization of complex types when they are defined and so that they are mutable (i.e. `new HashMap<>()` instead of `Collections.emptyMap()`)
- getter methods for all member variables (used by jackson for serialization)
- setter methods for all member variables (used by jackson for deserialization)
    - create defensive mutable copies of common (nested) collections such as lists, sets or maps (e.g. additionalInfo or the list of coordinates in a GeoJSONLineString), but not of complex objects (e.g. a GeoJSONLineString itself)
    - return the object itself (so calls to setter methods can be chained similar to the builder pattern)
- (mostly implicit) public constructor without arguments (used by jackson for deserialization)
- static `createMinimal()` methods as shortcuts for building minimal (or typically used) instances where it makes sense, i.e. not for classes where nearly all attributes are mandatory.
- for easy generic extension the `Map<String, Object>` "additionalInfo" is provided for many classes
- `validate()` method for checking if the state of the instance is legal, which throws an `IllegalArgumentException` including a description of what is invalid (in-depth-checking: classes should call validate on all instances they contain, e.g. a navigation instruction calls `validate()` of the landmarks it contains)
- optional member variables
    - `java.util.Optional<T>` is the type of the member variable and the getter (not Java serializable, but this is not a requirement for now)
    - `T` is used as argument for the setter: the value is set with `Optional.ofNullable(t)`, so that unsetting of a member is possible by passing `null`
- `java.time.ZonedDateTime` is used to represent time stamps
- meaningful implementation of `toString()`
- `hashCode()` and `equals()` are implemented for all data classes
- documentation for individual fields (if present) is always located at the public getters (not the setters)

## Funding
Initial development was done together with [Fluidtime](http://www.fluidtime.com) within the research project 'sproute', which was funded by the Vienna Business Agency (Call From Science to Products 2013).

## License
Everything in this repository is licensed under CC0.