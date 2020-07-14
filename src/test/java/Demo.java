import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author psl
 * @date 2020/7/4
 */
public class Demo implements MethodInterceptor {



    public static void main(String[] args) {
//        System.out.println(String.valueOf((char)29));
        Demo1 proxy = new Demo().getProxy(Demo1.class);
        proxy.say();
        proxy.say1();
    }
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cls){
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("after");
        return result;
    }
}
