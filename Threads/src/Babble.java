
import java.io.*;

/**
 * Threads Demo -- E. Stark, 4/29/2002
 */
class Babble {

    private static final double MAX_DELAY = 10000;
    private BufferedReader in
            = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args)
            throws IOException, InterruptedException {
        new Babble().go();
    }

    void go() throws IOException {
        while (true) {
            String what = in.readLine();
            Thread t = new Thread(new Talker(what, MAX_DELAY), what);
            t.start();
        }
    }

    class Talker implements Runnable {

        String saying;
        double maxDelay;

        Talker(String saying, double maxDelay) {
            this.saying = saying;
            this.maxDelay = maxDelay;
        }

        public void run() {
            while (!Thread.interrupted()) {
                System.out.println(saying);
                long when = System.currentTimeMillis();
                when += (long) (Math.random() * maxDelay);
                while (System.currentTimeMillis() < when) {
                    Thread.yield();
                }
            }
        }
    }
}
