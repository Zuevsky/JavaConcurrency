public class Task5 { // Прерывание потока
    public static void main(String[] args) {
        System.out.println("Main thread started...");
        JThread t = new JThread("JThread");
        t.start();
        try {
            System.out.println("Имя текущего потока: " + Thread.currentThread().getName());
            Thread.sleep(150);
            t.interrupt();
            Thread.sleep(150);
        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
        System.out.println("Main thread finished...");
    }

    static class JThread extends Thread {
        JThread(String name) {
            super(name);
        }

        public void run() {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            int counter = 1; // счетчик циклов
            while (!isInterrupted()) {
                System.out.println("Loop " + counter++);
                try{
                    sleep(50);
                } catch (InterruptedException e){
                    System.out.println("Thread has been interrupted");
                    break;
                }
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
        }
    }
}
