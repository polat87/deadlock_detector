package vt14.ass1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import vt14.ass1.util.GenericTreeNode;

public class LockTreeLock extends ReentrantLock
{
    public static final long serialVersionUID = 1;

    protected String name;
    public static Map<Thread, GenericTreeNode<String>> Map= new HashMap<Thread, GenericTreeNode<String>>();

    public static Map<Thread, GenericTreeNode<String>>Map()
    {
    	return Map;
    }
    
    public LockTreeLock(String name) {
        this.name = name;
    }

  
    public String getName() {
        return name;
    }

    public void lock() {

    Thread current = Thread.currentThread();
    
    if(!this.isHeldByCurrentThread())    	
    {
		if(!Map().containsKey(current))
		{
		    super.lock();		
			Map().put(current, new GenericTreeNode<String>(current.getName()));
			Map().get(current).addChild(new GenericTreeNode<String>(this.getName()));
			Map().put(current, Map().get(current).getChildren().get(0));
		}
		else
		{
		    super.lock();		
			GenericTreeNode<String> child = new GenericTreeNode<String>(this.getName());    	
			Map().get(current).addChild(child);
			Map().put(current, child);    	
		}
    }

//	Queries if this lock is held by any thread.
      // TODO: build up your lock tree here
      // Don't forget to call super.lock()
    	
    }

    public void unlock() {
      // TODO: build up your lock tree here
      // Don't forget to call super.unlock()
      if(this.isHeldByCurrentThread())
      {
		  Thread current = Thread.currentThread();      
		  GenericTreeNode<String> father = Map().get(current).getParent();
		  Map().put(current, father);    	
		  super.unlock();
      }
    }

    public String toString() {
        return name;
    }
}
