package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {

    @Test
    public void toStringTest() {
        Service service = Service.createMinimal(null);
        Assert.assertEquals("null", service.toString());
    }

}
