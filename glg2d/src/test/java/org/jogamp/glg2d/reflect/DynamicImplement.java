package org.jogamp.glg2d.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

public final class DynamicImplement implements InvocationHandler {
  private Object wrapped;

  private DynamicImplement(Object r) {
    wrapped = r;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) {
    Method proxiedMethod;

    Class<?> wrappedClass = wrapped.getClass();
    try {
      proxiedMethod = wrappedClass.getMethod(method.getName(), method.getParameterTypes());
    } catch (NoSuchMethodException ignore) {
      ArrayList<Method> methods = new ArrayList<>();
      for (Method m : wrappedClass.getMethods()) {
        if (m.getName().equals(method.getName()) && m.getParameterCount() == method.getParameterCount()) {
          Class<?>[] a = m.getParameterTypes();
          Class<?>[] b = method.getParameterTypes();

          boolean proper = true;
          for (int i = 0, n = a.length; i < n; i++) {
            Class<?> x = a[i];
            Class<?> y = b[i];

            if (!x.isAssignableFrom(y)) {
              proper = false;
              break;
            }
          }

          if (proper) {
            methods.add(m);
          }
        }
      }

      if (methods.isEmpty()) {
        throw new RuntimeException("No method found for " + method.getName());
      }

      if (methods.size() > 1) {
        throw new RuntimeException("Method overload not supported yet for" + method.getName()); // todo select best
      }

      proxiedMethod = methods.get(0);
    }

    try {
      return proxiedMethod.invoke(wrapped, args);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object makeImplement(Class<?> superClass, Object toWrap) {
    return makeImplement(new Class[]{superClass}, toWrap);
  }

  public static Object makeImplement(Class<?>[] interfaces, Object toWrap) {
    return Proxy.newProxyInstance(interfaces[0].getClassLoader(),
        interfaces,
        new DynamicImplement(toWrap));
  }
}
