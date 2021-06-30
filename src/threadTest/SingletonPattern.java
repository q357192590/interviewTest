package threadTest;

//饿汉模式
class Hungry{
    private static Hungry instance = new Hungry();
    private Hungry(){};
    public static Hungry getInstance(){
        return instance;
    }
}
//懒汉
class Lazy{
    private static Lazy instance;
    private Lazy(){};
    public static Lazy getInstance(){
        if(instance == null){
            instance = new Lazy();
        }
        return instance;
    }
}
//DCL
class DoubleCheckedLocking{
    private volatile static DoubleCheckedLocking instance;
    private DoubleCheckedLocking(){};
    public static DoubleCheckedLocking getInstance(){
        if(instance == null){
            synchronized(DoubleCheckedLocking.class){
                if(instance == null){
                    instance = new DoubleCheckedLocking();
                }
            }
        }
        return instance;
    }
}
//静态内部类
class StaticInnerClass{
    private static class InnerClass{
        private static final StaticInnerClass INSTANCE = new StaticInnerClass();
    }
    private StaticInnerClass(){};
    public static StaticInnerClass getInstance(){
        return InnerClass.INSTANCE;
    }
}
//枚举
class EnumSingleton{
    public enum Singleton{
        INSTANCE;
    }
}

public class SingletonPattern {
    public static void main(String[] args) {

    }
}
