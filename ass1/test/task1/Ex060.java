package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex060
{
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static LockTreeLock lock_four = new LockTreeLock("Lock4");
    public static boolean busy = false;
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }

    //-----------------------------------------------------------------------
    public void run()
    {
      while (ManyLocks.busy)
        ;
      
      ManyLocks.busy = true;

      lock_two_.lock();
      lock_one_.lock();

      lock_one_.unlock();
      lock_two_.unlock();

      ManyLocks.busy = false;
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }

    //-----------------------------------------------------------------------
    public void run()
    {
      while (ManyLocks.busy)
        ;
      
      ManyLocks.busy = true;

      lock_one_.lock();
      lock_two_.lock();

      lock_two_.unlock();
      lock_one_.unlock();
      
      ManyLocks.busy = false;
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

    try
    {
      LockTreeChecker.getInstance().checkAll();
    }
    catch(Exception ex)
    {
      System.err.println("Got fucked up in LockTreeChecker.check()");
      System.err.println(ex.getMessage());
      System.exit(-1);
    }
  }
}