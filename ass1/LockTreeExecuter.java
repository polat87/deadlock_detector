package vt14.ass1;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Executes a Java program that has been instrumented with LockTree.
 * 
 * This application executes a Java program that has been instrumented for
 * LockTree. After execution, the resulting lock trees are analyzed for
 * potential deadlocks.
 * 
 * We do not do execution right after instrumentation (and in the same process)
 * because this can lead to class-loading problems: During instrumentation, the
 * classes that should be instrumented may be loaded, and when we then execute
 * the program we get the old, uninstrumented behavior.
 * 
 */
public class LockTreeExecuter {

  /**
   * Executes a Java program that has been instrumented with LockTree.
   * 
   * @param args
   *          The element args[0] is supposed to contain the name (the full name
   *          including the package) of java class that should be executed. All
   *          further elements of args contain parameters for the main() method.
   */
  public static void main(String[] args) {
    
    if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
      printHelp();
      System.exit(0);
    }

    // Invoke the main method:
    try {
      Class<?> c = Class.forName(args[0]);
      Class<?>[] argTypes = new Class[] { String[].class };
      Method main = c.getDeclaredMethod("main", argTypes);
      String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
      main.invoke(null, (Object) mainArgs);

    } catch (Exception x) {
      System.err.println(x.getMessage());
      x.printStackTrace();
    }

    // Check the lock-trees that have been produced:
    LockTreeChecker.getInstance().checkAll();

  }
  
  /**
   * Prints usage information to stdout.
   */
  public static void printHelp() {
    System.out.println("  Usage: java vt12.ass1.LockTreeExecuter package.JavaClass a1 a2 ...");
    System.out.println("         where ");
    System.out.println("          - package.JavaClass is the name of a Java class with a main()");
    System.out.println("            method that should be executed, and");
    System.out.println("          - a1 a2 ... are strings that serve as parameters for the");
    System.out.println("            main() method.");
    System.out.println("  Example: java vt12.ass1.LockTreeExecuter vt12.ass1.test.task2.Ex001");
  }

}
