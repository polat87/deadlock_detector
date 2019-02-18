package vt14.ass1.test.task1;

import vt14.ass1.*;

import java.util.ArrayList;

//----------------------------------------------------------------------
class Ex091 
{
    //----------------------------------------------------------------------
    private class LockPool
    {
        ArrayList<LockTreeLock> locks = new ArrayList<LockTreeLock>();
        
        //----------------------------------------------------------------------
        public LockPool(int size)
        {
            if (size < 1)
                throw new IllegalArgumentException("size");
            
            for (int i=0;i<size;++i)
                locks.add(new LockTreeLock("Lock_" + (i+1)));            
        }
        
        //----------------------------------------------------------------------
        public void lock(int lockNr)
        {
            if (lockNr >= locks.size())
                throw new IndexOutOfBoundsException("lockNr");
            
            locks.get(lockNr).lock();
        }
        
        //----------------------------------------------------------------------
        public void unlock(int lockNr)
        {
            if (lockNr >= locks.size())
                throw new IndexOutOfBoundsException("lockNr");            
            
            locks.get(lockNr).unlock();
        }
        
        //----------------------------------------------------------------------
        public int getSize()
        {
            return locks.size();
        }
    }
    
    //----------------------------------------------------------------------
    private class SequenceLockThread extends Thread
    {
        LockPool lpool;
        ArrayList<ArrayList<Integer>> sequences = new ArrayList<ArrayList<Integer>>();
        
        public SequenceLockThread(String name,LockPool lpool) 
        {
            super(name);
            
            if (lpool == null)
                throw new NullPointerException("lpool can't be null");
            
            this.lpool = lpool;
        }
        
        //----------------------------------------------------------------------
        public void addSequence(int... sequence)
        {
            if (sequence.length != lpool.getSize())
                throw new IllegalArgumentException("Length of sequence must equal the size of the lock pool in use");
            
            ArrayList<Integer> newseq = new ArrayList<Integer>();
            for (int i : sequence)
                newseq.add(i);
            sequences.add(newseq);            
        }    

        //----------------------------------------------------------------------
        @Override
        public void run() 
        {            
            for (ArrayList<Integer> seq : sequences)
            {
                 int top = seq.size();
                
                 for (int i=0;i<top;++i)
                     lpool.lock(seq.get(i));
                 
                 for (int i=top-1;i>=0;--i)
                     lpool.unlock(seq.get(i));
            }            
        }
        
    }
    
    //----------------------------------------------------------------------
    private void runTest()
    {
        LockPool lpool = new LockPool(3);
        SequenceLockThread[] threads = new SequenceLockThread[2];
        
        threads[0] = new SequenceLockThread("Thread1", lpool);
        threads[1] = new SequenceLockThread("Thread2", lpool);
        
        threads[0].addSequence(0,1,2);
        threads[1].addSequence(0,2,1);  
        
        for (SequenceLockThread t : threads)
        {
            t.start();
            
            try
            {
                t.join();
            }
            catch (InterruptedException e) 
            {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        }          
        
        LockTreeChecker.getInstance().printLockTree(threads[0]);
        LockTreeChecker.getInstance().printLockTree(threads[1]);
        LockTreeChecker.getInstance().checkAll();
    }

    //----------------------------------------------------------------------
    public static void main(String[] args)
    {
        Ex091 d = new Ex091();
        d.runTest();        
    }


}

