package vt14.ass1.test.task1;

import java.util.logging.Level;
import java.util.logging.Logger;

import vt14.ass1.*;

class Ex039
{

    //---------------------------------------------------------------------------
    private static class ManyLocks
    {

        public static LockTreeLock lock_one = new LockTreeLock("Lock1");
        public static LockTreeLock lock_two = new LockTreeLock("Lock2");
    }

    //---------------------------------------------------------------------------
    private static class Thread1 extends Thread
    {

        LockTreeLock lock_one_ = ManyLocks.lock_one;
        LockTreeLock lock_two_ = ManyLocks.lock_two;

        //-----------------------------------------------------------------------
        Thread1(String name)
        {
            super(name);
        }

        //-----------------------------------------------------------------------
        public void run()
        {
            boolean bothLocks = false;

            do
            {
                lock_two_.lock();

                if (lock_one_.isLocked())
                {
                    lock_two_.unlock();
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(Ex040.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else
                {
                    lock_one_.lock();
                    bothLocks = true;
                }
            } while (!bothLocks);

            lock_one_.unlock();
            lock_two_.unlock();

        }
    }

    //---------------------------------------------------------------------------
    private static class Thread2 extends Thread
    {

        LockTreeLock lock_one_ = ManyLocks.lock_one;
        LockTreeLock lock_two_ = ManyLocks.lock_two;

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

        }
    }

    //----------------------------------------------------------------------
    public static void main(String[] args)
    {
        Thread1 example1 = new Thread1("FalseLockThread 1");
        example1.start();

        try
        {
            example1.join();
        } catch (InterruptedException e)
        {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        Thread2 example2 = new Thread2("FalseLockThread 2");
        example2.start();

        try
        {
            example2.join();
        } catch (InterruptedException e)
        {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        LockTreeChecker.getInstance().printLockTree(example1);
        LockTreeChecker.getInstance().printLockTree(example2);
        LockTreeChecker.getInstance().checkAll();
    }
}
