package threadTest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//A B C 交替打印 ，一共十轮
class ShareResource2{
    private int num = 1 ; //A-1 ,B-2,C-3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    public void print(int ThreadNum){
        lock.lock();
        try{
            if(ThreadNum == 1){
                //1 判断
                while(num != 1){
                    c1.await();
                }
                //2 干活
                for(int i = 1 ; i <= 5 ;i++){
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //3 通知
                num = 2;
                c2.signal();
            }else if(ThreadNum == 2){
                //1 判断
                while(num != 2){
                    c2.await();
                }
                //2 干活
                for(int i = 1 ; i <= 10 ;i++){
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //3 通知
                num = 3;
                c3.signal();
            }else{
                //1 判断
                while(num != 3){
                    c3.await();
                }
                //2 干活
                for(int i = 1 ; i <= 15 ;i++){
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //3 通知
                num = 1;
                c1.signal();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
public class CircularPrinting {
    public static void main(String[] args) {
        ShareResource2 shareResource = new ShareResource2();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(1);
            }
        },"A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(2);
            }
        },"B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print(3);
            }
        },"C").start();
    }
}