package mailbox;

import java.util.*;
import java.io.*;

public class MainboxOperation {

	public static void main(String[] args) {

		Mailbox mb = new Mailbox();
		try {
			FileInputStream fis = new FileInputStream("mybean.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			mb = (Mailbox) ois.readObject();
//not finished
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (mb == null) {
			System.out.println("Previous save not found, starting with an empty mailbox.");

		}
		String option = "";

		do {
			System.out.println("A – Add folder");
			System.out.println("R – Remove folder");
			System.out.println("C – Compose email");
			System.out.println("F – Open folder");
			System.out.println("I – Open Inbox");
			System.out.println("T – Open Trash");
			System.out.println("Q – Quit \n");

			System.out.println("Enter a user option: ");
			Scanner sc = new Scanner(System.in);
			option = sc.nextLine().toUpperCase();

			switch (option) {

			case "A":
				System.out.println("Enter folder name: ");
				String foldername = sc.nextLine();
				Folder newfolder = new Folder();
				newfolder.setName(foldername);
				mb.addFolder(newfolder);
				mb.displayMailbox();
				break;

			case "R":
				

			case "C":
				mb.composeEmail();
				System.out.println("Email successfully added to Inbox");
				mb.displayMailbox();

			case "F":

			case "I":

			case "T":

			}
		} while (option != "Q");

	}

}
