# ariadne-json-route-format
This repository holds the JSON exchange format for intermodal routes developed by the business unit Dynamic Transportation Systems of the [AIT Austrian Institute of Technology](http://dts.ait.ac.at). It is the default format used by the AIT routing framework Ariadne.

It specifies the format of a routing request as well as the format of the returned route(s). A route does not only consist of the geometry but also optional information such as detailed specification of the used mode of transports or navigation instructions.
Geometries are represented in the [GeoJSON](http://geojson.org) format.

The route format is defined through the Java classes in the package `at.ac.ait.ariadne.routeformat`.
Reading and writing of a route and exporting the JSON schema with Jackson is demonstrated in `JacksonExample.java`.
A simple example how to display a route in a browser with Leaflet is shown in `src/main/resources/ariadne-json-route-format_example_leaflet.html`.

## Coding Style
- implicit public constructor without arguments (used by jackson for deserialization)
- static `createMinimal()` methods as shortcuts for building minimal (or typically used) instances
- all members are private and mutable
    - initialization of complex types when they are defined and so that they are mutable (i.e. `new HashMap<>()` instead of `Collections.emptyMap()`)
- getter methods for all members (used by jackson for serialization)
- setter methods for all members (used by jackson for deserialization)
    - create defensive mutable copies of **external** complex data structures (e.g. the additionalInfo Map, but not of objects belonging to the route format, such as the GeoJSON classes)
    - return the object itself (so calls to setter methods can be chained similar to the builder pattern)
- for easy generic extension the map "additionalInfo" is provided for many classes
- `validate()` method for checking if the state of the instance is legal, which throws an `IllegalArgumentException` including a description of what is invalid (in-depth-checking: classes should call validate on all instances they contain, e.g. a navigation instruction calls `validate()` of the landmarks it contains)
- optional members
    - use `java.util.Optional<T>` as member (not Java serializable, but this is not a requirement for now)
    - just use `T` as argument for the setter and set the value with `Optional.ofNullable(t)`, so that unsetting of a member is possible by simply passing `null`
- `java.time.ZonedDateTime` is used to represent time stamps
- meaningful implementation of `toString()`
- override `hashCode()` and `equals()` where necessary (for now this is the case for classes related to modes of transport because we needed to compare them for validation purposes)
- documentation for individual fields (if present) is always located at the public getters (not the setters)

## Funding
Initial development was done together with [Fluidtime](http://www.fluidtime.com) within the research project 'sproute', which was funded by the Vienna Business Agency (Call From Science to Products 2013).

## License
Everything in this repository is licensed under CC0.

## Possible future features & improvements 
- display forbidden areas in leaflet-example
- public transport station details: differentiate between station entries & platforms, add travel time to transfer segments e.g. 5 min walk, 1 min escalators down, 2 min walk, 1 min elevator up;
- reevaluate handling of waiting times (in transfer segments? in pt segments?)

### Currently we are working on
- consistent mutability & "light builders"
- meaningful toString-methods for all classes
- v4 schema generation
