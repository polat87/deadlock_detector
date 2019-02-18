package vt14.ass1.test.task2;

import java.util.concurrent.locks.ReentrantLock;

import vt14.ass1.*;

public class Ex010
{

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static ReentrantLock lock_one = new ReentrantLock();
    public static ReentrantLock lock_two = new ReentrantLock();
  }
  
  private static class WorkInConstructor1 {
    
    public WorkInConstructor1() {
      ManyLocks.lock_one.lock();
      ManyLocks.lock_two.lock();
      ManyLocks.lock_two.unlock();
      ManyLocks.lock_one.unlock();
    }
  }
  
  private static class WorkInConstructor2 {
    
    public WorkInConstructor2() {
      ManyLocks.lock_two.lock();
      ManyLocks.lock_one.lock();
      ManyLocks.lock_one.unlock();
      ManyLocks.lock_two.unlock();
    }
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      WorkInConstructor1 w = new WorkInConstructor1();
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      WorkInConstructor2 w = new WorkInConstructor2();
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
