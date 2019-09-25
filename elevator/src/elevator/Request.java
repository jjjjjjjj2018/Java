package elevator;

import java.util.*;

public class Request {

	int sf = 0;
	int df = 0;
	int time = 0;

	public Request(int floors) {
		Random r = new Random();

		int sf = r.nextInt(floors) + 1;
		this.sf = sf;

		int df = r.nextInt(floors) + 1;
		while (df == sf) {
			df = r.nextInt(floors) + 1;
		}
		this.df = df;
		time++;

	}

	public int getTimeEntered() {
		return this.time;

	}

	public void setTimeEntered(int time) {
		this.time = time;
	}

	public int getSourceFloor() {
		return this.sf;
	}

	public void setSourceFloor(int sf) {
		this.sf = sf;

	}

	public int getDestinationFloor() {
		return this.df;
	}

	public void setDestinationFloor(int df) {
		this.df = df;

	}

}
