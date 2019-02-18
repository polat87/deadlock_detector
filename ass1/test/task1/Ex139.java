package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex139
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
    public static FalsePosLock fLock = new FalsePosLock();
  }

  //---------------------------------------------------------------------------
  private static class FalsePosLock
  {
	private static boolean false_pos = false;
    public void lock() {false_pos = true;}
    public void unlock() {false_pos = false;}
    public boolean isLocked() { return false_pos;}
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
    FalsePosLock fLock = ManyLocks.fLock;


    //-----------------------------------------------------------------------
    Thread1(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
    	while(fLock.isLocked());  
        fLock.lock();
        fLock.unlock();
        
    	lock_two_.lock();
   	 		lock_one_.lock();
                lock_five_.lock();
                lock_five_.unlock();
   	 		lock_one_.unlock();
            lock_three_.lock();
                lock_four_.lock();
                lock_four_.unlock();
            lock_three_.unlock();
   	 	lock_two_.unlock();
        lock_six_.lock();
        lock_six_.unlock();
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
    FalsePosLock fLock = ManyLocks.fLock;

    //-----------------------------------------------------------------------
    Thread2(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
        while(fLock.isLocked());  
        fLock.lock();
        fLock.unlock();
        
    	lock_one_.lock();
   	 		lock_two_.lock();
                lock_five_.lock();
                lock_five_.unlock();
   	 		lock_two_.unlock();
            lock_three_.lock();
                lock_four_.lock();
                lock_four_.unlock();
            lock_three_.unlock();
   	 	lock_one_.unlock();
        lock_five_.lock();
            lock_one_.lock();
                lock_two_.lock();
                    lock_six_.lock();
                    lock_six_.unlock();
                lock_two_.unlock();
            lock_one_.unlock();
        lock_five_.unlock();

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