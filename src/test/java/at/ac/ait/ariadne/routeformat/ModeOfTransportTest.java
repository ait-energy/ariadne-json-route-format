package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;

public class ModeOfTransportTest {

    @Test
    public void equalsTest() {
        Assert.assertTrue(ModeOfTransport.STANDARD_FOOT.equals(ModeOfTransport.STANDARD_FOOT));

        ModeOfTransport mot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.FOOT).setId("foot")
                .setColor("#377eb8");
        Assert.assertTrue(ModeOfTransport.STANDARD_FOOT.equals(mot));

        mot.setAdditionalInfo(ImmutableMap.of("key", (Object)"value"));
        Assert.assertFalse(ModeOfTransport.STANDARD_FOOT.equals(mot));
        Assert.assertEquals("value", mot.getAdditionalInfo().get("key"));
    }

    @Test
    public void equalsTestComplex() {
        ModeOfTransport a = ModeOfTransport.createMinimal(DetailedModeOfTransportType.BUS).setOperator(createOperator())
                .setService(createService());
        ModeOfTransport b = ModeOfTransport.createMinimal(DetailedModeOfTransportType.BUS).setOperator(createOperator())
                .setService(createService());
        Assert.assertTrue(a.equals(b));
    }

    private Service createService() {
        return Service.createMinimal("29A").setTowards("Floridsdorf");
    }

    private Operator createOperator() {
        return Operator.createMinimal("Wiener Linien").setId("WL");
    }

    @Test
    public void testColor() {
        ModeOfTransport mot = ModeOfTransport.createMinimal(DetailedModeOfTransportType.FOOT).setColor("#112233");
        mot.validate();

        try {
            mot.setColor("#112");
            mot.validate();
            Assert.fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            mot.setColor("#ABCDEG");
            mot.validate();
            Assert.fail("must throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

}
