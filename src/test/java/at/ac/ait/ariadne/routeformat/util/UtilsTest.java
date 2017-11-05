package at.ac.ait.ariadne.routeformat.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


public class UtilsTest {
    
    @Test
    public void parseZonedDateTimeTest() {
        String stringWithCity = "2007-12-03T10:15:30+01:00[Europe/Paris]";
        String stringWithoutCity = "2007-12-03T10:15:30+01:00";
        
        Date withCity = Utils.parseZonedDateTime(stringWithCity, "x");
        Date withoutCity = Utils.parseZonedDateTime(stringWithoutCity, "y");
        
        Assert.assertEquals(withCity.getTime(), withoutCity.getTime());
        Assert.assertEquals(stringWithoutCity, new SimpleDateFormat(Utils.PARSE_FORMAT).format(withoutCity));
        Assert.assertEquals("city is not kept", stringWithoutCity, new SimpleDateFormat(Utils.PARSE_FORMAT).format(withCity));
    }

}
