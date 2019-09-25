package mailbox;

import java.util.*;

public class TimeAscending implements Comparator {

	public int compare(Object o1, Object o2) {
		Email e1 = (Email) o1;
		Email e2 = (Email) o2;
		return (e1.getTimestamp().compareTo(e2.getTimestamp()));
	}

}
