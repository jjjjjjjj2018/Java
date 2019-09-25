package mailbox;

import java.util.Comparator;

public class SubjectAscending implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Email e1 = (Email) o1;
		Email e2 = (Email) o2;
		return (e1.getSubject().compareTo(e2.getSubject()));
	}

}
