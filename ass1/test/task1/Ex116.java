package vt14.ass1.test.task1;

import vt14.ass1.*;

public class Ex116 {

	private static class ManyLocks {	
		public static LockTreeLock first_lock = new LockTreeLock("Lock 1");
		public static LockTreeLock second_lock = new LockTreeLock("Lock 2");
		public static LockTreeLock third_lock = new LockTreeLock("Lock 3");
		public static LockTreeLock fourth_lock = new LockTreeLock("Lock 4");
		public static LockTreeLock fifth_lock = new LockTreeLock("Lock 5");
		public boolean isItFree = false;
	}
	
	private static class Thread1 extends Thread {
		LockTreeLock first_lock = ManyLocks.first_lock;
		LockTreeLock second_lock = ManyLocks.second_lock;
		LockTreeLock third_lock = ManyLocks.third_lock;
		LockTreeLock fourth_lock = ManyLocks.fourth_lock;
		LockTreeLock fifth_lock = ManyLocks.fifth_lock;
		ManyLocks ml = new ManyLocks();
		
		Thread1(String str_name) {
			super(str_name);
		}
		
		public void run()
		{
			while(ml.isItFree != false)
			{
				yield();
			}
			System.out.println(">> c) begin");
			fifth_lock.lock();
				fourth_lock.lock();
					third_lock.lock();
						second_lock.lock();
							first_lock.lock();
							first_lock.unlock();
						second_lock.unlock();
					third_lock.unlock();
				fourth_lock.unlock();
			fifth_lock.unlock();
			System.out.println(">> c) end");
			ml.isItFree = true;
		}
	}
	
	private static class Thread2 extends Thread {
		LockTreeLock first_lock = ManyLocks.first_lock;
		LockTreeLock second_lock = ManyLocks.second_lock;
		LockTreeLock third_lock = ManyLocks.third_lock;
		LockTreeLock fourth_lock = ManyLocks.fourth_lock;
		LockTreeLock fifth_lock = ManyLocks.fifth_lock;
		ManyLocks ml = new ManyLocks();
		
		Thread2(String str_name) {
			super(str_name);
		}
		
		public void run()
		{
			while(ml.isItFree != false)
			{
				yield();
			}
			System.out.println(">> c) begin");
			fourth_lock.lock();
				fifth_lock.lock();
					third_lock.lock();
					third_lock.unlock();
				fifth_lock.unlock();
			fourth_lock.unlock();
			ml.isItFree = true; 
			first_lock.lock();
				second_lock.lock();
				second_lock.unlock();
			first_lock.unlock();
			System.out.println(">> c) end");	
		}
	}	
	
	public static void main(String[] args) {
		
		System.out.println("-- BEGIN --");	
		Thread1 example_thread_1 = new Thread1("ExampleThread 1");
		example_thread_1.start();
		
		try {
			example_thread_1.join();
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		
		Thread2 example_thread_2 = new Thread2("ExampleThread 2");
		
		example_thread_2.start();
		
		try {
			example_thread_2.join();			
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		
		//LockTreeChecker.getInstance().printLockTree(example_thread_1);
		LockTreeChecker.getInstance().printLockTree(example_thread_2);
		LockTreeChecker.getInstance().checkAll();
	  System.out.println("-- END --");

	}

}

