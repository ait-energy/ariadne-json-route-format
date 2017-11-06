# Ariadne JSON Route Format

The Ariadne JSON route format is a JSON exchange format of routes for routing services and their clients. The specification is provided in form of a reference implementation using Java 8 and [Jackson](https://github.com/FasterXML/jackson). It is developed by the [AIT Austrian Institute of Technology](https://www.ait.ac.at/en/about-the-ait/center/center-for-mobility-systems) and used as default format by the AIT routing framework Ariadne.

The format both specifies routing requests to a server as well as server responses containing routes. Key feature is the support of intermodality, i.e. the use of mulitple modes of transport within a single request or route.

A route does not only consist of the geometry but also holds optional information such as detailed specification of the used mode of transports or navigation instructions.

Geometries can either be represented in the [GeoJSON](http://geojson.org) format as specified in RFC 7946 or as [encoded polyline](https://developers.google.com/maps/documentation/utilities/polylinealgorithm). See e.g. [com.google.maps:google-maps-services](https://googlemaps.github.io/google-maps-services-java/v0.2.3/javadoc/index.html?com/google/maps/model/EncodedPolyline.html) for an encoder/decoder in Java. 

The format for dates follows ISO 8601 with mandatory time zone offset and precision in seconds. The dates must therefore be in the schema `yyyy-MM-dd'T'HH:mm:ssXXX`, e.g. `2017-12-31T23:59:59+01:00`.

## Usage

The format can easily be used via the Java reference implementation for either Java 8 (preferred) or 7 (for legacy environments or Android development).
For other programming languages you have to roll your own implementation for reading and writing in the format.
We release to the [Maven Central Repository](https://search.maven.org).
To use the current release version just add this dependency to your `pom.xml`: 

	<dependency>
		<groupId>at.ac.ait</groupId>
		<artifactId>ariadne-json-route-format</artifactId>
		<version>0.19</version>
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

The route format is defined through the Java classes in the package (at.ac.ait.ariadne.routeformat)[src/main/java/at/ac/ait/ariadne/routeformat], most noteworthy (RoutingRequest.java)[src/main/java/at/ac/ait/ariadne/routeformat/RoutingRequest.java] and (RoutingResponse.java)[src/main/java/at/ac/ait/ariadne/routeformat/RoutingResponse.java].

A fictional request and an intermodal route demonstrate the features of this format in (IntermodalRouteExample.java)[src/main/java/at/ac/ait/ariadne/routeformat/example/IntermodalRouteExample.java]

Reading and writing of a route and exporting the JSON schema with Jackson is demonstrated in (JacksonExample.java)[src/main/java/at/ac/ait/ariadne/routeformat/example/JacksonExample.java].

A simple example how to display a route in a browser using (Leaflet)[http://leafletjs.com] is shown in (ariadne-json-route-format_example_leaflet.html)[src/main/resources/ariadne-json-route-format_example_leaflet.html).


### Further Hints

- Compatibility issues: when parsing a route .json with an older version of the route format it can happen that new and therefore unknown attributes are present in the .json file. In that case Jackson by default throws an `UnrecognizedPropertyException`. Configure your `ObjectMapper` with `mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)` to avoid the exception.


## Reference Implementation

The main reference implementation is provided in Java 8 and follows these coding guidlines.

- member variables are private and mutable
    - initialization of complex types when they are defined and so that they are mutable (i.e. `new HashMap<>()` instead of `Collections.emptyMap()`)
- getter methods for all member variables (used by Jackson for serialization)
- setter methods for all member variables (used by Jackson for deserialization)
    - create defensive mutable copies of common (nested) collections such as lists, sets or maps (e.g. additionalInfo or the list of coordinates in a GeoJSONLineString), but not of complex objects (e.g. a GeoJSONLineString itself)
    - return the object itself (so calls to setter methods can be chained similar to the builder pattern)
- (mostly implicit) public constructor without arguments (used by Jackson for deserialization)
- static `createMinimal()` methods as shortcuts for building minimal (or typically used) instances where it makes sense, i.e. not for classes where nearly all attributes are mandatory.
- for easy generic extension the `Map<String, Object>` "additionalInfo" is provided for many classes
- `validate()` method for checking if the state of the instance is legal, which throws an `IllegalArgumentException` including a description of what is invalid (in-depth-checking: classes should call validate on all instances they contain, e.g. a navigation instruction calls `validate()` of the landmarks it contains)
- optional member variables
    - `java.util.Optional<T>` is the type of the member variable and the getter (not Java serializable, but this is not a requirement for now)
    - `T` is used as argument for the setter: the value is set with `Optional.ofNullable(t)`, so that unsetting of a member is possible by passing `null`
- `java.time.ZonedDateTime` is used to represent time stamps,
- meaningful implementation of `toString()`
- `hashCode()` and `equals()` are implemented for all data classes
- documentation for individual fields (if present) is always located at the public getters (not the setters)


### Java 7 Version

For legacy environments such as Android (where e.g. ZonedDateTime is only available since API level 26) a backport to Java 7 is also available.

The main differences are as follows.

- `com.google.common.base.Optional` replaces `java.util.Optional`
- `java.util.Date` replaces `java.time.ZonedDateTime`
- the utility class `Routes.java` is not ported yet


## Funding

Initial development was done together with [Fluidtime](http://www.fluidtime.com) within the research project 'sproute', which was funded by the Vienna Business Agency (Call From Science to Products 2013).


## License

Everything in this repository is licensed under CC0.