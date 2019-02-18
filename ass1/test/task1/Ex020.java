package vt14.ass1.test.task1;

import java.util.concurrent.locks.ReentrantLock;

import vt14.ass1.*;

class Ex020
{

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static LockTreeLock lock_four = new LockTreeLock("Lock4");
    public static ReentrantLock gate_lock = new ReentrantLock();
    
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    ReentrantLock gate_lock_ = ManyLocks.gate_lock;
    

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      gate_lock_.lock();
     lock_one_.lock();
     lock_three_.lock();
     lock_four_.lock();
     lock_four_.unlock();
     lock_three_.unlock();
     lock_one_.unlock();
     gate_lock_.unlock();
     
     lock_two_.lock();
     gate_lock_.lock();
     lock_three_.lock();
     lock_four_.lock();
     lock_four_.unlock();
     lock_three_.unlock();
     gate_lock_.unlock();
     lock_two_.unlock();
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    ReentrantLock gate_lock_ = ManyLocks.gate_lock;
    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      gate_lock_.lock();
      lock_four_.lock();
      lock_one_.lock();
      lock_three_.lock();
      
      lock_three_.unlock();
      lock_one_.unlock();
      lock_four_.unlock();
      gate_lock_.unlock();
      
      lock_one_.lock();
      lock_one_.unlock();
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
