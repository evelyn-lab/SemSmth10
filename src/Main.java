import java.util.*;
class Producer implements Runnable {
    private final int limitedSize;
    private Queue<Double> queue;
    public Producer(Queue<Double> queue, int size) {
        this.queue = queue;
        this.limitedSize = size;
    }
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Produced: " + produce());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private double produce() throws InterruptedException {
        synchronized (queue) { // обязательно synchronized
            if (queue.size() == limitedSize) {
                queue.wait();
            }
            double newValue = Math.random();
            queue.add(newValue);
            queue.notifyAll();
            return newValue;
        }
    }

}
class Consumer implements Runnable {
    private Queue<Double> queue;
    public Consumer(Queue<Double> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Consumed: " + consume());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    private Double consume() throws InterruptedException {
        synchronized (queue) {
            if (queue.isEmpty()) {
                queue.wait();
            }
            queue.notifyAll();
            return queue.poll();
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Queue<Double> queue = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        Thread pThread = new Thread(new Producer(queue, size), "Producer");
        Thread cThread = new Thread(new Consumer(queue), "Consumer");
        pThread.start();
        cThread.start();
    }
}