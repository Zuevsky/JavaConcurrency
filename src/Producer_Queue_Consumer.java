/*
Программа бесконечно считывает целые числа, которые пользователь вводит в Standard Input.
При вводе -1 программа завершает свою работу.
Числа, вводимые пользователем, представляют собой количество секунд, на которое должен заснуть thread-worker.
После чего thread-worker выводит в File: ${timestamp} - Я засыпаю на ${seconds} секунд(ы).
Все задачи, thread-worker выполняет в том порядке, в котором они были введены пользователем.
Когда у thread-worker нет текущих задач, то он отсчитывает 3 секунды, ожидая команды, и в противном случае засыпает на 5 секунд.
 */

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.time.temporal.ChronoField;
import java.time.LocalTime;
import java.io.FileOutputStream;

public class Producer_Queue_Consumer {
    public static void main(String[] args) {
        Queue queue = new Queue();
        IsWorking isWorking = new IsWorking();
        Producer producer = new Producer("Producer", queue, isWorking);
        Consumer consumer = new Consumer("Consumer", queue, isWorking);
        producer.start();
        consumer.start();
    }

    static class Queue {
        ArrayDeque<Integer> inputStack = new ArrayDeque<>();
    }

    static class IsWorking {
        public boolean yesOrNo;
    }

    static class Producer extends Thread {
        Queue queue;
        Scanner scan = new Scanner(System.in);
        String input;
        int num;
        IsWorking isWorking;

        public Producer(String name, Queue queue, IsWorking isWorking) {
            super(name);
            this.queue = queue;
            this.isWorking = isWorking;
        }

        public void run() {
            isWorking.yesOrNo = true;
            while(isWorking.yesOrNo) {
                input = scan.next();
                try {
                    num = Integer.parseInt(input);
                    if(num != -1) {
                        queue.inputStack.addFirst(num);
                    } else {
                        queue.inputStack.addFirst(num);
                        isWorking.yesOrNo = false;
                    }
                } catch(NumberFormatException nfe) {
                    System.out.println("Введены неверные данные, требуется целое число!");
                }
            }
        }
    }

    static class Consumer extends Thread {
        Queue queue;
        IsWorking isWorking;

        public Consumer(String name, Queue queue, IsWorking isWorking) {
            super(name);
            this.queue = queue;
            this.isWorking = isWorking;
        }

        public void run() {
            try {
                sleep(3000);
            } catch(InterruptedException ie) {
                System.out.println("Поток был прерван!");
            }
            while(isWorking.yesOrNo) {
                if(queue.inputStack.size() > 0) {
                    int num = queue.inputStack.pollLast();
                    System.out.println("Я засыпаю на " + num + " секунд(ы).");
                    System.out.println();
                    logger(num);
                    try {
                        sleep(num*1000);
                        System.out.println("Я проснулся!");
                        System.out.println();
                    } catch(InterruptedException ie) {
                        System.out.println("Поток был прерван!");
                    }
                } else {
                    System.out.println("Текущих задач нет, начинаю отсчет!");
                    System.out.println();
                    try {
                        sleep(1500);
                        if (queue.inputStack.size() > 0) {
                            continue;
                        } else {
                            System.out.println("3");
                            System.out.println();
                            sleep(1500);
                        }
                        if (queue.inputStack.size() > 0) {
                            continue;
                        } else {
                            System.out.println("2");
                            System.out.println();
                            sleep(1500);
                        }
                        if (queue.inputStack.size() > 0) {
                            continue;
                        } else {
                            System.out.println("1");
                            System.out.println();
                            sleep(1500);
                        }
                        if (queue.inputStack.size() > 0) {
                            continue;
                        } else {
                            System.out.println("Текущих задач нет, я засыпаю на 5 секунд!");
                            System.out.println();
                            logger(5);
                            sleep(5000);
                            System.out.println("Я проснулся!");
                            System.out.println();
                        }
                    } catch (InterruptedException ie) {
                        System.out.println("Поток был прерван!");
                    }
                }
            }
            System.out.println("Программа принудительно завершена!");
        }


        public void logger(int num) {
            LocalTime now = LocalTime.now();
            try {
                FileOutputStream fos = new FileOutputStream("resources/Producer_Queue_Consumer.txt", true);
                String log = now.get(ChronoField.HOUR_OF_DAY) + ":" + now.get(ChronoField.MINUTE_OF_HOUR) + ":" +
                        now.get(ChronoField.SECOND_OF_MINUTE) + " - Я засыпаю на " + num + " секунд(ы).\n";
                byte[] buffer = log.getBytes();
                fos.write(buffer, 0, buffer.length);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
