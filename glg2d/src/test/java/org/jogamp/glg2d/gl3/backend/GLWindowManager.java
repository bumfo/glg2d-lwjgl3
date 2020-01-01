package org.jogamp.glg2d.gl3.backend;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

final class GLWindowManager extends WindowAdapter {
  private volatile boolean destroyed = false;
  private GLWindow window;

  private AnimatorManager animatorManager;

  private final boolean isAWT;

  private final Semaphore interrupt = new Semaphore(0);
  private final Semaphore task = new Semaphore(0);
  private final Thread myThread;

  GLWindowManager(GLWindow window, boolean isAWT) {
    this.window = window;
    this.isAWT = isAWT;

    animatorManager = new AnimatorManager();
    animatorManager.init(window);

    myThread = new Thread(this::run);
    myThread.start();
  }

  private void run() {
    try {
      while (!destroyed) {
        task.acquire();

        while (!destroyed && interrupt.tryAcquire(100, TimeUnit.MILLISECONDS)) {
          interrupt.drainPermits();
        }

        if (destroyed) return;

        task.drainPermits();
        animatorManager.resume();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  void scheduleResume() {
    interrupt.release();
    task.release();
  }

  @Override
  public void windowDestroyNotify(WindowEvent e) {
    if (destroyed) return;
    destroyed = true;

    myThread.interrupt();

    System.out.println("onDestroy");
    animatorManager.dispose(window);

    if (!isAWT) {
      window.setVisible(false, false);

      new Thread(() -> {
        window.destroy();
      }).start();
    }
  }
}
