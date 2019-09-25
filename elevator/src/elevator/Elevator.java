package elevator;

public class Elevator {
	int cf;
	int  es = 0;
	Request rq = null;

	public Elevator(int cf, int es, Request rq) {
		cf = 1;
		this.cf=cf;
		es = 0;
		this.es=es;
		rq = null;
		this.rq=rq;
	}

	public void setCurrentFloor(int cf) {
		this.cf = cf;
	}

	public int getCurrentFloor() {
		return this.cf;
	}

	public void setElevatorState(int es) {
		this.es = es;
	}

	public int getElevatorState() {
		return this.es;
	}

	public void setRequest(Request rq) {
		this.rq = rq;
	}

	public Request getRequest() {
		return this.rq;
	}
}
