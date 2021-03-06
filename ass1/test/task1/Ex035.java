package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex035
{

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
    LockTreeLock lock_1_ = ManyLocks.lock_one;
    LockTreeLock lock_2_ = ManyLocks.lock_two;
    LockTreeLock lock_3_ = ManyLocks.lock_three;
    LockTreeLock lock_4_ = ManyLocks.lock_four;
    LockTreeLock lock_5_ = ManyLocks.lock_five;
    LockTreeLock lock_6_ = ManyLocks.lock_six;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_1_.lock();
      
      lock_2_.lock();
      lock_4_.lock();
      lock_5_.lock();
      lock_5_.unlock();
      lock_4_.unlock();
      lock_4_.lock();
      lock_5_.lock();
      lock_5_.unlock();
      lock_4_.unlock();
      lock_2_.unlock();
      
      lock_3_.lock();
      lock_6_.lock();
      lock_6_.unlock();
      lock_3_.unlock();
     
      lock_1_.unlock();
      
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_1_ = ManyLocks.lock_one;
    LockTreeLock lock_2_ = ManyLocks.lock_two;
    LockTreeLock lock_3_ = ManyLocks.lock_three;
    LockTreeLock lock_4_ = ManyLocks.lock_four;
    LockTreeLock lock_5_ = ManyLocks.lock_five;
    LockTreeLock lock_6_ = ManyLocks.lock_six;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
    	lock_3_.lock();
    	lock_5_.lock();
    	lock_1_.lock();
    	lock_1_.unlock();
    	lock_5_.unlock();
    	lock_4_.lock();
    	lock_4_.unlock();
    	lock_3_.unlock();
    	
    	lock_4_.lock();
    	lock_5_.lock();
    	lock_5_.unlock();
    	lock_3_.lock();
    	lock_2_.lock();
    	lock_1_.lock();
    	lock_1_.unlock();
    	lock_2_.unlock();
    	lock_3_.unlock();
    	lock_4_.unlock();
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {
    Thread1 example1 = new Thread1("Thread 1");
    example1.start();

    try {
      example1.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    Thread2 example2 = new Thread2("Thread 2");
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
