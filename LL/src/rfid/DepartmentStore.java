/*Your name
Your SOLAR ID#
Your Email Address
The programming assignment number
The course (CSE214), and
Your recitation number and TAs Name. (See SECTIONS for your TA's name.)
*/
package rfid;

import java.util.*;
import java.io.*;

public class DepartmentStore {
	public static void main(String args[]) throws IOException {
		System.out.println("     Welcome!");
		System.out.println(" ");
		String option;
		do {
			System.out.println(" ");
			System.out.println("     C - Clean store ");
			System.out.println("     I - Insert an item into the list ");
			System.out.println("     L - List by location ");
			System.out.println("     M - Move an item in the store ");
			System.out.println("     O - Checkout  ");
			System.out.println("     P - Print all items in store");
			System.out.println("     R - Print by RFID tag number");
			System.out.println("     U - Update inventory system");
			System.out.println("     Q - Exit the program.");
			System.out.println("");
			System.out.println("");

			ItemList il = new ItemList();
			
			System.out.print("Please select an option: ");
			Scanner sc = new Scanner(System.in);
			option = sc.nextLine().toUpperCase();
			String name;
			String rfidTag;
			String initPosition;
			String newPosition;
			double price;

			switch (option) {
			case "C":
				il.cleanStore();
				break;

			case "I":
				System.out.print("Enter the name: ");
				sc = new Scanner(System.in);
				name = sc.nextLine();

				System.out.print("Enter the RFID: ");
				sc = new Scanner(System.in);
				rfidTag = sc.nextLine();

				System.out.print("Enter the original location: ");
				sc = new Scanner(System.in);
				initPosition = sc.nextLine();

				System.out.print("Enter the price: ");
				sc = new Scanner(System.in);
				price = sc.nextDouble();
				break;

			case "L":
				System.out.print("Enter the location: ");
				sc = new Scanner(System.in);
				newPosition = sc.nextLine();
				il.printByLocation(newPosition);
				break;

			case "M":
				System.out.print("Enter the RFID: ");
				sc = new Scanner(System.in);
				rfidTag = sc.nextLine();

				System.out.print("Enter the original location: ");
				sc = new Scanner(System.in);
				initPosition = sc.nextLine();

				System.out.print("Enter the new lovation: ");
				sc = new Scanner(System.in);
				newPosition = sc.nextLine();
				
				il.moveItem(rfidTag, initPosition, newPosition);
				break;

			case "O":
				System.out.print("Enter cart number: ");
				sc = new Scanner(System.in);
				newPosition = sc.nextLine();
				il.checkOut(newPosition);
				break;
				
			case "P":
				il.printAll();
				break;
				
			case "R":
				System.out.print("Enter the RFID: ");
				sc = new Scanner(System.in);
				rfidTag = sc.nextLine();
				il.printByRFID(rfidTag);
				break;

			case "U":
				il.removeAllPuechased();
				break;
			}
		} while (!option.equals("Q"));
		System.out.println("Goodbye!");
	}
}
