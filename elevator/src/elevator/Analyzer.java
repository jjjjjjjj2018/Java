package elevator;

import java.util.*;

public class Analyzer {
	public static void main(String args[]) throws Exception {
		double p;
		int fl;
		int ele;
		int time;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Welcome to the Elevator simulator!");
		System.out.println("");
		
		System.out.println("Please enter the probability of arrival for Requests: ");		
		p = sc.nextDouble();
		
		System.out.println("Please enter the number of floors: ");
		fl = sc.nextInt();
		
		System.out.println("Please enter the number of elevators: ");
		ele = sc.nextInt();
		
		System.out.println("Please enter the length of the simulation (in time units): ");
		time = sc.nextInt();
		
		if (p < 0 || p > 1 || fl < 0 || ele < 0 || time < 0)
			throw new Exception("Invalid Input");
		
		Simulator sm = new Simulator();
		sm.simulate(p, fl, ele, time);
		System.out.println("Total Wait TIme: " + sm.getTotalWaitTime());
		System.out.println("Total Request(s): " + sm.getTotalRequestTime());
		System.out.println("Average Wait Time: " + sm.getTotalWaitTime()/sm.getTotalRequestTime());
	}

}
