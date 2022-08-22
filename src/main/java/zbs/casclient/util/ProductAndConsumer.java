package zbs.casclient.util;

import java.util.concurrent.TimeUnit;

/**
 * 生产者-消费者
 * @author zbs
 * @since 2022/8/2 18:29
 */
public class ProductAndConsumer {
    private static int product = 0;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            for (int i=0;i<100;i++){
                try {
                    product();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "product").start();

        new Thread(() -> {
            for (int i=0;i<50;i++){
                try {
                    consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumer").start();

        new Thread(() -> {
            for (int i=0;i<50;i++){
                try {
                    consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumer2").start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("finally = " + product);
    }
    
    public synchronized static void product() throws InterruptedException {
        while (product >= 2){
            //商品充足时，等待消费
            ProductAndConsumer.class.wait();
        }
        product++;
        System.out.println(Thread.currentThread().getName() + " 生产=> " + product);
        ProductAndConsumer.class.notifyAll();
    }

    public synchronized static void consumer() throws InterruptedException {
        while (product == 0){
            //商品=0时，等待生产
            ProductAndConsumer.class.wait();
        }
        product--;
        System.out.println(Thread.currentThread().getName() + " 消费=> " + product);
        ProductAndConsumer.class.notifyAll();
    }
}
