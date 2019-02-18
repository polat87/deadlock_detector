package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex040
{

  //---------------------------------------------------------------------------
  private static class ManyLocks
  {
    public static LockTreeLock lock_one = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two = new LockTreeLock("Lock2");

    public static int tid = 0;
  
  }

  //---------------------------------------------------------------------------
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_1_ = ManyLocks.lock_one;
    LockTreeLock lock_2_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      while(ManyLocks.tid != 0)
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
      
    	
	  lock_1_.lock();
      lock_2_.lock();
      lock_2_.unlock();
      lock_1_.unlock();
      
      ManyLocks.tid = 1;
      
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {
    LockTreeLock lock_1_ = ManyLocks.lock_one;
    LockTreeLock lock_2_ = ManyLocks.lock_two;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
        while(ManyLocks.tid != 1)
    		try {
    			wait();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
          
        	
    	  lock_2_.lock();
          lock_1_.lock();
          lock_1_.unlock();
          lock_2_.unlock();
          
          ManyLocks.tid = 0;
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
