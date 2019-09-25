package mailbox;

import java.util.*;

import java.util.*;

public class Mailbox {
	private Folder inbox;
	private Folder trash;
	private ArrayList<Folder> mailbox;

	public void addFolder(Folder folder) {
		for (int i = 0; i < mailbox.size(); i++) {
			if (folder.getName().equals(mailbox.get(i).getName()))
				return;
		}
		mailbox.add(folder);
		return;
	}

	public void deleteFolder(String name) {
		for (int i = 0; i < mailbox.size(); i++) {
			if (mailbox.get(i).getName().equals(name)) {
				mailbox.remove(i);
				return;
			}
		}

	}

	public void composeEmail() {

		Email newemail = new Email(null, null, null, null, null, null);
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter recipient (To): ");
		String to = sc.nextLine();
		newemail.setTo(to);

		System.out.println("Enter carbon copy recipients (CC): ");
		String cc = sc.nextLine();
		newemail.setCc(cc);

		System.out.println("Enter blind carbon copy recipients (BCC): ");
		String bcc = sc.nextLine();
		newemail.setBcc(bcc);

		System.out.println("Enter subject line: ");
		String subject = sc.nextLine();
		newemail.setSubject(subject);

		System.out.println("Enter body: ");
		String body = sc.nextLine();
		newemail.setBody(body);

		newemail.setTimestamp();

		inbox.addEmail(newemail);

	}

	public void deleteEmail(Email email) {

	}

	public void clearTrash() {
		trash = new Folder();
	}

	public void moveEmail(Email email, String target) {
		if (email == null)
			return;
		else {
			for (int i = 0; i < mailbox.size(); i++) {
				if (mailbox.get(i).getName().equals(target)) {
					mailbox.get(i).removeEmail(i);
					return;
				}
			}
		}
	}

	public Folder getFolder(String name) {
		for (int i = 0; i < mailbox.size(); i++) {
			if (mailbox.get(i).getName().equals(name)) {
				return mailbox.get(i);
			}

		}
		return null;

	}
	public void displayMailbox(){
		System.out.println("Mailbox: ");
		System.out.println("--------");
		for (int i = 0; i<mailbox.size();i++){
			System.out.println(mailbox.get(i).getName());		
		}
		return;
	}
}
