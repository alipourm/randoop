package randoop.test;

import java.io.IOException;
import java.util.Enumeration;

import junit.framework.*;
import randoop.Globals;
import randoop.util.Files;
import randoop.util.Reflection;

public class GenerateNewFailures {

  public static void failureReproduced(Class<? extends Test> junitTest) {
    try {
      TestSuite ts= new TestSuite((Class<? extends TestCase>) junitTest);
      TestResult result = new TestResult(); 
      ts.run(result);
      Enumeration failures = result.failures();
      StringBuilder s = new StringBuilder();
      while (failures.hasMoreElements()) {
        TestFailure f = (TestFailure) failures.nextElement();
        s.append(f.toString()+Globals.lineSep);
      }
      String filename = "newFailures.txt";
      Files.writeToFile(s.toString(),filename);
//    System.out.println("writting to:" + new File(filename).getCanonicalPath().toString());
//    System.out.println("NEW FAILURES: " + s.toString());
    } catch (IOException e) {
      System.exit(1);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    if (args.length == 0)
      System.exit(1);
    String junitClassName = args[0];
    Class<? extends Test> test= Reflection.classForName(junitClassName).asSubclass(Test.class);
    failureReproduced(test);
    System.out.println("DONE");
  }

}


