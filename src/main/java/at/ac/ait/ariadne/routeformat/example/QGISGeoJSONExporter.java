package at.ac.ait.ariadne.routeformat.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import at.ac.ait.ariadne.routeformat.Route;
import at.ac.ait.ariadne.routeformat.RouteSegment;
import at.ac.ait.ariadne.routeformat.Service;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeature;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONFeatureCollection;
import at.ac.ait.ariadne.routeformat.geojson.GeoJSONLineString;

public class QGISGeoJSONExporter {

    private final Route route;

    public QGISGeoJSONExporter(Route route) {
        this.route = route;
    }

    public void writeLines(Path outFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        GeoJSONFeatureCollection<GeoJSONLineString> collection = new GeoJSONFeatureCollection<>();
        for (RouteSegment segment : route.getSegments()) {
            if (segment.getGeometryGeoJson().isPresent()) {
                GeoJSONFeature<GeoJSONLineString> feature = segment.getGeometryGeoJson().get();
                feature.getProperties().put("generalizedMot", segment.getModeOfTransport().getGeneralizedType().name());
                if (segment.getModeOfTransport().getDetailedType().isPresent())
                    feature.getProperties().put("detailedMot",
                            segment.getModeOfTransport().getDetailedType().get().name());
                if (segment.getModeOfTransport().getService().isPresent()) {
                    Service service = segment.getModeOfTransport().getService().get();
                    feature.getProperties().put("line", service.getName());
                    feature.getProperties().put("towards", service.getTowards().orElse(""));
                }
                collection.getFeatures().add(feature);
            }
        }

        String geoJson = mapper.writeValueAsString(collection);
        System.out.println(geoJson);
        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(geoJson);
        }
    }

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
        QGISGeoJSONExporter qgisGeoJSONExporter = new QGISGeoJSONExporter(
                new IntermodalRouteExample().getRouteFormatRoot().getRoutes().get(0));
        qgisGeoJSONExporter.writeLines(Paths.get("/tmp/oida.geojson"));
    }

}
