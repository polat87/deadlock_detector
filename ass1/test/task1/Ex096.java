
package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex096
{
  //---------------------------------------------------------------------------
  private static class LockCollection
  {
    public static LockTreeLock lock_one   = new LockTreeLock("Lock1");
    public static LockTreeLock lock_two   = new LockTreeLock("Lock2");
    public static LockTreeLock lock_three = new LockTreeLock("Lock3");
    public static int scheduler = 0;
  }

  //---------------------------------------------------------------------------
  private static class ThreadA extends Thread
  {
    LockTreeLock lock_one_   = LockCollection.lock_one;
    LockTreeLock lock_two_   = LockCollection.lock_two;
    LockTreeLock lock_three_ = LockCollection.lock_three; 

    //-----------------------------------------------------------------------
    ThreadA(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      while(LockCollection.scheduler == 1)
        yield();
      
      lock_one_.lock();
      lock_two_.lock();
      lock_three_.lock();
      lock_three_.unlock();
      lock_two_.unlock();
      lock_one_.unlock();
      
      LockCollection.scheduler = 1;
    }
  }

  //---------------------------------------------------------------------------
  private static class ThreadB extends Thread
  {
    LockTreeLock lock_one_   = LockCollection.lock_one;
    LockTreeLock lock_two_   = LockCollection.lock_two;
    LockTreeLock lock_three_ = LockCollection.lock_three;

    //-----------------------------------------------------------------------
    ThreadB(String name)
    {
      super(name);
    }
    
    //-----------------------------------------------------------------------
    public void run()
    {
      while(LockCollection.scheduler == 0)
        yield();
          
      lock_two_.lock();
      lock_one_.lock();
      lock_three_.lock();
      lock_three_.unlock();
      lock_one_.unlock();
      lock_two_.unlock();
      
      LockCollection.scheduler = 0;
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
