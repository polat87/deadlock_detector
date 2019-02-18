package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex058
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
  private static class Thread1 extends Thread
  {
    LockTreeLock lock_one_ = ManyLocks.lock_one;
    LockTreeLock lock_two_ = ManyLocks.lock_two;
    LockTreeLock lock_three = ManyLocks.lock_three;
    LockTreeLock lock_four = ManyLocks.lock_four;

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
      lock_one_.lock();
      lock_one_.unlock();
      lock_two_.unlock();
      lock_one_.unlock();
      
      lock_three.lock();
      lock_four.lock();
      lock_three.lock();
      lock_three.unlock();
      lock_four.unlock();
      lock_three.unlock();
    }

  }

  //---------------------------------------------------------------------------
  private static class Thread2 extends Thread {

	    LockTreeLock lock_one_ = ManyLocks.lock_one;
	    LockTreeLock lock_two_ = ManyLocks.lock_two;
	    LockTreeLock lock_three = ManyLocks.lock_three;
	    LockTreeLock lock_four = ManyLocks.lock_four;

    //-----------------------------------------------------------------------
    Thread2(String name)
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
        

        lock_four.lock();
        lock_three.lock();
        lock_three.unlock();
        lock_four.unlock();
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {

	String threadName1 = "ExampleSelfLock Thread 1";
	String threadName2 = "ExampleSelfLock Thread 2";
    Thread1 example1 = new Thread1(threadName1);
    example1.start();
    
    try {
      example1.join();
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    Thread2 example2 = new Thread2(threadName2);
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
