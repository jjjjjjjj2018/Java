package elevator;

import java.util.*;
import java.lang.*;

public class RequestQueue extends Vector {

	Vector vec = new Vector();

	public RequestQueue() {
		
	}

	public void enqueue(Request rq) throws Exception {
		if (vec.isEmpty() == true)
			throw new EmptyQueueException("Empty Queue");
		vec.add(rq);
	}

	public void dequeue() throws Exception {
		if (vec.isEmpty() == true)
			throw new EmptyQueueException("Empty Queue");
		vec.remove(vec.firstElement());
	}
}
