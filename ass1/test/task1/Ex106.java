package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex106
{
  static boolean needupdate_ = true;

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static LockTreeLock lock_four = new LockTreeLock("Lock4");
    public static LockTreeLock lock_six = new LockTreeLock("Lock6");
    public static LockTreeLock lock_seven = new LockTreeLock("Lock7");
    public static LockTreeLock lock_ten = new LockTreeLock("Lock10");
  
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    LockTreeLock lock_six_ = ManyLocks.lock_six;
    LockTreeLock lock_seven_ = ManyLocks.lock_seven;
    LockTreeLock lock_ten_ = ManyLocks.lock_ten;
    

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
      if(needupdate_ == true){
        lock_four_.lock();
        // update some value...
        lock_four_.unlock();
      }
      needupdate_ = false;
      lock_one_.unlock();
      
      lock_two_.lock();
      lock_six_.lock();
      lock_six_.unlock();
      lock_two_.unlock();
      
      //printLockTree();
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three_ = ManyLocks.lock_three;
    LockTreeLock lock_four_ = ManyLocks.lock_four;
    LockTreeLock lock_six_ = ManyLocks.lock_six;
    LockTreeLock lock_seven_ = ManyLocks.lock_seven;
    LockTreeLock lock_ten_ = ManyLocks.lock_ten;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_four_.lock();
      if(needupdate_ == false){
        lock_one_.lock();
        lock_one_.unlock();
      }
      needupdate_ = true;
      
      lock_three_.lock();
      lock_three_.unlock();
      lock_four_.unlock();

      lock_one_.lock();
      lock_two_.lock();
      lock_two_.unlock();
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
