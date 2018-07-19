package global;

import properties.Properties;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 29/05/18 Time : 5:09 PM Project Name: ClientMS Class Name:
 * global.Global
 */
public class Global
{
	public final boolean production = true;
	public static final boolean prod = true;
	private static Locale locale = new Locale("en", "US");


    public static Locale getLocale()
	{
		return locale;
	}
	
	public static void setLocale(Locale locale)
	{
		Global.locale = locale;
	}
	
	public static String getVersion() throws IOException
	{
		Properties p = new Properties();
		return p.getProperty("version", Properties.PROPERTY_TYPE.env);
	}

    public int getCurrentTimeZoneHours() {
        String offset = getCurrentTimezoneOffset();
        int hours = Integer.parseInt(getCurrentTimezoneOffset().substring(1, 3));
        if (offset.charAt(0) == '+') hours *= -1;
        return hours;
    }

    public int getCurrentTimeZoneMinutes() {
        String offset = getCurrentTimezoneOffset();
        return Integer.parseInt(offset.substring(4, 6));
    }

    private String getCurrentTimezoneOffset() {

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
        offset = (offsetInMillis >= 0 ? "+" : "-") + offset;

        return offset;
    }
}
