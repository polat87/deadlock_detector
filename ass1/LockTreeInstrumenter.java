package vt14.ass1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Instruments a given list of classes for analysis with LockTree.
 * 
 */
public class LockTreeInstrumenter {

  /**
   * Instruments a given list of classes for analysis with LockTree.
   * 
   * @param args
   *          A list of names (the full names including the package) of classes
   *          that should be instrumented. It is assumed that the classes can be
   *          found in the classpath.
   */
  public static void main(String[] args) {

    if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
      printHelp();
      System.exit(0);
    }

    // TODO:
    // Place here your code to instrument the classes passed as arguments in args.
    // Your code should:
    //  - instrument not only the classes passed as arguments but also all all their
    //    nested classes,
    //  - insert a call to your code before or after any invocation of myLock.lock(), where
    //    myLock is either an instance of java.util.concurrent.locks.ReentrantLock or
    //    an instance of a class derived from java.util.concurrent.locks.ReentrantLock.
    //    In the function that is called, you can then build up the lock trees.
    // A tutorial on how to use Javassist can be found here:
    //   http://www.csg.ci.i.u-tokyo.ac.jp/~chiba/javassist/tutorial/tutorial.html
    // The following page might also be helpful:
    //   http://theholyjava.wordpress.com/2010/06/25/implementing-build-time-instrumentation-with-javassist/
    // Under the heading "Instrumenting a method call" you can find an example of how
    // to instrument all calls to javax.naming.NamingEnumeration.next() with additional
    // code that is executed before and after the call.
    
  }

  /**
   * Prints usage information to stdout.
   */
  public static void printHelp() {
    System.out.println("  Usage: java vt12.ass1.LockTreeInstrumenter c1 c2 c3 ...");
    System.out.println("         where each c<i> is a class name (including the package).");
    System.out.println("         This application uses Javassist, so you need to have");
    System.out.println("         javassist.jar in you classpath.");
    System.out.println("  Example: java vt12.ass1.LockTreeInstrumenter vt12.ass1.test.task2.Ex001");
  }

}
