package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex062
{
    public static boolean var_lock = true;

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static LockTreeLock lock_four = new LockTreeLock("Lock4");
    public static LockTreeLock lock_five = new LockTreeLock("Lock5");
    public static LockTreeLock lock_six = new LockTreeLock("Lock6");

  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    LockTreeLock lock_five_ = ManyLocks.lock_five;
    LockTreeLock lock_six_ = ManyLocks.lock_six;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }

    //-----------------------------------------------------------------------
    public void run()
    {
      
      lock_five_.lock();
      
      var_lock = true;
      lock_one_.lock();
      lock_two_.lock();
      lock_two_.unlock();
      lock_one_.unlock();
      var_lock = false;
      lock_five_.unlock();
      
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    LockTreeLock lock_five_ = ManyLocks.lock_five;
    LockTreeLock lock_six_ = ManyLocks.lock_six;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }

    //-----------------------------------------------------------------------
    public void run()
    {
      lock_four_.lock();
      while(var_lock)
      {
          /* Do something */
          yield();
      }
      
      lock_two_.lock();
          lock_one_.lock();
          lock_one_.unlock();
          lock_two_.unlock();
      lock_four_.unlock();
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

    LockTreeChecker.getInstance().printLockTree(example1);
    LockTreeChecker.getInstance().printLockTree(example2);
    LockTreeChecker.getInstance().checkAll();
  }

}
