package threadTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ShareResource1{
    private volatile boolean flag = true ; //默认生产
    private AtomicInteger atomicInteger = new AtomicInteger();
    BlockingQueue<String> blockingQueue = null;

    public ShareResource1(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }
    public void myProduct() throws Exception {
        String data = null;
        boolean returnValue;
        while(flag) {
            data = atomicInteger.incrementAndGet() + "";
            returnValue = blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if(returnValue) {
                System.out.println(Thread.currentThread().getName() + "插入队列成功：" + data);
            }else{
                System.out.println(Thread.currentThread().getName() + "插入队列失败：" + data);
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread. currentThread().getName() + "大老板叫停，flag = false，生产线程结束");
    }
    public void myConsume() throws Exception {
        String res = null;
        boolean consumeFlag = true;
        while(flag || consumeFlag){
            res = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if(null == res || res.equalsIgnoreCase("")){
                consumeFlag = false;
                System.out.println(Thread.currentThread().getName() + "超过两秒没有取到，消费退出");
                System.out.println();
                System.out.println();
                continue;
            }
            System.out.println(Thread.currentThread().getName() + "消费队列成功：" + res);
        }
    }
    public void stop() throws Exception{
        this.flag = false;
    }
}

public class ProducerAndConsumer {
    public static void main(String[] args) throws Exception{
        ShareResource1 shareResource = new ShareResource1( new ArrayBlockingQueue<>(3));
         new Thread(() -> {
             System.out.println(Thread.currentThread().getName() + "生产线程启动");
             try {
                 shareResource.myProduct();
             }catch (Exception e){
                 e.printStackTrace();
             }
         },"Prod").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "消费线程启动");
            try {
                shareResource.myConsume();
            }catch (Exception e){
                e.printStackTrace();
            }
        },"Cons").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("5s，大老板main线程叫停，活动结束");
        shareResource.stop();
    }
}
