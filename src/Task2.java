public class Task2 { // Реализация нового потока через интерфейс Runnable
    public static void main(String[] args) {
        System.out.println("Main thread started...");
        Thread myThread = new Thread(new MyThread(), "MyThread");
        myThread.start();
        System.out.println("Main thread finished...");
    }

    static class MyThread implements Runnable {
        public void run() {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread has been interrupted");
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
        }
    }
}
