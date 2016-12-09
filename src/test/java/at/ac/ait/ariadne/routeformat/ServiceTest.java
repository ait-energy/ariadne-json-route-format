package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {

    @Test
    public void testColor() {
        Service service = Service.createMinimal("28A").setColor("#112233");
        service.validate();

        try {
            service.setColor("#112");
            service.validate();
            Assert.fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            service.setColor("#ABCDEG");
            service.validate();
            Assert.fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

}
