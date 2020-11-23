package Test;

public class TestExpr {
 
    //测试运行时实现抽象函数
    public static abstract class testabs
    {
        protected abstract void testmethod();
    }
    
    public static void main(String[] args)
    {
        var test=new testabs(){
            //可以运行时实现 感觉类似lambda赋值  
            @Override
            protected void testmethod() {
                Runnable lmbda=()->{System.out.println("test run time implement from a lambda");};
                lmbda.run();
            }
        };

        test.testmethod();
    }

}
