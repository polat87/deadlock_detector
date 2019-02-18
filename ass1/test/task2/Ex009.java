package vt14.ass1.test.task2;

import java.util.concurrent.locks.ReentrantLock;

import vt14.ass1.*;

public class Ex009
{
  //---------------------------------------------------------------------------
  private static class NotALock
  {
    public void lock() {
      // The method is named lock(), but it does not lock anything
      int b = 9;
      b = b + 2;
    }
    
    public void unlock() {
      // The method is named unlock(), but it does not unlock anything
      int c = 9;
      c = c + 2;
    }
    
  }
  
  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static ReentrantLock lock_one = new ReentrantLock();
    public static NotALock lock_two = new NotALock();
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    ReentrantLock lock_one_ = ManyLocks.lock_one;
    NotALock lock_two_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_one_.lock();
      lock_two_.lock();
      lock_two_.unlock();
      lock_one_.unlock();

    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    ReentrantLock lock_one_ = ManyLocks.lock_one;
    NotALock lock_two_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_two_.lock();
      lock_one_.lock();
      lock_one_.unlock();
      lock_two_.unlock();
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {
    Thread1 example1 = new Thread1("ExampleThread 1");
    example1.start();

    try {
      example1.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    Thread2 example2 = new Thread2("ExampleThread 2");
    example2.start();

    try {
      example2.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

//    LockTreeChecker.getInstance().printLockTree(example1);
//    LockTreeChecker.getInstance().printLockTree(example2);
  }

}
