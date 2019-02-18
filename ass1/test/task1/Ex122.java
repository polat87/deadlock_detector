package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex122 {

	// ---------------------------------------------------------------------------
	private static class ManyLocks {
		public static LockTreeLock lock_a = new LockTreeLock("LockA");
		public static LockTreeLock lock_b = new LockTreeLock("LockB");
	}

	private static Integer value = 0;

	// ---------------------------------------------------------------------------
	private static class Thread1 extends Thread {
		LockTreeLock lock_a_ = ManyLocks.lock_a;
		LockTreeLock lock_b_ = ManyLocks.lock_b;

		// -----------------------------------------------------------------------
		Thread1(String name) {
			super(name);
		}

		// -----------------------------------------------------------------------
		public void run() {
			lock_a_.lock();
			lock_b_.lock();
			lock_b_.unlock();
			lock_a_.unlock();

			value = 1;
			synchronized (value) {
				value.notifyAll();
			}
		}
	}

	// ---------------------------------------------------------------------------
	private static class Thread2 extends Thread {
		LockTreeLock lock_a_ = ManyLocks.lock_a;
		LockTreeLock lock_b_ = ManyLocks.lock_b;

		// -----------------------------------------------------------------------
		Thread2(String name) {
			super(name);
		}

		// -----------------------------------------------------------------------
		public void run() {
			synchronized (value) {
				if (value != 1) {
					try {
						value.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			lock_b_.lock();
			lock_a_.lock();
			lock_a_.unlock();
			lock_b_.unlock();
		}
	}

	// ----------------------------------------------------------------------
	public static void main(String[] args) {
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
