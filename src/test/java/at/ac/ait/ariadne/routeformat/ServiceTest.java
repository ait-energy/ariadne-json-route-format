package at.ac.ait.ariadne.routeformat;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceTest {

    @Test
    public void toStringTest() {
        Service service = Service.createMinimal(null);
        Assert.assertEquals("null", service.toString());
    }

    @Test
    public void serializationTest() throws IOException {
        Service x = Service.createMinimal("");

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        String serialized = mapper.writeValueAsString(x);
        Service deserialized = mapper.readValue(serialized, Service.class);
        Assert.assertTrue(deserialized.getName().isEmpty());
    }

}
