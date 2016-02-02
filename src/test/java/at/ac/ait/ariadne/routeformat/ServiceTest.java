package at.ac.ait.ariadne.routeformat;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {

	@Test
	public void testColor() {
		Service.builder().withName("28A").withColor("#112233").build();

		try {
			Service.builder().withName("28A").withColor("#112").build();
			Assert.fail("must throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}

		try {
			Service.builder().withName("28A").withColor("#ABCDEG").build();
			Assert.fail("must throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

}
