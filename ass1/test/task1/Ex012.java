package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex012
{

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static LockTreeLock lock_four = new LockTreeLock("Lock4");
  
  }

  //---------------------------------------------------------------------------
  private static class ThreadA extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;

    //-----------------------------------------------------------------------
    ThreadA(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_one_.lock();
      lock_two_.lock();
      lock_three_.lock();
      lock_four_.lock();
      lock_four_.unlock();
      lock_three_.unlock();
      lock_two_.unlock();
      lock_three_.lock();
      lock_four_.lock();
      lock_four_.unlock();
      lock_three_.unlock();
      lock_four_.lock();
      lock_four_.unlock();      
      lock_one_.unlock();
    }

  }

  //---------------------------------------------------------------------------
  private static class ThreadB extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;

    //-----------------------------------------------------------------------
    ThreadB(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_two_.lock();
      lock_four_.lock();
      lock_three_.lock();
      lock_three_.unlock();
      lock_four_.unlock();
      lock_three_.lock();
      lock_four_.lock();
      lock_one_.lock();
      lock_one_.unlock();
      lock_four_.unlock();
      lock_three_.unlock();
      lock_two_.unlock();
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {
    ThreadA example_A = new ThreadA("ExampleThread A");
    example_A.start();

    try {
      example_A.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    ThreadB example_B = new ThreadB("ExampleThread B");
    example_B.start();

    try {
      example_B.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    LockTreeChecker.getInstance().printLockTree(example_A);
    LockTreeChecker.getInstance().printLockTree(example_B);
    LockTreeChecker.getInstance().checkAll();
  }

}
