package exception;


public class InvalidCurrentLocationException extends Exception {
	public InvalidCurrentLocationException(){}

	public InvalidCurrentLocationException(String message){
		super(message);
	}
}