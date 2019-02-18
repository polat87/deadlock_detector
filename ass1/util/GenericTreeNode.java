package vt14.ass1.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

//---------------------------------------------------------------------------
//This class represents a general tree node.
//It is supposed to aid you in developing your locktree algorithm and to produce
// correct output.
//You may change it to improve your implementation.
public class GenericTreeNode<E>
{
  protected GenericTreeNode<E> parent;
  protected List<GenericTreeNode<E> > children = new ArrayList<GenericTreeNode<E> >();

  protected E data;

  //-----------------------------------------------------------------------
  public GenericTreeNode() {
  }

  //-----------------------------------------------------------------------
  public GenericTreeNode(E data) {
    this.data = data;
  }

  //-----------------------------------------------------------------------
  public void addChild(GenericTreeNode<E> child) {
    if(child != null) {
      child.setParent(this);
      children.add(child);
    }
  }

  //-----------------------------------------------------------------------
  public void setParent(GenericTreeNode<E> parent) {
    this.parent = parent;
  }

  //-----------------------------------------------------------------------
  public GenericTreeNode<E> getParent() {
    return parent;
  }

  //-----------------------------------------------------------------------
  public final List<GenericTreeNode<E> > getChildren() {
    return children;
  }

  //-----------------------------------------------------------------------
  public int getNumberOfChildren() {
    return children.size();
  }

  //-----------------------------------------------------------------------
  public void setData(E data) {
    this.data = data;
  }

  //-----------------------------------------------------------------------
  public E getData() {
    return data;
  }

  //-----------------------------------------------------------------------
  public boolean equals(GenericTreeNode<E> treeNode) {
    if(!treeNode.data.equals(data) ||
       children.size() != treeNode.children.size()) {
      return false;
    }
    Iterator<GenericTreeNode<E> > iter = children.iterator();
    Iterator<GenericTreeNode<E> > iter2 = treeNode.children.iterator();
    while (iter.hasNext()) {
      GenericTreeNode<E> node = iter.next();
      GenericTreeNode<E> node2 = iter2.next();
      if (!node.equals(node2)) {
        return false;
      }
    }
    return true;
  }

  //-----------------------------------------------------------------------
  public String toString() {
    return toString(0);
  }

  //-----------------------------------------------------------------------
  // produces correctly indented output
  protected String toString(int level) {
    String str = "";
    for (int i = 0; i < level; ++i) {
      str += "  ";
    }
    if(data != null) {
      str += data.toString();
    }
    str += "\n";
    for (GenericTreeNode<E> node : children) {
      str += node.toString(level+1);
    }
    return str;
  }
}
