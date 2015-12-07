# sproute-json-route-format
This repository holds the JSON exchange format for intermodal routes developed by the [AIT Austrian Institute of Technology](http://dts.ait.ac.at) and [Fluidtime](http://www.fluidtime.com).

It specifies the format of a routing request as well as the format of the returned routes. The routes contain geometries and important meta information that can e.g. be used to properly display a route in a smart phone app or on a website.
Geometries are stored in GeoJSON format.

The format is defined through the Java classes in the package `at.ac.ait.sproute.routeformat`.
Reading and writing of a route and exporting the JSON schema with Jackson is demonstrated in `JacksonExample.java`.
A simple example how to display a route in a browser with Leaflet is shown in `routeformat-usage-in-leaflet.html`.

## Coding Style
- all member variables are private final with public getters
- documentation for individual fields (if present) is always located at the public getters (not in the builder)
- builder pattern is used to construct instances
- builders allow unsetting fields through null values
- builders check if mandatory arguments are provided and throw an IllegalArgumentException in case of missing / invalid arguments
- for easy generic extension the map "additionalInfo" is provided for many classes

## License
Everything in this repository is licensed under CC0.
