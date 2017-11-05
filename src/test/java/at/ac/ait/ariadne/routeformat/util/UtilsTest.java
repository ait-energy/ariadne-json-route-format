package at.ac.ait.ariadne.routeformat.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void outputDateFormatTest() {
        ZonedDateTime time = ZonedDateTime.of(2017, 12, 31, 23, 0, 0, 0, ZoneId.of("Europe/Vienna"));
        Assert.assertEquals("2017-12-31T23:00:00+01:00", Utils.getDateTimeString(time));
    }

}
