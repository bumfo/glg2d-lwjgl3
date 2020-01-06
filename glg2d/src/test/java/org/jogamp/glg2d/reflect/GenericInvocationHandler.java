package org.jogamp.glg2d.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class GenericInvocationHandler implements InvocationHandler {
  private Object wrapped;

  private GenericInvocationHandler(Object r) {
    wrapped = r;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) {
    Method proxiedMethod;
    try {
      proxiedMethod = wrapped.getClass().getMethod(method.getName(), method.getParameterTypes());

      return proxiedMethod.invoke(wrapped, args);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object makeImplement(Class<?> superClass, Object toWrap) {
    return Proxy.newProxyInstance(superClass.getClassLoader(),
        new Class[]{superClass},
        new GenericInvocationHandler(toWrap));
  }

}
