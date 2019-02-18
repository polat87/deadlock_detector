package vt14.ass1.test.task1;

import vt14.ass1.*;

class Ex145
{

	// ---------------------------------------------------------------------------
	private static class ManyLocks {
		public static LockTreeLock lock_one = new LockTreeLock("Lock1");
		public static LockTreeLock lock_two = new LockTreeLock("Lock2");

	}

	// ---------------------------------------------------------------------------
	private static class Thread1 extends Thread {
		LockTreeLock lock_one_ = ManyLocks.lock_one;
		LockTreeLock lock_two_ = ManyLocks.lock_two;

		// -----------------------------------------------------------------------
		Thread1(String name) {
			super(name);
		}

		// -----------------------------------------------------------------------
		public void run() {
			lock_one_.lock();
				lock_two_.lock();
				lock_two_.unlock();
			lock_one_.unlock();
		}

	}

	// ---------------------------------------------------------------------------
	private static class Thread2 extends Thread {
		LockTreeLock lock_one_ = ManyLocks.lock_one;
		LockTreeLock lock_two_ = ManyLocks.lock_two;

		// -----------------------------------------------------------------------
		Thread2(String name) {
			super(name);
		}

		// -----------------------------------------------------------------------
		public void run() {
			boolean ready = false;

			while (!ready) {
				lock_two_.lock();

				if (!lock_one_.isLocked()) {
					lock_one_.lock();
					lock_one_.unlock();

					ready = true;
				}

				lock_two_.unlock();

				if (!ready)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}
	}

	// ----------------------------------------------------------------------
	public static void main(String[] args) {
		Thread1 example1 = new Thread1("lock1");
		example1.start();

		try {
			example1.join();
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		Thread2 example2 = new Thread2("lock2");
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

