package mailbox;

import java.util.Comparator;

public class TimeDecending implements Comparator {
	public int compare(Object o1, Object o2) {
		Email e1 = (Email) o1;
		Email e2 = (Email) o2;
		return (e2.getTimestamp().compareTo(e1.getTimestamp()));
	}
}
