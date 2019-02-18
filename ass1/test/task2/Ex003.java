package vt14.ass1.test.task2;

import java.util.concurrent.locks.ReentrantLock;

import vt14.ass1.*;


public class Ex003
{

  //---------------------------------------------------------------------------
  private static class MyLock extends ReentrantLock
  {
    private static final long serialVersionUID = 6144923655345169522L;
    
    String lockName;

    public MyLock(String name) {
      lockName = name;
    }   
  }
  
  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static MyLock lock_one = new MyLock("Lock1");
    public static ReentrantLock lock_two = new ReentrantLock();
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    MyLock lock_one_ = ManyLocks.lock_one;
    ReentrantLock lock_two_ = ManyLocks.lock_two;

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
    MyLock lock_one_ = ManyLocks.lock_one;
    ReentrantLock lock_two_ = ManyLocks.lock_two;

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
