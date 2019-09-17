
public class DeadlockDemo {

    private C obj1;
    private C obj2;

    public DeadlockDemo() {
        obj1 = new C(0);
        obj2 = new C(0);
        new Runner(1).start();
        new Runner(2).start();
    }

    public class C {

        private int value;

        public C(int v) {
            value = v;
        }

        public synchronized int getValue() {
            return value;
        }

        public synchronized void addValue(C other) {
            value += other.getValue();
        }
    }

    public class Runner extends Thread {

        private int i;

        public Runner(int i) {
            super("Runner-" + i);
            this.i = i;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("Runner " + i + " is alive");
                if (i == 1) {
                    obj2.addValue(obj1);
                } else if (i == 2) {
                    obj1.addValue(obj2);
                }
            }
        }
    }

    public static void main(String[] args) {
        new DeadlockDemo();
    }
}
