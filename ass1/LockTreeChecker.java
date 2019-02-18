package vt14.ass1;

import vt14.ass1.util.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

//---------------------------------------------------------------------------
public class LockTreeChecker
{
    private static LockTreeChecker instance;

    //-----------------------------------------------------------------------
    private LockTreeChecker() {}
    
    public static Map<Integer, Integer> lock_map = new HashMap<Integer, Integer>();
    public static Queue<GenericTreeNode<String>> explore_que = new LinkedList<GenericTreeNode<String>>();
    
    //-----------------------------------------------------------------------
    public static synchronized LockTreeChecker getInstance() {
        if (LockTreeChecker.instance == null) {
            LockTreeChecker.instance = new LockTreeChecker();
        }
        return LockTreeChecker.instance;
    }
  

    //-----------------------------------------------------------------------
    // Checks if the locktrees of the threads with the names <name1> and <name2>
    // show any potential deadlocks
    public void check(Thread thread1, Thread thread2) {
        System.out.println("Checking \"" + thread1.getName() + "\" and \"" + thread2.getName() + "\"");        
        // TODO:
        // Add your implementation to check the constructed lock trees here
        
        lock_map.clear();
        explore_que.clear();
        GenericTreeNode<String> tr1 = LockTreeLock.Map().get(thread1);
        GenericTreeNode<String> tr2 = LockTreeLock.Map().get(thread2);

        while(tr1.getParent() != null)        	
        	tr1 = tr1.getParent();        

        while(tr2.getParent() != null)        	
        	tr2 = tr2.getParent();                
        
        Queue<GenericTreeNode<String>> que = new LinkedList<GenericTreeNode<String>>();
    	if(!tr1.getChildren().isEmpty())
    	{
    		for(GenericTreeNode<String> elem : tr1.getChildren())
    		{
    			que.add(elem);
    		}
    		
    	}        
                
        boolean stop = false;        
        while(!stop)
        {        	
        	GenericTreeNode<String> father = que.poll();
        	if(father == null)
        		stop = true;
        	else
        	{
        		for(GenericTreeNode<String> child : father.getChildren())
				{        			
        			if(!explore_que.contains(father))  				        			
    					searchRecursiv(father, father, tr2);        			
					que.add(child);			
        			explore_que.add(father);
					
				}       	
        	}
        	
        	
        }

    }
    
    public void searchRecursiv(GenericTreeNode<String> father, GenericTreeNode<String> node, GenericTreeNode<String> tr2)
    {    	
        for(GenericTreeNode<String> child : node.getChildren())
		{
        	findDeadlock(father, child, tr2);    	
	    	searchRecursiv(father, child, tr2);    	
		}        	    	
    }
    
    public void findDeadlock(GenericTreeNode<String> node1, GenericTreeNode<String> node2, GenericTreeNode<String> tr2)
    {
        for(GenericTreeNode<String> child : tr2.getChildren())
		{        	
	    	if(child.getData().equals(node1.getData()))
	    	{
	    		GenericTreeNode<String> father = null;
	    		GenericTreeNode<String> temp = child;

	    		while(temp.getParent() != null)	    			
	    		{
	    			if(temp.getData().equals(node2.getData()))
	    				father = temp;
	    			temp = temp.getParent(); 	    			  			    			
	    		}
	    		
	    		if(father != null)
	    		{
			    	if(!isGated(node2, father) && !alreadyChecked(node1, node2, father, child))
			    	{
			    		lock_map.put(father.hashCode(), (node1.hashCode() + node2.hashCode()));
			    		lock_map.put(child.hashCode(), (node1.hashCode() + node2.hashCode()));
		    			System.out.print("POTENTIAL DEAD LOCK FOUND:\n" +  node2.getData() + "<->" + node1.getData() + "\n");
			    	}	    			
	    		}    			    		
	    	}	    	
	    	findDeadlock(node1, node2, child);
		}       		
    }
    
    public GenericTreeNode<String> checkDeadlock(GenericTreeNode<String> node1, GenericTreeNode<String> begin)
    {    	
        for(GenericTreeNode<String> child : begin.getChildren())
		{
	    	if(child.getData().equals(node1.getData()))
	    		return child;
	    	return checkDeadlock(node1, child);
		}     	    	
        return null;
    }
    
    public boolean alreadyChecked(GenericTreeNode<String> node1, GenericTreeNode<String> node2, GenericTreeNode<String> father, GenericTreeNode<String> child)
    {	
		for (Map.Entry<Integer, Integer> entry : lock_map.entrySet())
		{

			if(entry.getValue() == (node1.hashCode() + node2.hashCode()))
			{
				if(entry.getKey() == child.hashCode() || entry.getKey() == father.hashCode())
					return true;
			}
		}
		
    	return false;
    }
    

    public boolean isGated(GenericTreeNode<String> tree1, GenericTreeNode<String> tree2)
    {
        Queue<GenericTreeNode<String>> fathers1 = new LinkedList<GenericTreeNode<String>>();    	
        Queue<GenericTreeNode<String>> fathers2 = new LinkedList<GenericTreeNode<String>>();    	

		while(tree1.getParent() != null)	         
        {
        	fathers1.add(tree1.getParent());
        	tree1 = tree1.getParent();
        } 

		while(tree2.getParent() != null)	 		
        {
        	fathers2.add(tree2.getParent());
        	tree2 = tree2.getParent();
        }    	

		for(GenericTreeNode<String> f1 : fathers1)
		{			
			for(GenericTreeNode<String> f2 : fathers2)
			{
				if(f1.getData().equals(f2.getData()))
					return true;
			} 					
		}           
		
    	return false;
    }

    public void checkAll() {
        Collection<Thread> threads = LockTreeLock.Map().keySet();
        // TODO: get all threads that have to be checked
        for (Thread thread1 : threads) {
            for (Thread thread2 : threads) {
                if(thread1 == thread2) {
                    continue;
                }
                check(thread1, thread2);
            }
        }
    }

    public void printLockTree(Thread thread) {    	
    	GenericTreeNode<String> root = LockTreeLock.Map().get(thread);    	
//        while(!root.getData().contains("Thread"))
        while(root.getParent() != null)
    		root = root.getParent();        
        System.out.print(root);
        System.out.print("\n");
    }
}
