package vt14.ass1.test.task1;

import vt14.ass1.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ex061
{

  static final private MyDebugger debugger = new MyDebugger();

  final private List<MyThread> threads;


  public Ex061()
  {
    this.threads = new ArrayList<MyThread>();
  }

  public static void main(String[] args)
  {
    final Map<String, Lock> locks = new HashMap<String, Lock>();
    locks.put("Lock1", new LockTreeLock("Lock1"));
    locks.put("Lock2", new LockTreeLock("Lock2"));
    locks.put("SelfMadeGateLock", new ReentrantLock()); 


    final MyThread thread1 = new MyThread("ExampleThread 1", locks);
    thread1.addAction(new ThreadAction("LOCK", "SelfMadeGateLock")); 
    thread1.addAction(new ThreadAction("LOCK", "Lock1"));
    thread1.addAction(new ThreadAction("LOCK", "Lock2"));
    thread1.addAction(new ThreadAction("UNLOCK", "Lock2"));
    thread1.addAction(new ThreadAction("UNLOCK", "Lock1"));
    thread1.addAction(new ThreadAction("UNLOCK", "SelfMadeGateLock"));


    final MyThread thread2 = new MyThread("ExampleThread 2", locks);
    thread2.addAction(new ThreadAction("LOCK", "SelfMadeGateLock")); 
    thread2.addAction(new ThreadAction("LOCK", "Lock2")); 
    thread2.addAction(new ThreadAction("LOCK", "Lock1"));
    thread2.addAction(new ThreadAction("UNLOCK", "Lock1"));
    thread2.addAction(new ThreadAction("UNLOCK", "Lock2"));
    thread2.addAction(new ThreadAction("UNLOCK", "SelfMadeGateLock"));

    final Ex061 falsePositiveExample = new Ex061();
    falsePositiveExample.addThread(thread1);
    falsePositiveExample.addThread(thread2);

    falsePositiveExample.start();
  }

  public void addThread(final MyThread thread)
  {
    this.threads.add(thread);
  }


  public void start()
  {
    final long start = System.nanoTime();

    for (final MyThread thread : threads)
    {
      debugger.print("starting thread '" + thread.getName() + "' ...");

      thread.start(); 

      try
      {
        thread.join();

        debugger.print("joined thread '" + thread.getName() + "'.");
      }
      catch (final InterruptedException e)
      {
        System.err.println(e.getMessage());
        System.exit(-1);
      }
    }

    for (final MyThread thread : threads)
    {
      LockTreeChecker.getInstance().printLockTree(thread);
    }

    for (int i = 0, len = threads.size() - 1; i < len; ++i)
    {
      for (int k = i + 1, m = len + 1; k < m; ++k)
      {
        debugger.print("checking thread '" + threads.get(i).getName() + "' and '" + threads.get(k).getName() + "' for conflicts");

        LockTreeChecker.getInstance().checkAll();
      }
    }

    debugger.print("completed '" + threads.size() + "' threads in " + ((System.nanoTime() - start) / 1000000) + " ms.");
  }

  private static class MyThread extends Thread
  {
    final private Map<String, Lock> locks;

    final private List<ThreadAction> actions = new ArrayList<ThreadAction>();


    public MyThread(final String name, final Map<String, Lock> locks)
    {
      super(name);
      this.locks = locks;
    }

    public void addAction(final ThreadAction action)
    {
      if (!locks.keySet().contains(action.getLockName()))
      {
        throw new RuntimeException("no lock created for name '" + action.getLockName() + "'");
      }

      this.actions.add(action);
    }


    public void run()
    {
      for (final ThreadAction action : actions)
      {
        debugger.print("thread '" + getName() + "' performing action...");

        action.execute(locks);
      }

      debugger.print("thread '" + getName() + "' completed '" + actions.size() + "' actions.");
    }
  }

  private static class ThreadAction
  {
    private String actionType;

    private String lockName;

    public ThreadAction(final String actionType, final String lockName)
    {
      if (actionType == null || !("LOCK".equals(actionType) || "UNLOCK".equals(actionType)))
      {
        throw new RuntimeException("invalid action type");
      }
      if (lockName == null || lockName.length() < 1) throw new RuntimeException("invalid lock name");

      this.actionType = actionType;
      this.lockName = lockName;
    }

    public String getLockName()
    {
      return lockName;
    }

    public void execute(final Map<String, Lock> locks)
    {
      final Lock lock = locks.get(lockName);
      if (lock == null) throw new RuntimeException("invalid lock: " + lockName);
      if ("LOCK".equals(actionType))
      {

        debugger.print("locking '" + lockName + "'");

        lock.lock();
      }
      else
      { 

        debugger.print("UN-locking '" + lockName + "'");

        lock.unlock();
      }
    }
  }

  private static class MyDebugger
  {
    private boolean state;

    private MyDebugger()
    {
      this.state = (System.getProperty("debug") != null && "true".equals(System.getProperty("debug").toLowerCase())); // are we in debug mode?
    }

    public void setState(final boolean state)
    {
      this.state = state;
    }

    public void print(final String message)
    {
      if (state) System.out.println(message);
    }
  }


}
