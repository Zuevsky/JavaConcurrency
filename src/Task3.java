public class Task3 { // Т.к. интерфейс Runnable по факту является функциональным, то объект интерфейса мы можем представить в виде лямбда-выражения
    public static void main(String[] args) {
        System.out.println("Main thread started...");
        Runnable r = () -> {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread has been interrupted");
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
        };
        Thread myThread = new Thread(r, "MyThread");
        myThread.start();
        System.out.println("Main thread finished...");
    }
}
