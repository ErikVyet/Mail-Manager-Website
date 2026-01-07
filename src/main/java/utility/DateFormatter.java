package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	public static String toText(Date date) {
		try {
			return formatter.format(date);
		}
		catch(Exception exception) {
			return null;
		}
	}
	
	public static Date toDate(String string) {
		try {
			return formatter.parse(string);
		}
		catch(ParseException exception) {
			return null;
		}
	}
	
}
