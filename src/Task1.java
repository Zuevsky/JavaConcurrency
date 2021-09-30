public class Task1 { // Ожидание и очередность завершения потоков
    public static void main(String[] args) {
        System.out.println("Main thread started...");
        JThread t = new JThread("JThread ");
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted", t.getName());
        }
        System.out.println("Main thread finished...");
    }

    static class JThread extends Thread {
        JThread(String name) {
            super(name);
        }

        public void run() {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread has been interrupted");
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
        }
    }
}