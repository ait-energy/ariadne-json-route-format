# ariadne-json-route-format
This repository holds the JSON exchange format for intermodal routes developed by the business unit Dynamic Transportation Systems of the [AIT Austrian Institute of Technology](http://dts.ait.ac.at). It is the default format used by the AIT routing framework Ariadne.

It specifies the format of a routing request as well as the format of the returned routes. The routes contain geometries and important meta information that can e.g. be used to properly display a route in a smart phone app or on a website.
Geometries are represented in the [GeoJSON](http://geojson.org) format.

The route format is defined through the Java classes in the package `at.ac.ait.ariadne.routeformat`.
Reading and writing of a route and exporting the JSON schema with Jackson is demonstrated in `JacksonExample.java`.
A simple example how to display a route in a browser with Leaflet is shown in `src/main/resources/ariadne-json-route-format_example_leaflet.html`.

## Coding Style
- all member variables are private final with public getters
- documentation for individual fields (if present) is always located at the public getters (not in the builder)
- java.time.ZonedDateTime is used to represent time stamps
- builder pattern is used to construct instances
- builders allow unsetting fields through null values
- builders check if mandatory arguments are provided and throw an IllegalArgumentException in case of missing / invalid arguments
- for easy generic extension the map "additionalInfo" is provided for many classes

## Funding
Initial development was done together with [Fluidtime](http://www.fluidtime.com) within the research project 'sproute', which was funded by the Vienna Business Agency (Call From Science to Products 2013).

## License
Everything in this repository is licensed under CC0.

## Possible future features & improvements 
- RouteSegment: fix misleading naming of getDepartureTime/ArrivalTime (actually start/end of segment due to boarding/alighting)
- RouteSegment: remove durationSeconds, add travelingSeconds (and add getter for duration which sums boarding + traveling + alighting) 
- public transport station details: differentiate between station entries & platforms, add travel time to transfer segments e.g. 5 min walk, 1 min escalators down, 2 min walk, 1 min elevator up;
- reevaluate handling of waiting times (in transfer segments? in pt segments?)
- add meaningful toString-methods for all classes