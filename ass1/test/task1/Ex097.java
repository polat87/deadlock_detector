package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex097
{
  //---------------------------------------------------------------------------
  private static class LockCollection
  {
  	public static LockTreeLock lock_one   = new LockTreeLock("Lock1");
  	public static LockTreeLock lock_two   = new LockTreeLock("Lock2");
  	public static LockTreeLock lock_three = new LockTreeLock("Lock3");
  	public static LockTreeLock lock_four  = new LockTreeLock("Lock4");
  	public static LockTreeLock lock_five  = new LockTreeLock("Lock5");
  	public static LockTreeLock lock_six   = new LockTreeLock("Lock6");
  	public static LockTreeLock lock_seven = new LockTreeLock("Lock7");
  }

  //---------------------------------------------------------------------------
  private static class ThreadA extends Thread
  {
    LockTreeLock lock_one_   = LockCollection.lock_one;
    LockTreeLock lock_two_   = LockCollection.lock_two;
    LockTreeLock lock_three_ = LockCollection.lock_three;
    LockTreeLock lock_four_  = LockCollection.lock_four;
    LockTreeLock lock_five_  = LockCollection.lock_five;
    LockTreeLock lock_six_   = LockCollection.lock_six;
    LockTreeLock lock_seven_ = LockCollection.lock_seven;

    //-----------------------------------------------------------------------
    ThreadA(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_three_.lock();
      lock_one_.lock();
      lock_two_.lock();
      lock_two_.unlock();
      lock_one_.unlock();
      lock_three_.unlock();
      
      lock_four_.lock();
      lock_four_.unlock();
      
      lock_seven_.lock();
      lock_five_.lock();
      lock_six_.lock();
      lock_six_.unlock();
      lock_five_.unlock();
      lock_seven_.unlock();
    }
  }

  //---------------------------------------------------------------------------
  private static class ThreadB extends Thread
  {
    LockTreeLock lock_one_   = LockCollection.lock_one;
    LockTreeLock lock_two_   = LockCollection.lock_two;
    LockTreeLock lock_three_ = LockCollection.lock_three;
    LockTreeLock lock_four_  = LockCollection.lock_four;
    LockTreeLock lock_five_  = LockCollection.lock_five;
    LockTreeLock lock_six_   = LockCollection.lock_six;
    LockTreeLock lock_seven_ = LockCollection.lock_seven;

    //-----------------------------------------------------------------------
    ThreadB(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      lock_three_.lock();
      lock_two_.lock();
      lock_one_.lock();
      lock_one_.unlock();
      lock_two_.unlock();
      lock_three_.unlock();
      
      lock_seven_.lock();
      lock_six_.lock();
      lock_five_.lock();
      lock_five_.unlock();
      lock_six_.unlock();
      lock_seven_.unlock();
      
      lock_four_.lock();
      lock_four_.unlock();
    }
  }

  //----------------------------------------------------------------------
  public static void main(String[] args)
  {
    ThreadA thread_a = new ThreadA("MThreadA");
    thread_a.start();

    try
    {
      thread_a.join();
    } catch (InterruptedException e)
    {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    ThreadB thread_b = new ThreadB("MThreadB");
    thread_b.start();

    try
    {
      thread_b.join();
    } catch (InterruptedException e)
    {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    LockTreeChecker.getInstance().printLockTree(thread_a);
    LockTreeChecker.getInstance().printLockTree(thread_b);
    LockTreeChecker.getInstance().checkAll();
  }
}
