

public class NamedThread extends Thread implements Runnable {
    
    private final String myName;
    
    public NamedThread(String name) {
        super(name);
        myName = name;
    }
    
    public static void main(String[] args) {
        NamedThread alice = new NamedThread("Alice");
        NamedThread bob = new NamedThread("Bob");
        alice.start();
        new Thread(bob).start();
    }
    
    public void run() {
        System.out.println("My name is " + myName + ".  " +
                           "I am thread " + Thread.currentThread());
    }
    
}
