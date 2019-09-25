package mailbox;

import java.util.*;

public class Folder {
	private ArrayList<Email> folder = new ArrayList<Email>();
	private String name;
	private String currentSortingMethod;

	public Folder() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addEmail(Email email) {
		this.folder.add(email);
	}

	public Email removeEmail(int index) {
		this.folder.remove(index - 1);
		return folder.get(index - 1);
	}

	public void sortBySubjectAscending() {

		this.currentSortingMethod = "Subject ascending";
	}

	public void sortBySubjectDescending() {

		this.currentSortingMethod = "Subject desending";
	}

	public void sortByDateAscending() {

		this.currentSortingMethod = "Date ascending";
	}

	public void sortByDateDescending() {

		this.currentSortingMethod = "Date desceding";
	}

	public void displayEmails() {
		System.out.println(this.name + "/n");
		System.out.printf("%8s \t %20s \t %8s", "Index |", "Time", "| Subject");
		for (int i = 0; i<folder.size();i++){
		System.out.printf("%8s \t %20s \t %8s", i+"|" , folder.get(i).getTimestamp().HOUR+":"+ folder.get(i).getTimestamp().MINUTE+" "+folder.get(i).getTimestamp().DATE+"/"+folder.get(i).getTimestamp().MONDAY, "| Subject");
		}
	}
}
