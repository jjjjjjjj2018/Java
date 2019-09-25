package elevator;

import java.util.*;

public class Simulator {
	static int Total_Requests_Time = 0;
	static int Total_Wait_Time = 0;

	public static void simulate(double p, int Num_Of_Floors, int Num_Of_Elevators, int Total_Time) throws Exception {

		BooleanSource bs = new BooleanSource();
		RequestQueue rQ = new RequestQueue();
		Elevator ele = new Elevator(1, 0, null);

		for (int time = 1; time <= Total_Time; time++) {

			if (bs.requestArrived(p) == true) {

				Request r = new Request(Num_Of_Floors);
				rQ.enqueue(r);
				Total_Requests_Time++;
			}
			if (rQ.isEmpty() == false) {
				if (ele.getElevatorState() == 0) {
					ele.setRequest(rQ.firstElement());
					if (ele.getCurrentFloor() < ele.getRequest().getSourceFloor()) {
						ele.setCurrentFloor(ele.getCurrentFloor() + 1);
						ele.setElevatorState(1);
					}
					if (ele.getCurrentFloor() > ele.getRequest().getSourceFloor()) {
						ele.setCurrentFloor(ele.getCurrentFloor() - 1);
						ele.setElevatorState(1);
					}
					if (ele.getCurrentFloor() == ele.getRequest().getSourceFloor()) {
						ele.setElevatorState(2);
						rQ.dequeue();
					}

				}
				if (ele.getElevatorState() == 1) {
					if (ele.getCurrentFloor() < ele.getRequest().getSourceFloor()) 
						ele.setCurrentFloor(ele.getCurrentFloor() + 1);
					
					if (ele.getCurrentFloor() > ele.getRequest().getSourceFloor()) 
						ele.setCurrentFloor(ele.getCurrentFloor() - 1);
					
					if (ele.getCurrentFloor() == ele.getRequest().getSourceFloor()) {
						ele.setElevatorState(2);
						rQ.dequeue();
					}
				}
				if (ele.getElevatorState() == 2) {
					if (ele.getCurrentFloor() < ele.getRequest().getDestinationFloor())
						ele.setCurrentFloor(ele.getCurrentFloor() + 1);
					if (ele.getCurrentFloor() > ele.getRequest().getDestinationFloor())
						ele.setCurrentFloor(ele.getCurrentFloor() - 1);
					if (ele.getCurrentFloor() == ele.getRequest().getDestinationFloor()) {
						ele.setElevatorState(0);
					}

				}

				if (rQ.size() > 0)
					Total_Wait_Time++;
			}
		}
	}

	public int getTotalRequestTime() {
		return Total_Requests_Time;
	}

	public int getTotalWaitTime() {
		return Total_Wait_Time;
	}
}
