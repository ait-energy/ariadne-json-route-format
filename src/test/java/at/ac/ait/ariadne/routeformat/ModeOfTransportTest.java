package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import at.ac.ait.ariadne.routeformat.Constants.DetailedModeOfTransportType;
import at.ac.ait.ariadne.routeformat.ModeOfTransport.Builder;

public class ModeOfTransportTest {

	@Test
	public void equalsTest() {
		Assert.assertTrue(ModeOfTransport.STANDARD_FOOT.equals(ModeOfTransport.STANDARD_FOOT));

		Builder builder = ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.FOOT);
		Assert.assertTrue(ModeOfTransport.STANDARD_FOOT.equals(builder.build()));

		builder.withAdditionalInfo(ImmutableMap.of("key", "value"));
		Assert.assertFalse(ModeOfTransport.STANDARD_FOOT.equals(builder.build()));

		ModeOfTransport a = builder.withAdditionalInfo(ImmutableMap.of("key", "value")).build();
		ModeOfTransport b = builder.withAdditionalInfo(ImmutableMap.of("key", "value")).build();
		Assert.assertTrue("equal contents of additionalInfo", a.equals(b));
	}

	@Test
	public void equalsTestComplex() {
		Builder builder = ModeOfTransport.builder().withDetailedType(DetailedModeOfTransportType.BUS);

		ModeOfTransport a = builder.withOperator(createOperator()).withService(createService()).build();
		ModeOfTransport b = builder.withOperator(createOperator()).withService(createService()).build();

		Assert.assertTrue(a.equals(b));
	}

	private Service createService() {
		return Service.builder().withName("29A").withTowards("Floridsdorf").build();
	}

	private Operator createOperator() {
		return Operator.builder().withId("WL").withName("Wiener Linien").build();
	}

}
