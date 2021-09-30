/*
Создано хранилище Storage, в котором сохраняются целые числа. У Storage есть два метода: добавляющий число, и убирающий число.
Созданы 2 типа потока: генерирующий число для добавления в Storage и удаляющий число из хранилища.
Созданы несколько экземпляров каждого вида потоков.
Работа методов добавляющих и удаляющих числа из хранилища - синхронизирована.
*/

import java.util.ArrayDeque;

public class Store {
    public static void main(String[] args) {
        Storage storage = new Storage();
        ProducerThread producer1 = new ProducerThread("Производитель 1", storage);
        ProducerThread producer2 = new ProducerThread("Производитель 2", storage);
        ProducerThread producer3 = new ProducerThread("Производитель 3", storage);
        ConsumerThread consumer1 = new ConsumerThread("Потребитель 1", storage);
        ConsumerThread consumer2 = new ConsumerThread("Потребитель 2", storage);
        ConsumerThread consumer3 = new ConsumerThread("Потребитель 3", storage);
        producer1.start();
        producer2.start();
        producer3.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
    }

    static class Storage {
        int capacity = 10;
        ArrayDeque<Integer> numbers = new ArrayDeque<>();
        boolean isWorking = true;

        public synchronized void produce() {
            while(numbers.size() < capacity) {
                int num = (int) (Math.random() * (Math.random() * 100));
                if(num > 90 & num < 100) {
                    isWorking = false;
                }
                numbers.addFirst(num);
                System.out.println(Thread.currentThread().getName() + " - В хранилище добавлено число: " + num);
                System.out.println("Всего в хранилище " + numbers.size() + " чисел");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ie) {
                    System.out.println("Поток наполнения хранилища прерван!");
                }
            }
        }

        public synchronized void consume() {
            while(numbers.size() > 0) {
                int num = numbers.pollLast();
                System.out.println(Thread.currentThread().getName() + " - Из хранилища удалено число: " + num);
                System.out.println("Всего в хранилище " + numbers.size() + " чисел");
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException ie) {
                    System.out.println("Поток опустошения хранилища прерван!");
                }
            }
        }
    }

    static class ProducerThread extends Thread {
        Storage storage;

        public ProducerThread(String name, Storage storage) {
            super(name);
            this.storage = storage;
        }

        public void run() {
            while(storage.isWorking) {
                storage.produce();
            }
        }
    }

    static class ConsumerThread extends Thread {
        Storage storage;

        public ConsumerThread(String name, Storage storage) {
            super(name);
            this.storage = storage;
        }

        public void run() {
            while(storage.isWorking) {
                storage.consume();
            }
        }
    }
}
