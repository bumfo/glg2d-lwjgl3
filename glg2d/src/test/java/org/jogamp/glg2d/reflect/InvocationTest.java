package org.jogamp.glg2d.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class InvocationTest {
  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> robotClass = Class.forName("org.jogamp.glg2d.reflect.Robot");

    Object o = GenericInvocationHandler.makeImplement(robotClass, new Object() {
      @SuppressWarnings("unused")
      public void moveTo(int a, int b) {
        System.out.println("moveTo " + a + ", " + b);
      }
    });

    Method moveToMethod = robotClass.getMethod("moveTo", int.class, int.class);

    moveToMethod.invoke(o, 0, 0);
  }
}
