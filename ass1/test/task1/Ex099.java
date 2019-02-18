package vt14.ass1.test.task1;

import java.util.logging.Level;
import java.util.logging.Logger;

import vt14.ass1.*;

class Ex099
{
  private static boolean wait = true;
  private static Object syncLock;

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      synchronized(syncLock) {
        while(!wait) {
          try {
             syncLock.wait();
           } catch (InterruptedException ex) {
             Logger.getLogger(Ex099.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
      lock_one_.lock();
        lock_two_.lock();
        lock_two_.unlock();
      lock_one_.unlock();
      wait = !wait;
      synchronized(syncLock) {
        syncLock.notifyAll();
      }
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      synchronized(syncLock) {
        while(wait) {
          try {
             syncLock.wait();
           } catch (InterruptedException ex) {
             Logger.getLogger(Ex099.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
      lock_two_.lock();
          lock_one_.lock();
          lock_one_.unlock();
      lock_two_.unlock();
      wait = !wait;
      synchronized(syncLock) {
        syncLock.notifyAll();
      }
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {
    syncLock = new Object();

    Thread1 example1 = new Thread1("False Alarm Thread 1");
    example1.start();

    try {
      example1.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    Thread2 example2 = new Thread2("False Alarm Thread 2");
    example2.start();

    try {
      example2.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    LockTreeChecker.getInstance().printLockTree(example1);
    LockTreeChecker.getInstance().printLockTree(example2);
    LockTreeChecker.getInstance().checkAll();
  }

}
