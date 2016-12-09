package at.ac.ait.ariadne.routeformat.example;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JacksonExampleTest {

    /**
     * Generating a .json for the example route and parsing it again should
     * complete without {@link Exception}s
     */
    @Test
    public void testJacksonGeneration() throws JsonGenerationException, JsonMappingException, IOException {
        JacksonExample.main(new String[0]);
    }

}
