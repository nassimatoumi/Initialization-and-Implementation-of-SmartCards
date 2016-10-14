package caisse;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class getDate {
	/*permet de récupérer la date actuelle
	 * */
	
	String dateS;
	Calendar cal;
	DateFormat dateFormat;
	int month,day,year,hour,min;
	
	public getDate()
	{
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		month = cal.get(Calendar.MONTH)+1;
		day=cal.get(Calendar.DATE);
		year=cal.get(Calendar.YEAR)-2000;
		hour=cal.get(Calendar.HOUR_OF_DAY);
		min=cal.get(Calendar.MINUTE);
		
	}
	
	public byte sendDay()
	{
		return (byte)day;
	}
	
	public byte sendMonth()
	{
		return (byte)month;
	}
	
	public byte sendYear()
	{
		return (byte)year;
	}
	
	public byte sendHour()
	{
		return (byte)hour;
	}
	
	public byte sendMinute()
	{
		return (byte)min;
	}

}
