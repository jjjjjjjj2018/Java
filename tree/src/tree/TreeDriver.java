package tree;

import java.io.*;
import java.util.*;

public class TreeDriver {
	public static void main(String arg[]) throws Exception {
		String choice;
		Tree tree = new Tree();
		do {
			Scanner sc = new Scanner(System.in);
			System.out.println(
					"L - Load a Tree. \nH - Begin a Help Session. \nT - Traverse the Tree in preorder. \nQ - Quit \n");
			System.out.print("Choice> ");
			choice = sc.nextLine().toUpperCase();
			System.out.println("");
			switch (choice) {
			case "L":
				System.out.print("Enter the file name> ");
				String file = sc.nextLine();
				Scanner input = new Scanner(new File(file));
				System.out.println("Tree created successfully!");
				break;
				
			case "H":
				System.out.println("Help Session Starting...");
				tree.beginSession();
				
			case "T":
				System.out.println("Traversing the tree in preorder: ");
				tree.preOrder();
				
			
			}
		} while (!choice.equals("Q"));
	}
}
