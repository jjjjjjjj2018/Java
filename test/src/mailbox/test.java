package mailbox;

import java.util.*;

public class test {
	public static void main(String arg[]) {
		ArrayList<String> test = new ArrayList<String>();
		test.add("bob");
		test.add("asdasf");
		test.add("dgdfgs");
		test.add("fgldfk");
		test.sort(String::compareToIgnoreCase);
		System.out.println(test);
		System.out.printf("%s \t %s \t %s", "Index |", "Time", "| Subject");
		GregorianCalendar timestamp = null;
		timestamp.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR, Calendar.MINUTE);
	}
}
