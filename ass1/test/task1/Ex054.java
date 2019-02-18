package vt14.ass1.test.task1;

import vt14.ass1.*;

public class Ex054 {
	
	
	private static class TheLocks {
		public static LockTreeLock lock1 = new LockTreeLock("lock1");
		public static LockTreeLock lock2 = new LockTreeLock("lock2");
		public static LockTreeLock lock3 = new LockTreeLock("lock3");
		public static int g = 1;
	}
	
	private static class Thread1 extends Thread {
		LockTreeLock lock1 = TheLocks.lock1;
		LockTreeLock lock2 = TheLocks.lock2;
		LockTreeLock lock3 = TheLocks.lock3;
		
		//-----------------------------------------------------------------------
	    Thread1(String name)
	    {
	      super(name);
	    }
	    
	     public void run() {
	    	lock3.lock();
	    	while(TheLocks.g == 0) {
	    		lock3.unlock();
	    		yield();
	    		lock3.lock();
	    	}
	    	lock3.unlock();
	    	lock1.lock();
		     	lock2.lock();
		     	lock2.unlock();
		    lock1.unlock();
		    lock3.lock();
		    	TheLocks.g = 0;
		    lock3.unlock();
	     }
	}
	
	private static class Thread2 extends Thread {
		LockTreeLock lock1 = TheLocks.lock1;
		LockTreeLock lock2 = TheLocks.lock2;
		LockTreeLock lock3 = TheLocks.lock3;
		
		//-----------------------------------------------------------------------
	    Thread2(String name)
	    {
	      super(name);
	    }
	    
	     public void run() {
	    	lock3.lock();
		    while(TheLocks.g == 1) {
		    	lock3.unlock();
		    	yield();
		    	lock3.lock();
		    }
		    lock3.unlock();
	    	lock2.lock();
	    		lock1.lock();
	    		lock1.unlock();
	    	lock2.unlock();
	    	lock3.lock();
	    		TheLocks.g = 1;
	    	lock3.unlock();
	     }
	}
	
	//----------------------------------------------------------------------
	  public static void main(String[] args)
	  {
	    Thread1 example1 = new Thread1("Thread1");
	    example1.start();

	    try {
	      example1.join();
	    } catch (InterruptedException e) {
	      System.err.println(e.getMessage());
	      System.exit(-1);
	    }

	    Thread2 example2 = new Thread2("Thread2");
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
