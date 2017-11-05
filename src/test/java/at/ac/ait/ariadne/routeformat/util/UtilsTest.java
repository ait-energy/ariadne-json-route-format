package at.ac.ait.ariadne.routeformat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;


public class UtilsTest {
    
    @Test
    public void outputDateFormatTest() {
        GregorianCalendar calendar = new GregorianCalendar(2017, 11, 31, 23, 0);
        String dateString = Utils.getDateTimeString(calendar.getTime());
        // time zone will depend on where test is run
        // Assert.assertEquals("2017-12-31T23:00:00+01:00", Utils.getDateTimeString(calendar.getTime()));
        
        // so only check length & first part of date
        Assert.assertTrue(dateString.startsWith("2017-12-31T23:00:00"));
        Assert.assertEquals(25, dateString.length());
    }
    
    @Test
    public void parseZonedDateTimeTest() {
        String stringWithCity = "2007-12-03T10:15:30+01:00[Europe/Paris]";
        String stringWithoutCity = "2007-12-03T10:15:30+01:00";
        
        Date withCity = Utils.parseDateTime(stringWithCity, "x");
        Date withoutCity = Utils.parseDateTime(stringWithoutCity, "y");
        
        Assert.assertEquals(withCity.getTime(), withoutCity.getTime());
        Assert.assertEquals(stringWithoutCity, new SimpleDateFormat(Utils.DATE_TIME_FORMAT).format(withoutCity));
        Assert.assertEquals("city is not kept", stringWithoutCity, new SimpleDateFormat(Utils.DATE_TIME_FORMAT).format(withCity));
    }

}
