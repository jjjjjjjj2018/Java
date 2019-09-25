package exception;


public class InvalidRFIDException extends Exception {
	public InvalidRFIDException(){}
	public InvalidRFIDException(String message){
		super(message);
	}
}
